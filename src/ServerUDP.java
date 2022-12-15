import java.io.*;
import java.net.*;
public class ServerUDP {
    public static void main(String[] args) throws IOException {

        DatagramSocket server = new DatagramSocket(4999);

        receiveAndRespond(server);
        receiveAndRespond(server);
        receiveAndRespond(server);
        receiveAndRespond(server);

        server.close();
    }

   static public void receiveAndRespond(DatagramSocket server){
       try {

        //odebranie
        byte[] buf = new byte[50];
        DatagramPacket packetReceiver = new DatagramPacket(buf, buf.length);
        server.receive(packetReceiver);
        String received = new String(packetReceiver.getData(), 0 , packetReceiver.getLength());
        System.out.println(received);

        //odeslanie
        DatagramPacket packetSender = new DatagramPacket( received.getBytes(), received.length(), packetReceiver.getAddress(), packetReceiver.getPort());
            server.send(packetSender);

        } catch (Exception e) {
           System.err.println(e);
        }
    }
}
