package edu.eci.arsw.threads;
import edu.eci.arsw.blacklistvalidator.HostBlackListsValidator;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

import java.util.LinkedList;

/**
 * @author Maria Hernandez y Alan Marin
 */
public class CountFast implements Runnable{
    private int count = 0, checked=0;
    private int rangeA;
    private int rangeB;
    private String ipaddress;
    private HostBlacklistsDataSourceFacade servers;
    private  Thread t;
    private LinkedList<Integer> blackListOcurrences = new LinkedList<>();;


    public CountFast(int rangeA, int rangeB, String ipaddress,HostBlacklistsDataSourceFacade servers ) {
        this.rangeA = rangeA;
        this.rangeB = rangeB;
        this.ipaddress = ipaddress;
        this.servers = servers;
        t = new Thread(this,Integer.toString(rangeA));
        t.start();
    }

    @Override
    public void run () {
        for (int rangeA = this.rangeA; rangeA < rangeB; rangeA++){
            if(count>=5){break;}
            checked++;
            if (servers.isInBlackListServer(rangeA, ipaddress)){
                blackListOcurrences.add(rangeA);
                count ++;
            }
        }
    }

    public Boolean isDead(){
        return !(t.isAlive());
    }

    public void join(){
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getChecked() {
        return checked;
    }

    public LinkedList<Integer> getBlackListOcurrences() {
        return blackListOcurrences;
    }
}