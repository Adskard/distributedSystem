package cz.cvut.fel.dsv.chat;

import com.google.rpc.context.AttributeContext;
import cz.cvut.fel.dsv.chat.ChatProto.ChatResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ManagedChannelProvider;
import io.grpc.stub.StreamObserver;
import lombok.extern.java.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@Log
public class MessageHandler {

    private ManagedChannel channel;

    private NodeGrpc.NodeStub stub;
    private NodeGrpc.NodeBlockingStub blockingStub;

    private final Address sender;

    public MessageHandler(Address sender) {
        this.sender = sender;
    }

    public void sendRoutedMessage(String recipient, String content){
        log.info(String.format("Sending message from:%s to:%s", sender, recipient));
    }

    public void sendDirectMessage(Address recipient, String content){
        log.info(String.format("Sending message from:%s to:%s", sender, recipient));
    }

    public void sendMessage(String host, int port, String content){
        final CountDownLatch finishLatch = new CountDownLatch(1);

        channel = ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). Disable TLS to avoid certificates.
                .usePlaintext()
                .build();

        stub = NodeGrpc.newStub(channel);
        blockingStub = NodeGrpc.newBlockingStub(channel);
        StreamObserver<ChatResponse> responseStreamObserver = new StreamObserver<>() {
            @Override
            public void onNext(ChatResponse sumResult) {
                // There will be only one result.
                System.out.println("nice");
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();  // return value already handled (it was error)
            }
        };

        ChatProto.ChatRequest request = ChatProto.ChatRequest.newBuilder()
                .setContent(content)
                .setRecipientName(host)
                .setSenderId(12)
                .build();

        stub.receiveMessage(request, responseStreamObserver);
        try {
            finishLatch.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException ie) {
            System.out.println(ie.getMessage());
        }
    }
}
