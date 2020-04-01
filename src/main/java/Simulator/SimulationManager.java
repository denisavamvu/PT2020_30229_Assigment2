package Simulator;

import Simulator.Scheduler;
import Task.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.*;

import static java.lang.Thread.sleep;

public class SimulationManager implements Runnable {
    private String filename;
    private ArrayList<Client> clients;
    private int simulationInterval;
    private int minArrivingTime;
    private int maxArrivingTime;
    private int minServiceTime;
    private int maxServiceTime;
    private boolean toRun=true;
    private int simulationTime=0;
    private Scheduler scheduler;
    public SimulationManager(int numberOfClients,int numberOfQueues,int simulationInterval,int minArrivingTime,int maxArrivingTime,
                     int minServiceTime,int maxServiceTime){
        scheduler = new Scheduler(numberOfClients,numberOfQueues);
        this.simulationInterval=simulationInterval;
        this.minArrivingTime=minArrivingTime;
        this.maxArrivingTime=maxArrivingTime;
        this.minServiceTime=minServiceTime;
        this.maxServiceTime=maxServiceTime;

        clients=new ArrayList<Client>();
        for(int i=1;i<=numberOfClients;i++)
            clients.add(randomClientGenerator(i));
    }
    public void setFilename(String filename){
        this.filename=filename;
    }
    public Client randomClientGenerator(int ID){
        Client client=new Client();
        client.setID(ID);
        Random random = new Random();
        int n;
        n=random.nextInt(maxArrivingTime-minArrivingTime+1);
        n+=minArrivingTime;
        client.setArrivalTime(n);

        n=random.nextInt(maxServiceTime-minServiceTime+1);
        n+=minServiceTime;
        client.setServiceTime(n);
        return client;
    }
    public synchronized void run() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filename, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        int i=1;
        float waitingTime=0;
        for(Server server: scheduler.getServers()) {
            server.setName("Queue "+i);
            i++;
            server.start();
        }
        while(toRun){
            Iterator<Client> c=clients.iterator();
            while(c.hasNext()){
                Server s=this.scheduler.getMinWaitingTimePeriod();
                Client a=c.next();
                if(a.getArrivalTime()<=simulationTime) {
                    s.addClient(a);
                    c.remove();
                }
            }

            String s="";
            writer.println("time: "+simulationTime);
            writer.println("Waiting clients:");
            for(Client client: clients)
                s+=client.toString()+",";
            writer.println(s);
            for(Server server: scheduler.getServers())
                writer.println(server.toString());
            writer.println();
            waitingTime+=this.scheduler.getCurrentWaitingTime();
            simulationTime++;
            for(Server server: scheduler.getServers()) {
                server.setTimePassed(true);
                server.interrupt();
            }

            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(scheduler.noMoreClients()&&clients.isEmpty())
                break;

            if(simulationTime==simulationInterval)
                toRun=false;
        }
        for(Server server: scheduler.getServers()) {
            server.setToRun(false);
            server.interrupt();
        }
        writer.println("Average wainting time: "+waitingTime/scheduler.getNumberOfClients());
        writer.close();
    }

}
