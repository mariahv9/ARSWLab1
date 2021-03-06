package edu.eci.arsw.primefinder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{

	
	int a,b;
	
	private List<Integer> primes;

	private Boolean allWating=false;
	private ArrayList<Integer> Waiting;
	
	public PrimeFinderThread(int a, int b, ArrayList<Integer> waiting) {
		super();
                this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;


		this.Waiting=waiting;

	}

        @Override
	public void run(){
            for (int i= a;i < b;i++){						
                if (isPrime(i)){
                    primes.add(i);
                }
                synchronized (Waiting){
                	if(Waiting.size()==1){
                		allWating = true;
						try {
							Waiting.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
                allWating = false;
            }
            allWating = true;
	}
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}

	public Boolean getAllWating() {
		return allWating;
	}
}
