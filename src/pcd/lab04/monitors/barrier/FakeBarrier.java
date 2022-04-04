package pcd.lab04.monitors.barrier;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Barrier - to be implemented
 */
public class FakeBarrier implements Barrier {

	private int nParticipants;
	private int nHits;
//	private Lock mutex;
//	private Condition allHit;
	
	public FakeBarrier(int nParticipants) {
		this.nParticipants = nParticipants;
//		mutex = new ReentrantLock();
//		allHit = mutex.newCondition();
		nHits = 0;
	}
	
	@Override
	public synchronized void hitAndWaitAll() throws InterruptedException {
		nHits++;
		if (nParticipants == nHits) {
			notifyAll();
		} else {
			while (nHits < nParticipants) {
				wait();
			}
		}
//		try {
//			mutex.lock();
//			nHits++;
//			if (!isAllHit()) {
//				allHit.await();
//			}
//			if (isAllHit()) {
//				allHit.signalAll();
//			}
//		} finally {
//			mutex.unlock();
//		}
	}

//	private boolean isAllHit() {
//		return nParticipants == nHits;
//	}
	
}
