import java.io.*;
import java.net.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
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

        // hashmapa przechowująca timeStampy wysłania pytania do klienta
        HashMap<Integer, Instant> time = new HashMap<Integer, Instant>();

        while((tempQuestion = brQ.readLine()) != null){
            question.add(tempQuestion);
        }

        DatagramSocket server = new DatagramSocket(4999);


while(true) {

    boolean endOfQuestions = false;

    //odebranie
    byte[] buf = new byte[300];
    DatagramPacket packetReceiver = new DatagramPacket(buf, buf.length);
    server.receive(packetReceiver);

    String received = new String(packetReceiver.getData(), 0, packetReceiver.getLength());
    System.out.println(received);

    DatagramPacket packetSender = new DatagramPacket(received.getBytes(), received.length(), packetReceiver.getAddress(), packetReceiver.getPort());
    server.send(packetSender);

    if (!(whichQuestion.containsKey(packetReceiver.getPort())))
        whichQuestion.put(packetReceiver.getPort(), 0);
    else {
        //weryfikacja czasu odpowiedzi na pytanie
        boolean onTime = true;
        Integer tempPort = packetReceiver.getPort();

        if (time.get(packetReceiver.getPort()) != null) {
            Instant now = time.get(packetReceiver.getPort());
            System.out.println(now);
            Instant later = Instant.now();
            System.out.println(later);
            Instant now2 = time.get(tempPort).plus(5, ChronoUnit.SECONDS);
            var duration = Duration.between(now, later);
            var MAX_RESPONSE_TIME = Duration.between(now, now2);
            if (duration.compareTo(MAX_RESPONSE_TIME) > 0) {
                onTime = false;
            }
        }


        //zapisywanie odpowiedzi do pliku od klienta
        FileWriter fwA = new FileWriter("bazaOdpowiedzi.txt", true);
        BufferedWriter bwA = new BufferedWriter(fwA);

        //zapisywanie wyników
        FileWriter fwR = new FileWriter("wyniki.txt", true);
        BufferedWriter bwR = new BufferedWriter(fwR);


        int temp = 0;
        // jeśli klient odpowiedział już na pierwsze pytanie
        if (whichQuestion.containsKey(packetReceiver.getPort())) {
            temp = whichQuestion.get(packetReceiver.getPort());
            // jeśli zostały jeszcze jakieś pytania
            if (temp < question.size()) {

                packetSender = new DatagramPacket(question.get(temp).getBytes(), question.get(temp).length(), packetReceiver.getAddress(), packetReceiver.getPort());
                server.send(packetSender);

                //rozpoczecie licznika czasowego
                Instant timeStamp = Instant.now();
                time.put(tempPort, timeStamp);
            }
            // ostatnie pytanie z kolokwium
            else {
                // zapisanie odpowiedzi
                if (!endOfQuestions) {
                    if (onTime) {

                        bwA.append(packetReceiver.getPort() + " - " + received + System.lineSeparator());
                    } else {

                        bwA.append(packetReceiver.getPort() + " - brak odpowiedzi" + System.lineSeparator());
                    }
                }
                bwA.close();
                endOfQuestions = true;

                // wyslanie i obliczenie wyniku
                String stringPoints = "Wynik: " + (calculatePoints(packetReceiver.getPort()).toString());
                bwR.append(packetReceiver.getPort() + " " + stringPoints + System.lineSeparator());
                bwR.close();
                packetSender = new DatagramPacket(stringPoints.getBytes(), stringPoints.length(), packetReceiver.getAddress(), packetReceiver.getPort());
                server.send(packetSender);

            }

            // inkrementacja nastepnego pytania klienta
            Integer help = whichQuestion.get(packetReceiver.getPort()) + 1;
            whichQuestion.put(tempPort, help);

            // zapisanie odpowiedzi
            if (endOfQuestions == false && whichQuestion.get(packetReceiver.getPort()) != 1) {
                if (onTime) {

                    bwA.append(packetReceiver.getPort() + " - " + received + System.lineSeparator());
                } else {

                    bwA.append(packetReceiver.getPort() + " - brak odpowiedzi" + System.lineSeparator());
                }
                bwA.close();
            }

        }
        // jeśli klient jeszcze nie otrzymal pierwszego pytania
        else {

            whichQuestion.put(tempPort, 1);

            packetSender = new DatagramPacket(question.get(0).getBytes(), question.get(0).length(), packetReceiver.getAddress(), packetReceiver.getPort());
            server.send(packetSender);

            //rozpoczecie licznika czasowego
            Instant timeStamp = Instant.now();
            time.put(tempPort, timeStamp);
        }
    }
}
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
}
