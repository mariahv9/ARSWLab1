/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import edu.eci.arsw.threads.CountFast;

import java.util.ArrayList;

/**
 * @author Maria Hernandez y Alan Marin
 */
public class HostBlackListsValidator {
    public ArrayList<Thread> ranges = new ArrayList<Thread>();
    public int ocurrencesCount = 0;
    private HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();

    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @param parts
     */
    public void checkHost(String ipaddress, int parts) throws InterruptedException {
        int ctd = skds.getRegisteredServersCount() / parts;

        if (parts % 2 == 0) {
            for (int i = 0; i < skds.getRegisteredServersCount(); i += ctd) {
                Thread c = new Thread(new CountFast(i, ctd + i, ipaddress));
                ranges.add(c);
            }
        } else {
            for (int i = 0; i < ctd * parts; i += ctd) {
                Thread c = new Thread( new CountFast(i, ctd+i, ipaddress));
                ranges.add(c);
            }
            Thread c = new Thread( new CountFast(ctd * parts, skds.getRegisteredServersCount(), ipaddress));
            ranges.add(c);
        }

        for (int i = 0; i < ranges.size(); i++) {
            ranges.get(i).start();
        }

        for (int i = 0; i < ranges.size(); i++) {
            ranges.get(i).join();
        }
    }

//        for (int i=0;i<skds.getRegisteredServersCount() && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){

//            checkedListsCount++;
//            if (skds.isInBlackListServer(i, ipaddress)){
//                blackListOcurrences.add(i);
//                ocurrencesCount++;
//            }
//        }

//        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
//            skds.reportAsNotTrustworthy(ipaddress);
//        }
//        else{
//            skds.reportAsTrustworthy(ipaddress);
//        }

//        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});

//        return blackListOcurrences;
//    }
//    }
        //    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());

    public HostBlacklistsDataSourceFacade getServers (){
        return skds;
    }

    public void setOcurrencesCount(int ocurrencesCount) {
        this.ocurrencesCount += ocurrencesCount;
    }

    public int getOcurrencesCount() {
        return ocurrencesCount;
    }
}