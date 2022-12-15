package cz.cvut.fel.dsv.chat;


import io.grpc.*;
import io.grpc.stub.StreamObserver;
import lombok.extern.java.Log;
import cz.cvut.fel.dsv.chat.ChatProto.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Log
public class Node implements Runnable{
    private final MessageHandler messageHandler;
    private final Neighborhood neighborhood;
    private final Address address;
    private final ConsoleInterface consoleInterface;

    private final List<Address> registeredUserNodes = new ArrayList<>();

    private final List<ChatRequest> chatRequest = new ArrayList<>();

    private final Server listeningServer;

    public Node(String username, String hostname, int port){
        log.info(String.format("Initializing local node at port: %s", port));

        address = new Address(username, hostname, port);

        neighborhood = new Neighborhood(address);
        messageHandler = new MessageHandler(address);

        consoleInterface = new ConsoleInterface(this);

        listeningServer = ServerBuilder.forPort(port)
                .addService(new NoseService(this))
                .build();
    }

    public void sendChatMessage(String recipient, String content){

    }

    public void sendHelloToNeighbors(){

    }

    public void testConnection(){
        messageHandler.sendDirectMessage(new Address("Pepa", "localhost", 50001), "Ahoj");
    }

    public static void main(String[] args) throws IOException {
        String localName = args[0];
        int appPort = Integer.parseInt(args[1]);

        Node localNode = new Node(localName, "localhost", appPort);
        log.info("Running local chat node: " + localNode.address);
        localNode.run();

        new Thread(localNode.consoleInterface).run();
    }

    public void terminateServer() throws InterruptedException {
        log.info(String.format("Terminating server on port %s", listeningServer.getPort()));
        if (Objects.nonNull(listeningServer)) listeningServer.awaitTermination(10,TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        try {
            listeningServer.start();
        } catch (IOException ex) {
            log.log(Level.SEVERE,String.format("Listening server on port %s could not start!",
                    listeningServer.getPort()) ,ex);
            throw new RuntimeException(ex);
        }
    }
    /**
     * Grpc node service implementation
     */
    class NoseService extends NodeGrpc.NodeImplBase{
        private final Node node;
        public NoseService(Node node) {
            this.node = node;
        }

        @Override
        public void receiveMessage(ChatRequest request,
                                StreamObserver<ChatResponse> responseObserver) {

            consoleInterface.displayIncomingMessage(request.getRecipientName(), request.getContent());
            responseObserver.onNext(ChatResponse.newBuilder().setSuccess(true).build());

            responseObserver.onCompleted();
        }

        @Override
        public void routeMessage(ChatRequest request,
                                StreamObserver<ChatResponse> responseObserver) {
            responseObserver.onNext(ChatResponse.newBuilder().setSuccess(true).build());

            responseObserver.onCompleted();
        }

        @Override
        public void sendHello(ChatRequest request,
                              StreamObserver<ChatResponse> responseObserver){

        }

        @Override
        public void registerUserNode(RegistrationForm request,
                                     StreamObserver<ChatResponse> responseObserver) {
        }
    }
}
