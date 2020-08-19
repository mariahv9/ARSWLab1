/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import edu.eci.arsw.threads.CountFast;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Maria Hernandez y Alan Marin
 */
public class HostBlackListsValidator {
    private static final int BLACK_LIST_ALARM_COUNT=5;
    private int checkedListsCount = 0;
    private int ocurrencesCount = 0;

    private ArrayList<CountFast> threadsCountFast = new ArrayList<>();

    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @param parts number of threads to make a partition.
     */
    public LinkedList<Integer> checkHost(String ipaddress, int parts) {
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();

        int ctd = skds.getRegisteredServersCount() / parts;

        if (parts % 2 == 0) {
            for (int i = 0; i < skds.getRegisteredServersCount(); i += ctd) {
                CountFast c = new CountFast(i, ctd + i, ipaddress, skds);
//                c.join();
                threadsCountFast.add(c);
            }
        } else {
            for (int i = 0; i < ctd * parts; i += ctd) {
                CountFast c = new CountFast(i, ctd + i, ipaddress, skds);
//                c.join();
                threadsCountFast.add(c);
            }
            CountFast c = new CountFast(ctd * parts, skds.getRegisteredServersCount(), ipaddress, skds);
//            c.join();
            threadsCountFast.add(c);
            
        }


        LinkedList<Integer> serversBlack =isInBlackLIst();

        if (ocurrencesCount >= BLACK_LIST_ALARM_COUNT) {
            skds.reportAsNotTrustworthy(ipaddress);
        } else {
            skds.reportAsTrustworthy(ipaddress);
        }

        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});

        return serversBlack;

    }


    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());

    
    public LinkedList<Integer> isInBlackLIst(){
        LinkedList<Integer> blackListOcurrences = new LinkedList<>();
        int deadthreads =0;
        while (ocurrencesCount<BLACK_LIST_ALARM_COUNT && deadthreads<threadsCountFast.size()){
            ocurrencesCount = 0;
            checkedListsCount =0;
            deadthreads = 0;

            for (CountFast c: threadsCountFast){
                if (c.isDead()){
                    deadthreads++;
                }
                checkedListsCount+= c.getChecked();
                ocurrencesCount+= c.getCount();
            }
        }

        if(deadthreads<threadsCountFast.size()){
            for (CountFast c: threadsCountFast){
                blackListOcurrences.addAll(c.getBlackListOcurrences());
                c.setCount(BLACK_LIST_ALARM_COUNT);
            }
        }
        return blackListOcurrences;

    }
}