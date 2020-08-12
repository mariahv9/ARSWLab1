/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

//import com.sun.jdi.request.ThreadDeathRequest;
//import sun.jvm.hotspot.utilities.soql.JSJavaTypeArray;

/**
 *
 * @author Maria Hernandez y Alan Marin
 */
public class CountThreadsMain {
    private Thread thread1, thread2, thread3;

    public CountThreadsMain(){
        thread1 = new Thread(new CountThread(0, 99));
        thread2 = new Thread(new CountThread(99, 199));
        thread3 = new Thread(new CountThread(199, 299));
    }

    public void run(){
        thread1.run();
        thread2.run();
        thread3.run();
    }

    public void start(){
        thread1.start();
        thread2.start();
        thread3.start();
    }

    public static void main(String a[]){
        CountThreadsMain c = new CountThreadsMain();
        c.start();
    }
}