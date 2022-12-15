import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientUDP {
    public static void main(String args[]) throws IOException {
        String[] tempArgs = new String[2];
        tempArgs[0] = "localhost";
        tempArgs[1] = "4999";

        if (tempArgs.length < 2)
            System.out.println("Wprowadz adres serwera TCP oraz numer portu");
        else {
            int port = 0;
            try {
                port = Integer.parseInt(tempArgs[1]);
            } catch (NumberFormatException e) {
                System.err.println("Wprowadl pcprawny numer portu: " + e);
                return;
            }

            DatagramSocket client = new DatagramSocket();
            InetAddress IP = InetAddress.getByName(tempArgs[0]);
            String message = "message";
            byte[] buf = message.getBytes();
            DatagramPacket p = new DatagramPacket(buf, buf.length, IP, 4999);
            client.send(p);


        }
    }
}
