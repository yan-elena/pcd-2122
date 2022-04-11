package pcd.lab06.executors.quad2_withsynch;

public class QuadratureResult {
	
	private double sum; 
	private int nTotalResultsToWait;
	private int nResultsArrived;
	
	public QuadratureResult(int nResults){
		nTotalResultsToWait = nResults;
		nResultsArrived = 0;
	}
	
	public synchronized void add(double value){
		sum += value;
		nResultsArrived++;
		if (nResultsArrived >= nTotalResultsToWait){
			notifyAll();
		}
	}

	// in questo caso getResult permette di sincronizzarsi sulla disponibilità del risultato
	public synchronized double getResult() throws InterruptedException {
		while (nResultsArrived < nTotalResultsToWait){
			wait();
		}
		return sum;
	}
}
