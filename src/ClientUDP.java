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


        authorize(client, "authorization");

        while (!endOfQuestions) {
            answer = sc.nextLine();
            sendAndReceive(client, answer);
        }

        System.out.println("Koniec kolokwium");
        client.close();

    }

    static public String sendAndReceive(DatagramSocket client, String msg) throws IOException {

        boolean notConnected = true;
        byte[] buf;
        String received = new String();

        while (notConnected) {
            //wysylanie pakietu
            InetAddress IP = InetAddress.getByName("localhost");
            String message = msg;
            buf = message.getBytes();
            DatagramPacket packetSender = new DatagramPacket(buf, buf.length, IP, 4999);
            client.send(packetSender);

            //odebranie pakietu do weryfikacji
            buf = new byte[256];
            DatagramPacket packetReceiver = new DatagramPacket(buf, buf.length);
            client.receive(packetReceiver);
            received = new String(packetReceiver.getData(), 0, packetReceiver.getLength());

            if (message.equals(received)) {
                System.out.println("autoryzacja przebiegła pomyślnie");
                notConnected = false;
            }
        }

        //odebranie pakietu
        buf = new byte[256];
        DatagramPacket packetReceiver = new DatagramPacket(buf, buf.length);
        client.receive(packetReceiver);
        received = new String(packetReceiver.getData(), 0, packetReceiver.getLength());
        if (received.substring(0,5).equals("Wynik")) {
            System.out.println(received);
            endOfQuestions = true;
        } else {
            System.out.println(received);
        }
        return received;
    }


    static public void authorize(DatagramSocket client, String msg) throws IOException {

        boolean notConnected = true;
        byte[] buf;
        String received = new String();

        while (notConnected) {

            //wysylanie pakietu
            InetAddress IP = InetAddress.getByName("localhost");
            String message = msg;
            buf = message.getBytes();
            DatagramPacket packetSender = new DatagramPacket(buf, buf.length, IP, 4999);
            client.send(packetSender);

            //odebranie pakietu do weryfikacji
            buf = new byte[256];
            DatagramPacket packetReceiver = new DatagramPacket(buf, buf.length);

            client.receive(packetReceiver);

            received = new String(packetReceiver.getData(), 0, packetReceiver.getLength());

            if (message.equals(received)) notConnected = false;

        }
        System.out.println("autoryzacja przebiegła pomyślnie");
        System.out.println("wciśnij ENTER aby rozpocząć kolokwium");
    }
}

