package seti1;

import java.io.IOException;
import java.net.MulticastSocket;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static final int LOCAL_PORT = 8000;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Wrong args for program");
            System.exit(0);
        }

        UUID uuid = UUID.randomUUID();
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(LOCAL_PORT);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Sender sender = new Sender(socket, args[0], uuid);
        Receiver receiver = new Receiver(socket, args[0]);
        Thread sendThread = new Thread(sender);
        Thread receiveThread = new Thread(receiver);

        receiveThread.start();
        sendThread.start();

        Scanner scanner = new Scanner(System.in);
        String word;
        while (true) {
            word = scanner.nextLine();
            if ("exit".equals(word)) {
                socket.close();
                try {
                    sendThread.interrupt();
                    sendThread.join();
                    receiveThread.interrupt();
                    receiveThread.join();
                    System.exit(0);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
