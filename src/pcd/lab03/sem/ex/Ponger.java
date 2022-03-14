package pcd.lab03.sem.ex;

import java.util.concurrent.Semaphore;

public class Ponger extends Thread {

	private Semaphore pingerEvent;
	private Semaphore pongerEvent;
	
	public Ponger(Semaphore pingerEvent, Semaphore pongerEvent) {
		this.pingerEvent = pingerEvent;
		this.pongerEvent = pongerEvent;
	}	
	
	public void run() {
		while (true) {
			try {
				pongerEvent.acquire();
				System.out.println("pong!");
				pingerEvent.release();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}