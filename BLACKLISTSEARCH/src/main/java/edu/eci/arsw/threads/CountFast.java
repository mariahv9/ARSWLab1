package edu.eci.arsw.threads;
import edu.eci.arsw.blacklistvalidator.HostBlackListsValidator;
import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import sun.jvm.hotspot.runtime.Thread;

import java.util.LinkedList;

/**
 * @author Maria Hernandez y Alan Marin
 */
public class CountFast extends HostBlackListsValidator implements Runnable{
    private int count = 0;
    private int rangeA = 0;
    private int rangeB = 0;
    private String ipaddress;
    private Thread t;
    public HostBlacklistsDataSourceFacade servers = HostBlacklistsDataSourceFacade.getInstance();

    public CountFast(int rangeA, int rangeB, String ipaddress) {
        this.rangeA = rangeA;
        this.rangeB = rangeB;
        this.ipaddress = ipaddress;
        this.t = new Thread(this,Integer.toString(rangeA));
    }

    @Override
    public void run () {
        LinkedList<Integer> blackListOcurrences = new LinkedList<>();

        for (int rangeA = this.rangeA; rangeA < rangeB; rangeA++){
            if (servers.isInBlackListServer(rangeA, ipaddress)){
                blackListOcurrences.add(rangeA);
                count ++;
                super.setOcurrencesCount(count);
            }
            if (super.getOcurrencesCount() >= 5){
                break;
            }
        }
    }
}