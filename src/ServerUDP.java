import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.Instant;

public class ServerUDP {
    public static void main(String[] args) throws IOException {

        //czytanie pytan przez serwer
        FileReader frQ = new FileReader("bazaPytan.txt");
        BufferedReader brQ = new BufferedReader(frQ);
        ArrayList<String> question = new ArrayList<String>();
        String tempQuestion;

        HashMap<Integer, Integer> whichQuestion = new HashMap<Integer, Integer>();


        HashMap<Integer, Instant> time = new HashMap<Integer, Instant>();

        while((tempQuestion = brQ.readLine()) != null){
            question.add(tempQuestion);
        }

        DatagramSocket server = new DatagramSocket(4999);
while(true) {

    boolean endOfQuestions = false;

        //odebranie
        byte[] buf = new byte[50];
        DatagramPacket packetReceiver = new DatagramPacket(buf, buf.length);
        server.receive(packetReceiver);
        String received = new String(packetReceiver.getData(), 0, packetReceiver.getLength());
        System.out.println(received);

    //zapisywanie odpowiedzi do pliku od klienta
    FileWriter fwA = new FileWriter("bazaOdpowiedzi.txt", true);
    BufferedWriter bwA = new BufferedWriter(fwA);

    Integer tempPort = packetReceiver.getPort();

        int temp = 0;
        if (whichQuestion.containsKey(packetReceiver.getPort())) {
            temp = whichQuestion.get(packetReceiver.getPort());
            DatagramPacket packetSender;
            if(temp < question.size()) {
                packetSender = new DatagramPacket(question.get(temp).getBytes(), question.get(temp).length(), packetReceiver.getAddress(), packetReceiver.getPort());
                server.send(packetSender);

                //rozpoczecie licznika czasowego
                Instant now = Instant.now();
                time.put(tempPort, now);
            }else {
                endOfQuestions = true;
                bwA.append(packetReceiver.getPort() + " - " + received + System.lineSeparator());
                bwA.close();
               String stringPoints = calculatePoints(packetReceiver.getPort()).toString();
                packetSender = new DatagramPacket(stringPoints.getBytes(), stringPoints.length(), packetReceiver.getAddress(), packetReceiver.getPort());
                server.send(packetSender);
                final String alert = "EndOfQuestionsAlert";
                packetSender = new DatagramPacket(alert.getBytes(), alert.length(), packetReceiver.getAddress(), packetReceiver.getPort());
                server.send(packetSender);
            }


            Integer help = whichQuestion.get(packetReceiver.getPort()) + 1;
            whichQuestion.put(tempPort, help);

            if(endOfQuestions == false) {
                bwA.append(packetReceiver.getPort() + " - " + received + System.lineSeparator());
                bwA.close();
            }

        } else {
            whichQuestion.put(tempPort, 1);
            DatagramPacket packetSender = new DatagramPacket(question.get(0).getBytes(), question.get(0).length(), packetReceiver.getAddress(), packetReceiver.getPort());
            server.send(packetSender);

            //rozpoczecie licznika czasowego
            Instant now = Instant.now();
            time.put(tempPort, now);
        }

}
      //  server.close();

    }


    static public Integer calculatePoints(Integer clientPort) throws IOException {
        ArrayList<String> answersList = new ArrayList<String>();
        ArrayList<String> correctAnswersList = new ArrayList<String>();

        FileReader frA = new FileReader("bazaOdpowiedzi.txt");
        BufferedReader brA = new BufferedReader(frA);
        String line = new String();
        String tempAnswer = new String();
        String tempPort = new String();


        FileReader frCA = new FileReader("poprawneOdpowiedzi.txt");
        BufferedReader brCA = new BufferedReader(frCA);
        String correctAnswer = new String();

        while ((line = brA.readLine()) != null) {
            int indexOfDash = line.indexOf('-');
            tempAnswer = line.substring(indexOfDash + 2);

            tempPort = line.substring(0, indexOfDash - 1);
            if (tempPort.equals(clientPort.toString())) {
                answersList.add(tempAnswer);
            }
        }

        while ((correctAnswer = brCA.readLine()) != null) {
            correctAnswersList.add(correctAnswer);
        }

        int i = 0;
        int points = 0;

        //ten FOR jest zle !!!!
        for (String var : correctAnswersList) {
            if (var.equals(answersList.get(i))) {
                points++;
            }
            i++;
        }

        brA.close();
        brCA.close();

    return points;
    }







//   static public void receive(DatagramSocket server){
//        try {
//        //odebranie
//        byte[] buf = new byte[50];
//        DatagramPacket packetReceiver = new DatagramPacket(buf, buf.length);
//        server.receive(packetReceiver);
//        String received = new String(packetReceiver.getData(), 0 , packetReceiver.getLength());
//        System.out.println(received);
//
//        //odeslanie
//        DatagramPacket packetSender = new DatagramPacket( received.getBytes(), received.length(), packetReceiver.getAddress(), packetReceiver.getPort());
//            server.send(packetSender);
//
//            //utworzenie watku
//            ServerUDPThread x = new ServerUDPThread(packetReceiver.getAddress(),packetReceiver.getPort(), server);
//            System.out.println("utowrzono watek dla  ->"+packetReceiver.getAddress()+" "+packetReceiver.getPort()+"<-");
//        } catch (Exception e) {
//           System.err.println(e);
//        }
//    }

}
