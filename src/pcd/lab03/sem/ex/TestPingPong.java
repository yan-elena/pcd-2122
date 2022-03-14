package pcd.lab03.sem.ex;

import java.util.concurrent.Semaphore;

/**
 * Unsynchronized version
 * 
 * @TODO make it sync 
 * @author aricci
 *
 */
public class TestPingPong {
	public static void main(String[] args) {
		Semaphore pingerEvent = new Semaphore(0);
		Semaphore pongerEvent = new Semaphore(0);
		new Pinger(pingerEvent, pongerEvent).start();
		new Ponger(pingerEvent, pongerEvent).start();

		pingerEvent.release();
	}

}
