import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerUDPThread extends Thread{
    InetAddress address;
    int port;
    DatagramSocket server;

    ServerUDPThread(InetAddress address, int port, DatagramSocket server){
        this.address = address;
        this.port = port;
        this.server = server;
    }

    @Override
    public void run() {

            receive(server);

    }


     public void sendAndReceive(DatagramSocket server, String msg){
        try {

            //wysylanie pakietu
            String message = msg;
            byte[] buf = message.getBytes();
            DatagramPacket packetSender = new DatagramPacket( message.getBytes(), message.length(), address, port);
            server.send(packetSender);

            //odebranie pakietu
            buf = new byte[256];
            DatagramPacket packetReceiver = new DatagramPacket(buf, buf.length);
            server.receive(packetReceiver);
            String received = new String(packetReceiver.getData(), 0, packetReceiver.getLength());
            System.out.println(received);

        } catch (Exception e) {
            System.err.println(e);
        }
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
            System.out.println("jestem w wątku  ->"+packetReceiver.getAddress()+" "+packetReceiver.getPort()+"<-");
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
