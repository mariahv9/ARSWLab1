/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    private ArrayList<Integer> Waiting = new ArrayList();
    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];

        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, Waiting);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1, Waiting);
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        Scanner myObj = new Scanner(System.in);
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }
        while (someoneAlive()){
            try {

                sleep(TMILISECONDS);

                Waiting.add(1);
                while (!(allWating())){}

                synchronized (Waiting){
                    for(int i = 0;i < NTHREADS;i++ ) {
                        System.out.println(pft[i].getPrimes().toString());
                    }
                    System.out.print("Presione Enter para continuar....");
                    String Enter = myObj.nextLine();
                    Waiting.clear();
                    Waiting.notify();
                }

            } catch (InterruptedException e) {
                    e.printStackTrace();
            }
        }
    }

    private boolean someoneAlive(){
        boolean dead =false;
        for(int i = 0;i < NTHREADS;i++ ) {
            dead |= pft[i].isAlive();
        }
        return dead;
    }

    private boolean allWating(){
        boolean waiting =true;
        for(int i = 0;i < NTHREADS;i++ ) {
            waiting &= pft[i].getAllWating();
        }
        return waiting;
    }
    
}
