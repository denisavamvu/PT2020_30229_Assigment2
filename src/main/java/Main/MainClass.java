package Main;

import Simulator.SimulationManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class MainClass {
    public static void main(String []args){
        int numberOfClients;
        int numberOfQueues;
        int simulationInterval;
        int minArrivingTime;
        int maxArrivingTime;
        int minServiceTime;
        int maxServiceTime;
        //se iau argumentele pentru a se crea fisierele de input si output
        String inputFilename=args[0];
        String outputFilename=args[1];
        String data = new String();
        try {
            File f = new File(inputFilename);
            Scanner s = new Scanner(f);
            while(s.hasNextLine()){
                data = data + " " + s.nextLine();
            }
        }
        catch(FileNotFoundException e){
            System.out.println("error file not found");
            e.printStackTrace();
        }
        String[] values = data.split("\\D+");

        numberOfClients =Integer.parseInt(values[1]);
        numberOfQueues = Integer.parseInt(values[2]);
        simulationInterval= Integer.parseInt(values[3]);
        minArrivingTime= Integer.parseInt(values[4]);
        maxArrivingTime= Integer.parseInt(values[5]);
        minServiceTime= Integer.parseInt(values[6]);
        maxServiceTime= Integer.parseInt(values[7]);


        SimulationManager s=new SimulationManager(numberOfClients, numberOfQueues, simulationInterval, minArrivingTime, maxArrivingTime,
         minServiceTime, maxServiceTime);
        s.setFilename(outputFilename);
        Thread t=new Thread(s);
        t.start();


    }
}
