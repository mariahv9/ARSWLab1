/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author Maria Hernandez y Alan Marin
 */
public class CountThread implements Runnable {
    private int A, B;

    public CountThread (int A, int B) {
        this.A = A;
        this.B = B;
    }

    @Override
    public void run() {
        for (int i = A; i < B; i++) System.out.println(i);
    }
}