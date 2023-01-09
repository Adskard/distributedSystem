package cz.cvut.fel.dsv.chat;


import cz.cvut.fel.dsv.chat.election.ElectionDetails;
import cz.cvut.fel.dsv.chat.election.ElectionState;
import cz.cvut.fel.dsv.chat.exception.NameNotFoundException;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import cz.cvut.fel.dsv.chat.ChatProto.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Main node class for node initialization and runtime.
 */
@Log
@Getter
@Setter
public class Node{

    /**
     * Unique node identification.
     */
    private final long id;

    private long time = 0;

    /**
     * Details for leader election process.
     */
    private ElectionDetails electionDetails;

    /**
     * Node name for chat integration.
     */
    private final String username;

    /**
     * Node address for communication
     */
    private final Address address;

    /**
     * Topology repair in progress lock.
     */
    private boolean repairInProgress = false;

    /**
     * Handler for communication between nodes.
     */
    private final MessageHandler messageHandler;

    /**
     * Information about connected nodes.
     */
    private Neighborhood neighborhood;

    /**
     * Console interface for chat interaction.
     */
    private final ConsoleInterface consoleInterface;

    /**
     * Mapping of usernames to addresses, for leader node to facilitate communication.
     */
    private final Map<String, Address> registeredUserNodes = new HashMap<>();

    /**
     * Requests going through this node.
     */
    private final List<ChatRequest> chatRequest = new ArrayList<>();

    /**
     * Grpc server for listening on designated address.
     */
    private final Server listeningServer;

    public static void main(String[] args) throws IOException {

        //parsing arguments
        String username = args[0];
        String hostName = args[1];
        int appPort = Integer.parseInt(args[2]);

        //init node
        Node localNode = new Node(username, hostName, appPort);

        //log to file
        //log is from lombok @Log
        FileHandler fileHandler = new FileHandler(username + appPort + ".log");
        log.addHandler(fileHandler);

        SimpleFormatter formatter = new SimpleFormatter() {
            private static final String format = "[%1$tT - lc %4$s] [%2$s] %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                        new Date(lr.getMillis()),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage(),
                        localNode.getTime()
                );
            }
        };
        fileHandler.setFormatter(formatter);

        //start server
        log.info("Starting server on " + localNode.address);
        localNode.listeningServer.start();

        //debug join gate node
        if(appPort != 50000){
            localNode.joinChat(Address.newBuilder()
                    .setHost("localhost")
                    .setPort(50000)
                    .setNodeId(localNode.generateId("localhost", "Ivan",50000))
                    .build());
        }

        // shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Server shutting down");
            try {
                localNode.terminateServer();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));

        //run interface
        new Thread(localNode.consoleInterface).run();
    }

    /**
     * Basic chat node foundation
     *
     * @param username name of user on this chat node
     * @param hostname network interface name
     * @param port port on given interface
     */
    public Node(String username, String hostname, int port){
        log.info(String.format("Initializing local node at port: %s", port));

        id = generateId(hostname, username, port);

        this.username = username;


        address = Address.newBuilder()
                .setPort(port)
                .setHost(hostname)
                .setNodeId(id)
                .build();

        registeredUserNodes.put(username, address);

        neighborhood = Neighborhood.newBuilder()
                .setNext(address)
                .setLeader(address)
                .setNext2(address)
                .setPrev(address)
                .build();

        messageHandler = new MessageHandler(this);

        electionDetails = new ElectionDetails();
        electionDetails.setState(ElectionState.ACTIVE);
        electionDetails.setVirtualId(id);

        consoleInterface = new ConsoleInterface(this);

        listeningServer = ServerBuilder.forPort(port)
                .addService(new NodeService(this))
                .build();
    }

    private void updateTime(long receivedTime){
        time = receivedTime > time ? receivedTime : time;
        time += 1;
    }

    private void updateTime(){
        time += 1;
    }

    /**
     * Safe listening server termination
     * @throws InterruptedException if server did not exit safely
     */
    public void terminateServer() throws InterruptedException {
        log.info(String.format("Terminating server on port %s", listeningServer.getPort()));
        listeningServer.shutdown();
        if (Objects.nonNull(listeningServer)) listeningServer.awaitTermination(2,TimeUnit.SECONDS);
    }

    /**
     * Generates unique id for this node
     * @param hostname
     * @param nodeName
     * @param port
     * @return
     */
    private int generateId(String hostname, String nodeName, int port){
        int generatedId = 0;
        generatedId += nodeName.hashCode() * 1000000000;
        generatedId += hostname.hashCode() * 1000000000;
        generatedId += port * 1000000000;
        return generatedId;
    }

    /**
     * Relays information about election start
     */
    public void electionBegin(Address startAddr){
        log.log(Level.INFO, "Starting election");
        Address next = neighborhood.getNext();

        //void details from possible last election
        electionDetails.setState(ElectionState.ACTIVE);
        electionDetails.setPrev2Id(null);
        electionDetails.setPrevId(null);
        electionDetails.setVirtualId(id);

        //relay election start
        messageHandler.sendElectionStart(startAddr, neighborhood.getNext());

        //send first vote
        messageHandler.sendElectionVote(next, electionDetails.getVirtualId(), 1);
    }

    /**
     * Peterson leader election algorithm implementation
     *
     * @param uid
     */
    public void elect(long uid, int phase){
        log.info(String.format("Received phase %s vote %s", phase, uid));

        Address next = neighborhood.getNext();

        //relay message if passive
        if(electionDetails.getState().equals(ElectionState.PASSIVE)){
            log.info("Passively relaying message");
            messageHandler.sendElectionVote(next, uid, phase);
            return;
        }

        updateTime();

        if(electionDetails.getState().equals(ElectionState.ACTIVE)){
            //vote 1 was sent at election start

            //wait for vote 1
            if(phase == 1){
                log.info("Got first vote");
                electionDetails.setPrevId(uid);

                //send vote 2 with node own id
                messageHandler.sendElectionVote(next, uid, 2);
            }

            //wait for vote 2
            else if (phase == 2) {
                log.info("Got second vote");
                electionDetails.setPrev2Id(uid);
            }

            //continue only after getting both votes
            if(Objects.isNull(electionDetails.getPrev2Id()) && Objects.isNull(electionDetails.getPrevId()))
                return;

            //change state based on vote result
            //virtual id traversed whole topology, this node is leader
            if(Long.valueOf(uid).equals(electionDetails.getVirtualId())){
                log.info("I am the Leader!");
                electionDetails.setState(ElectionState.LEADER);
                messageHandler.sendElectionResult(address, next);
            }

            //node updates its virtual id, voting continues, start new round
            else if (electionDetails.getPrevId() > electionDetails.getVirtualId() &&
                electionDetails.getPrevId() > electionDetails.getPrev2Id()) {
                log.info("Election round end!");

                log.info(String.format("my vId: %s prevId: %s prev2Id: %s", electionDetails.getVirtualId(),
                        electionDetails.getPrevId(), electionDetails.getPrev2Id()));

                electionDetails.setVirtualId(electionDetails.getPrevId());

                //forget losers
                electionDetails.setPrevId(null);
                electionDetails.setPrev2Id(null);

                //send vote 1 of new round
                messageHandler.sendElectionVote(next, electionDetails.getVirtualId(), 1);
            }

            //node is out of the election and turns to relay
            else{
                electionDetails.setState(ElectionState.PASSIVE);
                log.info("Election state changed to passive");
            }
        }
    }

    /**
     * Sets and relays information about leader election winner.
     *
     * @param leader Address of newly elected leader node
     */
    public void confirmElectionResult(Address leader){
        log.info("Confirming election result");
        updateTime();

        Neighborhood newLeaderNeigh = Neighborhood.newBuilder().setLeader(leader)
                .setPrev(neighborhood.getPrev())
                .setNext2(neighborhood.getNext2())
                .setNext(neighborhood.getNext())
                .build();

        setNeighborhood(newLeaderNeigh);

        //register
        messageHandler.sendRegistrationRequest(leader, address, username);

        //confirmation went full circle
        if(leader.equals(address)) return;

        //relay
        messageHandler.sendElectionResult(leader, neighborhood.getNext());

    }


    /**
     * Changes this nodes neighborhood to accommodate the joining node.
     * Sets this neighborhood next to joining node.
     *
     * @param joiningAddr address of the joining node
     * @return neighborhood to be adopted by the joining node
     */
    public Neighborhood acceptNewNode(Address joiningAddr){
        log.info(String.format("New node %s is joining me", joiningAddr.getNodeId()));

        boolean firstJoin = neighborhood.getNext().equals(address);

        //register self
        if(firstJoin) registeredUserNodes.put(username, address);

        Neighborhood joiningNeighborhood = Neighborhood.newBuilder()
                .setLeader(neighborhood.getLeader())
                .setPrev(address)
                .setNext(neighborhood.getNext())
                .setNext2(firstJoin ? joiningAddr : neighborhood.getNext2())
                .build();

        //repeated join
        //when node sends a repeat join request
        if(joiningAddr.equals(neighborhood.getNext())){
            Neighborhood n2 = messageHandler.sendNeighborhoodInfoRequest(neighborhood.getNext2());
            joiningNeighborhood = Neighborhood.newBuilder()
                    .setLeader(neighborhood.getLeader())
                    .setPrev(address)
                    .setNext(neighborhood.getNext2())
                    .setNext2(n2.getNext())
                    .build();
            return joiningNeighborhood;
        }

        Neighborhood thisNewNeighborhood = Neighborhood.newBuilder()
                .setLeader(neighborhood.getLeader())
                .setPrev(neighborhood.getPrev())
                .setNext(joiningAddr)
                .setNext2(neighborhood.getNext())
                .build();

        setNeighborhood(thisNewNeighborhood);

        updateTime();

        return  joiningNeighborhood;
    }

    /**
     * Join running chat
     * @param gate known chat node
     */
    public void joinChat(Address gate){
        log.info("Joining new chat system through: " + gate.getNodeId());

        //join topology
        Neighborhood myNeighborhood = messageHandler.sendJoinRequest(gate);

        updateTime();
        setNeighborhood(myNeighborhood);
        log.info("My new neighborhood "+ neighborhood.toString());

        //register chat
        messageHandler.sendRegistrationRequest(neighborhood.getLeader(), address, username);

        updateTime();
    }

    /**
     * Process for repairing topology, disconnecting misssing node,
     * begins leader election if leader is missing
     * @param missingNode node that is unreachable
     */
    public void repairTopology(Address missingNode){
        log.info("Trying to repair topology");
        if(repairInProgress){
            log.info("Repair already in progress");
            return;
        }
        repairInProgress = true;
        //This nodes next is missing
        if(neighborhood.getNext().equals(missingNode)){
            log.info("Changing topology");
            Address newNext = neighborhood.getNext2();
            Neighborhood tmpTopology = messageHandler.sendNeighborhoodInfoRequest(newNext);

            Neighborhood newNeighborhood = Neighborhood.newBuilder()
                    .setLeader(neighborhood.getLeader())
                    .setPrev(neighborhood.getPrev())
                    .setNext(newNext)
                    .setNext2(tmpTopology.getNext())
                    .build();

            setNeighborhood(newNeighborhood);

            updateTime();

            messageHandler.sendPrevChangeRequest(newNext, address);
            messageHandler.sendNext2ChangeRequest(neighborhood.getPrev(), newNext);
            log.info("Topology repaired");
        }
        else{
            messageHandler.sendRepairRequest(neighborhood.getNext(), missingNode);
            return;
        }

        repairInProgress = false;
        //repair leader
        if(!messageHandler.isNodeResponsive(neighborhood.getLeader())){
            electionBegin(address);
        }
    }


    /**
     * For sending a chat message using chat system
     * @param recipient name of registered system recipient
     * @param content content of message
     */
    public void sendChatMessage(String recipient, String content){
        messageHandler.sendChatMessage(neighborhood.getLeader(), recipient, content);
    }

    public void sendHelloToNeighbors(){
        messageHandler.sendHelloMessage(neighborhood.getLeader());
        messageHandler.sendHelloMessage(neighborhood.getNext());
        messageHandler.sendHelloMessage(neighborhood.getPrev());
        messageHandler.sendHelloMessage(neighborhood.getNext2());
    }

    /**
     * Grpc node service implementation
     */
    class NodeService extends NodeGrpc.NodeImplBase{
        private final Node node;
        public NodeService(Node node) {
            this.node = node;
        }


        @Override
        public void receiveMessage(ChatRequest request,
                                StreamObserver<BoolResponse> responseObserver) {
            log.info("Receiving message from" + request.getSender());
            updateTime(request.getTime());

            try {
                consoleInterface.displayIncomingMessage(request.getSender(), request.getContent());
                responseObserver.onNext(BoolResponse.newBuilder().setSuccess(true).build());

                responseObserver.onCompleted();
            }
            catch (RuntimeException ex){
                log.warning(ex.getMessage());
                responseObserver.onError(ex);
            }


        }

        @Override
        public void routeMessage(ChatRequest request,
                                StreamObserver<BoolResponse> responseObserver) {
            log.info(String.format("Routing message to %s from %s", request.getRecipientName(), request.getSender()));
            updateTime(request.getTime());
            chatRequest.add(request);

            try{
                Address recipientAddress = registeredUserNodes.get(request.getRecipientName());

                if(Objects.isNull(recipientAddress)) throw new NameNotFoundException("Recipient name not found!");

                messageHandler.sendDirectMessage(recipientAddress, request);

                responseObserver.onNext(BoolResponse.newBuilder().setSuccess(true).build());
                responseObserver.onCompleted();
            }
            catch (NameNotFoundException ex){
                log.warning(ex.getMessage());
                responseObserver.onNext(BoolResponse.newBuilder().setSuccess(false).build());
                responseObserver.onCompleted();
            }
            catch (RuntimeException ex){
                log.warning(ex.getMessage());
                responseObserver.onError(ex);
            }


        }

        @Override
        public void greet(ChatRequest request,
                              StreamObserver<BoolResponse> responseObserver){
            log.info(request.getSender() + " is sending a greeting");
            updateTime(request.getTime());

            try {
                node.consoleInterface.displayHello(request.getSender());
                responseObserver.onNext(BoolResponse.newBuilder().setSuccess(true).build());
                responseObserver.onCompleted();
            }
            catch (RuntimeException ex){
                log.warning(ex.getMessage());
                responseObserver.onError(ex);
            }
        }

        @Override
        public void startElection(AddressRequest startAddr, StreamObserver<BoolResponse> responseObserver) {
            log.info("Got election start message from: " + startAddr.getAddress().getNodeId());
            updateTime(startAddr.getTime());

            try {
                if(startAddr.getAddress().equals(address)){
                    responseObserver.onNext(BoolResponse.newBuilder().setSuccess(false).build());
                }
                else{
                    electionBegin(startAddr.getAddress());
                    responseObserver.onNext(BoolResponse.newBuilder().setSuccess(true).build());
                }
                responseObserver.onCompleted();
            }
            catch (RuntimeException ex){
                log.warning(ex.getMessage());
                responseObserver.onError(ex);
            }
        }


        @Override
        public void elect(Vote request, StreamObserver<BoolResponse> responseObserver) {
            log.info("Got election vote message");
            updateTime(request.getTime());

            try {
                node.elect(request.getNodeId(), request.getPhase());
                responseObserver.onNext(BoolResponse.newBuilder().setSuccess(true).build());
                responseObserver.onCompleted();
            }
            catch (RuntimeException ex){
                log.warning(ex.getMessage());
                responseObserver.onError(ex);
            }
        }

        @Override
        public void leaderElected(AddressRequest newLeaderAddr, StreamObserver<BoolResponse> responseObserver) {
            log.info("Got information about new leader: "+ newLeaderAddr);
            updateTime(newLeaderAddr.getTime());

            try{
                confirmElectionResult(newLeaderAddr.getAddress());
                responseObserver.onNext(BoolResponse.newBuilder().setSuccess(true).build());
                responseObserver.onCompleted();
            }
            catch (RuntimeException ex){
                log.log(Level.WARNING, "Election result could not be confirmed");
                responseObserver.onError(ex);
            }
        }

        @Override
        public void acceptJoin(AddressRequest joinRequest, StreamObserver<Neighborhood> responseObserver) {
            Address joiningAddr = joinRequest.getAddress();
            log.info("Accepting join request by: " + joiningAddr.getNodeId());
            updateTime(joinRequest.getTime());

            try{
                Neighborhood joiningNeighborhood = node.acceptNewNode(joiningAddr);

                messageHandler.sendPrevChangeRequest(joiningNeighborhood.getNext(), joiningAddr);
                messageHandler.sendNext2ChangeRequest(neighborhood.getPrev(), joiningAddr);

                responseObserver.onNext(joiningNeighborhood);
                responseObserver.onCompleted();
            }
            catch (RuntimeException ex){
                log.log(Level.WARNING, "Node could not join");
                responseObserver.onError(ex);
            }
        }

        @Override
        public void getNeighborhood(NeighborhoodRequest request, StreamObserver<Neighborhood> responseObserver) {
            log.info("Neighborhood info requested" );
            updateTime(request.getTime());
            responseObserver.onNext(neighborhood);
            responseObserver.onCompleted();
        }

        @Override
        public void setNext2(AddressRequest newNext2Addr, StreamObserver<BoolResponse> responseObserver) {
            log.info(String.format("Changing next2 node from %s to %s",
                    node.getNeighborhood().getNext2().getNodeId(), newNext2Addr.getAddress().getNodeId()));
            updateTime(newNext2Addr.getTime());

            try{
                node.setNeighborhood(Neighborhood.newBuilder()
                        .setNext(node.getNeighborhood().getNext())
                        .setNext2(newNext2Addr.getAddress())
                        .setPrev(node.getNeighborhood().getPrev())
                        .setLeader(node.getNeighborhood().getLeader())
                        .build());

                responseObserver.onNext(BoolResponse.newBuilder().setSuccess(true).build());
                responseObserver.onCompleted();
            }
            catch(RuntimeException ex){
                log.warning(ex.getMessage());
                responseObserver.onError(ex);
            }

        }

        @Override
        public void setPrev(AddressRequest newPrevAddr, StreamObserver<BoolResponse> responseObserver) {
            log.info(String.format("Changing prev node from %s to %s",
                    node.getNeighborhood().getPrev().getNodeId(), newPrevAddr.getAddress().getNodeId()));
            updateTime(newPrevAddr.getTime());

            try{
                node.setNeighborhood(Neighborhood.newBuilder()
                        .setNext(node.getNeighborhood().getNext())
                        .setNext2(node.getNeighborhood().getNext2())
                        .setPrev(newPrevAddr.getAddress())
                        .setLeader(node.getNeighborhood().getLeader())
                        .build());

                responseObserver.onNext(BoolResponse.newBuilder().setSuccess(true).build());

                responseObserver.onCompleted();
            }
            catch(RuntimeException ex){
                log.warning(ex.getMessage());
                responseObserver.onError(ex);
            }
        }

        @Override
        public void fixMissingNode(AddressRequest missingNode, StreamObserver<BoolResponse> responseObserver) {
            log.info("Got missing node request " + missingNode.getAddress().getNodeId());
            updateTime(missingNode.getTime());

            try {
                node.repairTopology(missingNode.getAddress());
                responseObserver.onNext(BoolResponse.newBuilder().setSuccess(true).build());
                responseObserver.onCompleted();
            }
            catch (RuntimeException ex){
                log.warning(ex.getMessage());
                responseObserver.onError(ex);
            }
        }

        @Override
        public void registerUserNode(RegistrationRequest registrationForm,
                                     StreamObserver<BoolResponse> responseObserver) {
            log.info("Registering new node username: " + registrationForm.getUsername());
            updateTime(registrationForm.getTime());

            try{
                node.registeredUserNodes.put(registrationForm.getUsername(), registrationForm.getNodeAddress());
                responseObserver.onNext(BoolResponse.newBuilder().setSuccess(true).build());
                responseObserver.onCompleted();
                log.info("Registration success");
            }
            catch (RuntimeException ex){
                responseObserver.onError(ex);
                log.warning("Registration failed");
            }
        }

    }
}
