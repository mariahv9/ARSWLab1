package edu.eci.arsw.threads;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;

public class ThreadValidator extends Thread {
    public static int ocurrencesCount = 0;
    public static int checkedListsCount = 0;

    public ThreadValidator() {

        LinkedList<Integer> blackListOcurrences = new LinkedList<>();
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();

        for (int i = 0; i < skds.getRegisteredServersCount() && ocurrencesCount < 5; i++) {
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipaddress)) {
                blackListOcurrences.add(i);
                ocurrencesCount++;
            }
            if (countOcurrencies()) {
                break;
            }
        }
    }

    public boolean countOcurrencies (){
        if (ocurrencesCount >= 5){
            return true;
        }
        else{
            return false;
        }
    }
}
