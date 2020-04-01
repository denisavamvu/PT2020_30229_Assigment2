package Simulator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.*;
import Task.*;
public class Scheduler {
    private ArrayList<Server> servers;
    private int numberOfClients;
    private int numberOfQueues;

    public Scheduler(int numberOfClients, int numberOfQueues){
        servers = new ArrayList<Server>();
        for(int i=0;i<numberOfQueues;i++)
            servers.add(new Server());
        this.numberOfClients=numberOfClients;
        this.numberOfQueues=numberOfQueues;
    }

    public void setServers(ArrayList<Server> servers) {
        this.servers = servers;
    }

    public void setNumberOfClients(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public void setNumberOfQueues(int numberOfQueues) {
        this.numberOfQueues = numberOfQueues;
    }

    public ArrayList<Server> getServers() {
        return servers;
    }

    public int getNumberOfClients() {
        return numberOfClients;
    }

    public int getNumberOfQueues() {
        return numberOfQueues;
    }


    public boolean noMoreClients(){
        for(Server server: servers)
            if(server.hasClients())
                return false;
        return true;
    }
    public Server getMinWaitingTimePeriod(){
        int minTime=999999999;
        for(Server server: servers)
            if(server.getWaitingPeriod()<minTime)
                minTime=server.getWaitingPeriod();

        Server s=new Server();
        for(Server server: servers)
            if(server.getWaitingPeriod()==minTime)
            {
                s= server;
                break;
            }
        return s;
    }
    public int getCurrentWaitingTime(){
        int current=0;
        for(Server server: servers)
            current+=server.getCurrtentWaitingTime();
        return current;
    }


}
