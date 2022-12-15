package cz.cvut.fel.dsv.chat;

import lombok.extern.java.Log;

import java.io.*;
import java.util.function.Predicate;
import java.util.logging.Level;

@Log
public class ConsoleInterface implements Runnable{

    private final PrintStream output = System.out;
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

        if(commandRecognition.test("send")){
            try{
                sendMessage(formattedCommand[1], formattedCommand[2]);
            }
            catch (RuntimeException ex){
                log.log(Level.WARNING, "Invalid send command", ex);
                printHelp();
            }
        } else if (commandRecognition.test("hello")) {
            sendHello();

        } else if (commandRecognition.test("quit")) {
            reading = false;
        } else {
            printHelp();
        }
    }

    private void sendHello(){
        output.println("Saying hello to neighbors");
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
                send <recipientName> <message> - sends a message to a recipient""";
        output.println(help);
    }

    public void displayHello(String sender){
        output.printf("%s sent you a hello message!%n", sender);
    }

    public void displayIncomingMessage(String sender, String content){
        output.printf("Incoming message from: %s%n", sender);
        output.println(content);
    }

    @Override
    public void run(){
        log.info("Running console interface");
        output.println("Welcome to GrpcChatApp!");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String commandline;

        while (reading) {
            output.print("chat> ");
            try {
                commandline = reader.readLine();
                parseCommands(commandline);
            } catch (IOException e) {
                e.printStackTrace();
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
