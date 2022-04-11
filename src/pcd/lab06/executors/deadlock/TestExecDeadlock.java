package pcd.lab06.executors.deadlock;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * DOMANDA ESAME:
 * per capire la differenza tra il livello logico e
 * se mandiamo in esecuzione con fixedThreadPool con numero di processori + 1
 * ora i task eseguono prima A e poi B
 * se ci chiede di fare un'estensione: dobbiamo pare in modo che per tutti i task possono eseguire la parte B solo se
 * tutti hanno eseguito la parte A, come facciamo?
 * ok allora facciamo una barriera ciclica, e quindi lo inizializza a 100 (come numero di task) e chiama la barriera, ma
 * se lo mettiamo in esecuzione va in deadlock
 * perché?
 * Perché se abbiamo 16 core logici, se i primi 17 thread riescono ad allocare il task e poi fanno il wait sulla barriera,
 * non si blocca il task, ma si blocca il thread esecutore, ma allora non abbiamo più thread per poter eseguire B, ma
 * neanche per finire A
 *
 * come risolvere?
 * o con fork-join
 * oppure con il framework Fork-join pattern
 * ogni task possa essere ulteriormente decomposto in più sottotask e poi ha la possibilità di fare la join, utilizzando
 * il thread dinamico
 * con ForkJoinTask
 */
public class TestExecDeadlock {

	public static void main(String[] args) throws Exception {

		int nTasks = 100; 
		int nThreads = Runtime.getRuntime().availableProcessors() + 1;
		
		ExecutorService exec = Executors.newFixedThreadPool(nThreads);
		CyclicBarrier barrier = new CyclicBarrier(nTasks);
				
		for (int i = 0; i < nTasks; i++) {
			exec.execute(new MyTask("task-" + i, barrier));
		}		
		
		exec.shutdown();
		exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);		
	}
}


