import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class ClientUDP {
    public static void main(String args[]) throws IOException {

            DatagramSocket client = new DatagramSocket();

        //scanner
        Scanner sc = new Scanner(System.in);
        String answer;

            sendAndReceive(client, "prosba o pierwsze pytanie");

            boolean endOfQuestions = true;
        while(endOfQuestions) {

            answer = sc.nextLine();

            if(sendAndReceive(client, answer).equals("koniec pytan")){
                endOfQuestions = false;
            }
        }

            client.close();

    }
    static public String sendAndReceive(DatagramSocket client, String msg) throws IOException{

                   //wysylanie pakietu
                   InetAddress IP = InetAddress.getByName("localhost");
                   String message = msg;
                   byte[] buf = message.getBytes();
                   DatagramPacket packetSender = new DatagramPacket(buf, buf.length, IP, 4999);
                   client.send(packetSender);

                   //odebranie pakietu
                   buf = new byte[256];
                   DatagramPacket packetReceiver = new DatagramPacket(buf, buf.length);
                   client.receive(packetReceiver);
                   String received = new String(packetReceiver.getData(), 0, packetReceiver.getLength());
                   System.out.println(received);

                return received;
    }

//    static public void sendAndReceive(DatagramSocket client, String msg){
//          boolean success = false;
//           while(!success) {
//               try {
//                   //wysylanie pakietu
//                   InetAddress IP = InetAddress.getByName("localhost");
//                   String message = msg;
//                   byte[] buf = message.getBytes();
//                   DatagramPacket packetSender = new DatagramPacket(buf, buf.length, IP, 4999);
//                   client.send(packetSender);
//
//                   //odebranie pakietu
//                   buf = new byte[256];
//                   DatagramPacket packetReceiver = new DatagramPacket(buf, buf.length);
//                   client.receive(packetReceiver);
//                   String received = new String(packetReceiver.getData(), 0, packetReceiver.getLength());
//                   System.out.println(received);
//
//                   //weryfikacja
//                   if (msg.equals(received)) success = true;
//
//               } catch (Exception e) {
//                   System.err.println(e);
//               }
//           }
//    }

//    static public void receiveAndSend(DatagramSocket client, String msg){
//        boolean success = false;
//        while(!success) {
//            try {
//
//
//                //odebranie pakietu
//                buf = new byte[256];
//                DatagramPacket packetReceiver = new DatagramPacket(buf, buf.length);
//                client.receive(packetReceiver);
//                String received = new String(packetReceiver.getData(), 0, packetReceiver.getLength());
//                System.out.println(received);
//
//                //wysylanie pakietu
//                InetAddress IP = InetAddress.getByName("localhost");
//                String message = msg;
//                byte[] buf = message.getBytes();
//                DatagramPacket packetSender = new DatagramPacket(buf, buf.length, IP, 4999);
//                client.send(packetSender);
//
//                //weryfikacja
//                if (msg.equals(received)) success = true;
//
//            } catch (Exception e) {
//                System.err.println(e);
//            }
//        }
//    }

}

