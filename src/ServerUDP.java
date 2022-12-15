import java.io.*;
import java.net.*;
public class ServerUDP {
    public static void main(String[] args) throws IOException {

        DatagramSocket server = new DatagramSocket(4999);

            receive(server);

//        server.close();
    }

   static public void receive(DatagramSocket server){
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

            //utworzenie watku
            ServerUDPThread x = new ServerUDPThread(packetReceiver.getAddress(),packetReceiver.getPort(), server);
            System.out.println("utowrzono watek dla  ->"+packetReceiver.getAddress()+" "+packetReceiver.getPort()+"<-");
        } catch (Exception e) {
           System.err.println(e);
        }
    }

}
