package seti1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.UUID;

public class Sender implements Runnable {
    private final MulticastSocket socket;
    private InetAddress address;
    private final DatagramPacket sendPacket;
    private UUID uuid;

    private EncryptPackage packetInfo = new EncryptPackage();

    private static final int LOCAL_PORT = 8000;

    public Sender (MulticastSocket socket, String addressOfGroup, UUID uuid) {
        try {
            address = InetAddress.getByName(addressOfGroup);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.socket = socket;
        this.uuid = uuid;
        byte[] packetByte = packetInfo.getIdAsByte(uuid);
        sendPacket = new DatagramPacket(packetByte, packetByte.length, address, LOCAL_PORT);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                socket.send(sendPacket);
            } catch (IOException ignored) {
            }
        }
    }
}
