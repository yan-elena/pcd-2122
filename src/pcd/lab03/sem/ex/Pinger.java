package pcd.lab03.sem.ex;

import java.util.concurrent.Semaphore;

public class Pinger extends Thread {

	private Semaphore pingerEvent;
	private Semaphore pongerEvent;

	public Pinger(Semaphore pingerEvent, Semaphore pongerEvent) {
		this.pingerEvent = pingerEvent;
		this.pongerEvent = pongerEvent;
	}	
	
	public void run() {
		while (true) {
			try {
				pingerEvent.acquire();
				System.out.println("ping!");
				pongerEvent.release();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}