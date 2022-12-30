import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.Scanner;


public class ClientUDP {
    static boolean endOfQuestions = false;
    final static String alert = "EndOfQuestionsAlert";

    public static void main(String args[]) throws IOException {

            DatagramSocket client = new DatagramSocket();

        //scanner
        Scanner sc = new Scanner(System.in);
        String answer;

            //authentication(client);
        sendAndReceive(client, "Rozpocznij rozwiazywanie kolokwium");

        while(!endOfQuestions) {
            answer = sc.nextLine();
            sendAndReceive(client, answer);
        }

        System.out.println("Koniec kolokwium");
        client.close();

    }
    static public void authentication(DatagramSocket client) throws IOException{

        InetAddress  IP = InetAddress.getByName("localhost");
        String message;
        byte[] buf;
        DatagramPacket packetSender;
        DatagramPacket packetReceiver;
        String received;

        boolean notConnected = true;
        while(notConnected){
            //wyslanie potwierdzenia
            Random random = new Random();
            Integer confirmCode = random.nextInt(1000000);
            message = confirmCode.toString();
            buf = message.getBytes();
            packetSender = new DatagramPacket(buf, buf.length, IP, 4999);
            client.send(packetSender);
            System.out.println(message);

            //odebranie potwierzenia
            buf = new byte[256];
            packetReceiver = new DatagramPacket(buf, buf.length);
            client.receive(packetReceiver);
            received = new String(packetReceiver.getData(), 0, packetReceiver.getLength());
            if(received.equals(confirmCode.toString())){
                notConnected = false;
            }
            System.out.println(received);
        }
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

        if(received.equals(alert)){
            endOfQuestions = true;
        }else {
            System.out.println(received);
        }

        return received;
    }
}

