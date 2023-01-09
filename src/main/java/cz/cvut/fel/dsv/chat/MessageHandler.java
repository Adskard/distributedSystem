package cz.cvut.fel.dsv.chat;

import cz.cvut.fel.dsv.chat.ChatProto.*;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import lombok.extern.java.Log;

import java.util.logging.Level;


@Log
public class MessageHandler {
    private final Node node;

    public MessageHandler(Node node) {
        this.node = node;
    }

    /**
     * Creates asynchronous stub for a given address
     * @param address for channel creation
     * @return asynchronous stub
     */
    private NodeGrpc.NodeStub getAsyncStubForAddress(Address address){
        return NodeGrpc.newStub(
                getChannelFromAddress(address));
    }

    /**
     * Creates a blocking stub for a given address
     * @param address for channel creation
     * @return blocking stub
     */
    private NodeGrpc.NodeBlockingStub getBlockingStubForAddress(Address address){
        return NodeGrpc.newBlockingStub(
                getChannelFromAddress(address));
    }

    /**
     * Creates a channel for a given address
     * @param address for channel creation
     * @return channel to address
     */
    private ManagedChannel getChannelFromAddress(Address address){
        ManagedChannel  m =ManagedChannelBuilder.forAddress(address.getHost(), address.getPort())
                .usePlaintext()
                .build();
        return  m;
    }

    public void sendElectionStart(Address startAddr, Address next){
        log.info("Sending election start message to: " + next.getNodeId());

        NodeGrpc.NodeBlockingStub stub = getBlockingStubForAddress(next);

        try{
            stub.startElection(AddressRequest.newBuilder()
                    .setTime(node.getTime())
                    .setAddress(startAddr)
                    .build());
        }
        catch (RuntimeException ex){
            log.log(Level.SEVERE, "Could not start election!");
        }
    }

    public void sendElectionResult(Address leaderAddr, Address next){
        log.info("Sending election result to: " + next.getNodeId());

        NodeGrpc.NodeBlockingStub stub = getBlockingStubForAddress(next);
        try {
            stub.leaderElected(AddressRequest.newBuilder()
                    .setAddress(leaderAddr)
                    .setTime(node.getTime())
                    .build());
        }
        catch (RuntimeException ex){
            log.warning(ex.getMessage());
            node.repairTopology(next);
        }

    }

    public void sendElectionVote(Address next, long uid, int phase){
        log.info(String.format("Sending phase %s vote with uid: %s", phase, uid));
        NodeGrpc.NodeBlockingStub stub = getBlockingStubForAddress(next);

        Vote vote = Vote.newBuilder()
                .setNodeId(uid)
                .setTime(node.getTime())
                .setPhase(phase)
                .build();

        try{
            stub.elect(vote);
        }
        catch (RuntimeException ex){
            log.log(Level.SEVERE, "Could not send vote!");
        }
    }

    /**
     * Sends a string message to a recipient through leader node invocation.
     * Recipient name must be registered on leaderNode.
     *
     * @param leaderNode address of leader node
     * @param recipient name of intended recipient
     * @param content message content
     */
    public void sendChatMessage(Address leaderNode, String recipient, String content){
        log.info("Sending chat message to: " + recipient);
        NodeGrpc.NodeStub stub = getAsyncStubForAddress(leaderNode);

        ChatRequest request = ChatRequest.newBuilder()
                .setRecipientName(recipient)
                .setContent(content)
                .setSender(node.getUsername())
                .setTime(node.getTime())
                .build();

        StreamObserver<BoolResponse> observer = new StreamObserver<>() {
            @Override
            public void onNext(BoolResponse value) {
                if(value.getSuccess()){
                    log.info("message successfully sent!");
                }
                else {
                    log.info("User name not found!");
                }
            }

            @Override
            public void onError(Throwable t) {
                log.log(Level.WARNING, "Message was not sent!", t);
                node.repairTopology(leaderNode);
            }

            @Override
            public void onCompleted() {

            }
        };

        stub.routeMessage(request, observer);
    }

    /**
     * Sends a direct message to node on given address
     * @param recipient address of recipient node
     * @param request message
     */
    public void sendDirectMessage(Address recipient, ChatRequest request){
        log.info("Sending direct chat message to: " + recipient.getNodeId());
        NodeGrpc.NodeBlockingStub stub = getBlockingStubForAddress(recipient);
        try{
            stub.receiveMessage(request);
        }
        catch (RuntimeException ex){
            log.warning(ex.getMessage());
            node.repairTopology(recipient);
        }
    }

    /**
     * Finds out if a node is still alive
     * @param nodeAddress address of possibly dead node
     * @return true if node is alive, false otherwise
     */
    public boolean isNodeResponsive(Address nodeAddress){
        log.info(String.format("Finding out if node %s is responsive", nodeAddress.getNodeId()));

        NodeGrpc.NodeBlockingStub stub = getBlockingStubForAddress(nodeAddress);

        ChatRequest request = ChatRequest.newBuilder()
                .build();
        try{
            stub.greet(request);
            return true;
        }
        catch(RuntimeException exception){
            log.warning("Node unresponsive id: " + nodeAddress.getNodeId());
            return false;
        }
    }

    /**
     * Sends repair request for the missing node to next node
     * @param next recipient of repair request
     * @param missingNode node that is currently missing
     */
    public void sendRepairRequest(Address next, Address missingNode){
        log.info(String.format("Sending repair request about node: %s to: %s",
                missingNode.getNodeId(), next.getNodeId()));

        NodeGrpc.NodeBlockingStub stub = getBlockingStubForAddress(next);

        try{
            stub.fixMissingNode(AddressRequest.newBuilder()
                    .setAddress(missingNode)
                    .setTime(node.getTime())
                    .build());
        }
        catch (RuntimeException ex){
            log.log(Level.SEVERE, "Could not fix missing node");
        }
    }

    /**
     * For getting information about node neighborhood
     * @param address address whose neighborhood we want
     * @return neighborhood of given address
     */
    public Neighborhood sendNeighborhoodInfoRequest(Address address){
        log.info("Sending neighborhood info request to: "+ address.getNodeId());
        NodeGrpc.NodeBlockingStub stub = getBlockingStubForAddress(address);

        try{
            return stub.getNeighborhood(NeighborhoodRequest.newBuilder()
                    .setTime(node.getTime())
                    .build());
        }
        catch (RuntimeException ex){
            log.log(Level.WARNING, "Node not responding");
            node.repairTopology(address);
        }
        return  null;
    }

    /**
     * Sends a join request to chat network
     * @param addressToJoin known gate node of chat network
     * @return assigned neighborhood for this node
     */
    public Neighborhood sendJoinRequest(Address addressToJoin){
        log.info("Sending join request to: " + addressToJoin.getNodeId());
        NodeGrpc.NodeBlockingStub stub = getBlockingStubForAddress(addressToJoin);

        Neighborhood n = null;

        try{
            n = stub.acceptJoin(AddressRequest.newBuilder()
                    .setAddress(node.getAddress())
                    .setTime(node.getTime())
                    .build());
        }
        catch(RuntimeException exception){
            log.log(Level.WARNING, "Could not connect to node");
        }

        return n;
    }

    /**
     * Sends a change request of previous node
     * For topology control
     * @param recipient recipient of change request
     * @param prevAddr new address for the recipient to change their prev to
     */
    public void sendPrevChangeRequest(Address recipient, Address prevAddr){
        log.info(String.format("Asking %s to change their prev to %s", recipient.getNodeId(), prevAddr.getNodeId()));

        NodeGrpc.NodeBlockingStub  stub = getBlockingStubForAddress(recipient);

        try{
            stub.setPrev(AddressRequest.newBuilder()
                    .setAddress(prevAddr)
                    .setTime(node.getTime())
                    .build());
        }
        catch (RuntimeException ex){
            log.log(Level.WARNING, "Unreachable node");
            node.repairTopology(recipient);
        }
    }

    /**
     * Sends a change request of next2 node
     * For topology control
     * @param recipient recipient of change request
     * @param newNext2 new address for recipient node to change their next2 to
     */
    public void sendNext2ChangeRequest(Address recipient, Address newNext2){
        log.info(String.format("Asking %s to change their next2 to %s", recipient.getNodeId(), newNext2.getNodeId()));

        NodeGrpc.NodeBlockingStub  stub = getBlockingStubForAddress(recipient);
        try{
            stub.setNext2(AddressRequest.newBuilder()
                    .setAddress(newNext2)
                    .setTime(node.getTime())
                    .build());
        }
        catch (RuntimeException ex){
            log.log(Level.WARNING, "Unreachable node");
            node.repairTopology(recipient);
        }
    }

    /**
     * Sends user registration request to leader node
     * for chat message routing
     * @param leader address of leader node
     * @param addrToRegister address for given username
     * @param username username for given address
     */
    public void sendRegistrationRequest(Address leader, Address addrToRegister, String username){
        log.info("Sending registration request");
        NodeGrpc.NodeBlockingStub stub = getBlockingStubForAddress(leader);

        RegistrationRequest registrationForm = RegistrationRequest.newBuilder()
                .setNodeAddress(addrToRegister)
                .setUsername(username)
                .setTime(node.getTime())
                .build();

        stub.registerUserNode(registrationForm);
    }

    /**
     * Hello message for given node
     * For topology diagnosis
     * @param address recipient
     */
    public void sendHelloMessage(Address address){
        NodeGrpc.NodeStub stub = getAsyncStubForAddress(address);
        StreamObserver<BoolResponse> observer = new StreamObserver<>() {
            @Override
            public void onNext(BoolResponse value) {

            }

            @Override
            public void onError(Throwable t) {
                log.log(Level.WARNING, "Could not send hello to neighbors");
                node.repairTopology(address);
            }

            @Override
            public void onCompleted() {
                log.fine("Sent hello to neighbors");
            }
        };

        ChatRequest request = ChatRequest.newBuilder()
                .setContent("Hello Neighbour!")
                .setSender(node.getUsername())
                .setRecipientName(String.valueOf(address.getNodeId()))
                .setTime(node.getTime())
                .build();
        try{
            stub.greet(request , observer);
        }
        catch(RuntimeException ex){
            log.log(Level.WARNING, "Node unreachable");

        }
    }
}
