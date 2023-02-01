package seti1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.UUID;

public class Receiver implements Runnable {
    private final MulticastSocket socket;
    private InetAddress address;
    private final DatagramPacket receivePacket;

    private static final int PACKET_SIZE = 16;

    public Receiver(MulticastSocket socket, String addressOfGroup) {
        try {
            address = InetAddress.getByName(addressOfGroup);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        receivePacket = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
        this.socket = socket;
    }

    @Override
    public void run() {
        EncryptPackage packetInfo = new EncryptPackage();
        KeepCopies keepCopies = new KeepCopies();

        try {
            socket.joinGroup(address);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        while (!Thread.interrupted()) {
            try {
                socket.receive(receivePacket);
            } catch (IOException ignored) {
            }

            UUID receiveUuid = packetInfo.getIdFromByte(receivePacket.getData());
            InetAddress receiveAddress = receivePacket.getAddress();
            keepCopies.checkMap(receiveAddress, receiveUuid);
        }

    }
}
