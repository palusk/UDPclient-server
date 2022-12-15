import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class ClientUDP {
    public static void main(String args[]) throws IOException {

            DatagramSocket client = new DatagramSocket();

            sendAndReceive(client, "pobierz moje dane");
            sendAndReceive(client, "jestem w wątku");

//
//            //scanner
//            Scanner sc = new Scanner(System.in);
//            String answer;
//
//            while(( question = bf.readLine()) != null) {
//
//                if(question.equals("timeError")){
//                    if((question = bf.readLine())==null) question = " ";
//                    System.out.println("Minal czas na odpowiedz");
//                }
//
//                System.out.println(question);
//
//                answer = sc.nextLine();
//                pw.println(answer);
//                pw.flush();
//            }




            client.close();

    }
    static public void sendAndReceive(DatagramSocket client, String msg){
          boolean success = false;
           while(!success) {
               try {
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

                   //weryfikacja
                   if (msg.equals(received)) success = true;

               } catch (Exception e) {
                   System.err.println(e);
               }
           }
    }

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

