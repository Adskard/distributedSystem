package cz.cvut.fel.dsv.chat;

import lombok.extern.java.Log;

import java.io.*;
import java.util.function.Predicate;
import java.util.logging.Level;

@Log
public class ConsoleInterface implements Runnable{

    private final PrintStream output = System.out;

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private boolean reading = true;
    private final Node node;

    public ConsoleInterface(Node node) {
        this.node = node;
    }


    private void parseCommands(String command){
        String[] formattedCommand = command.strip().split(" ");
        if(formattedCommand.length == 0) {
            printHelp();
            return;
        }

        Predicate<String> commandRecognition = knownCommand ->  formattedCommand[0].equalsIgnoreCase(knownCommand);

        if(commandRecognition.test("message")){
            try{
                sendMessage(formattedCommand[1],
                        command.substring(formattedCommand[1].length() + formattedCommand[0].length() + 2));
            }
            catch (RuntimeException ex){
                printHelp();
            }
        } else if (commandRecognition.test("hello")) {
            sendHello();
        }
        else if (commandRecognition.test("neighborhood")) {
            printNeighborhood();
        }
        else if (commandRecognition.test("registered")) {
            printRegisteredNodes();
        }
        else if (commandRecognition.test("requests")) {
            printChatRequests();
        }
        else if (commandRecognition.test("quit")) {
            try{
                reading = false;
            }
            catch (RuntimeException ex){
                printHelp();
            }
        } else {
            printHelp();
        }
    }

    private void printChatRequests(){
        output.println(node.getChatRequest());
    }

    private void printRegisteredNodes(){
        output.println(node.getRegisteredUserNodes().toString());
    }

    private void printNeighborhood(){
        output.println("Current neighbors");
        output.println(
                String.format("Leader: id:%s %s:%s",
                        node.getNeighborhood().getLeader().getNodeId(),
                        node.getNeighborhood().getLeader().getHost(),
                        node.getNeighborhood().getLeader().getPort()));
        output.println(
                String.format("Next: id:%s %s:%s",
                        node.getNeighborhood().getNext().getNodeId(),
                        node.getNeighborhood().getNext().getHost(),
                        node.getNeighborhood().getNext().getPort()));
        output.println(
                String.format("Next2: id:%s %s:%s",
                        node.getNeighborhood().getNext2().getNodeId(),
                        node.getNeighborhood().getNext2().getHost(),
                        node.getNeighborhood().getNext2().getPort()));
        output.println(
                String.format("Prev: id:%s %s:%s",
                        node.getNeighborhood().getPrev().getNodeId(),
                        node.getNeighborhood().getPrev().getHost(),
                        node.getNeighborhood().getPrev().getPort()));
    }

    private void sendHello(){
        output.println("Saying hello to neighbors!");
        node.sendHelloToNeighbors();
    }

    private void sendMessage(String recipient, String content){
        output.println("Sending message to " + recipient);
        node.sendChatMessage(recipient, content);
    }

    private void printHelp(){
        String help = """
                Commands:
                help - prints this help
                hello - sends a hello message to neighboring nodes
                message <recipientName> <messageContent> - sends a message to a recipient
                neighborhood - prints current neighborhood addresses
                quit - to exit application, takes several seconds to safely terminate
                registered - prints registered nodes for chat routing
                requests - prints routed chat requests""";
        output.println(help);
    }

    public void displayHello(String sender){
        output.printf("%s sent you a hello message\n", sender);
    }

    public void displayIncomingMessage(String sender, String content){
        output.printf("Incoming message from: %s%n\n", sender);
        output.println(content + "\n");
    }

    @Override
    public void run(){
        log.info("Running console interface");
        output.println("Welcome to GrpcChatApp!");
        String commandline;

        while (reading) {
            output.print("\ncmd> ");

            try {
                commandline = reader.readLine();
                parseCommands(commandline);
            } catch (IOException e) {
                log.log(Level.SEVERE, "Error parsing commandline", e);
                reading = false;
            }
        }

        try {
            node.terminateServer();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        output.println("Goodbye :)");
    }
}
