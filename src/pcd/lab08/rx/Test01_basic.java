package pcd.lab08.rx;

import java.util.Arrays;

import io.reactivex.rxjava3.core.*;

public class Test01_basic {

	public static void main(String[] args){
				
		log("creating with just.");

		// Flowable supporta le funzionalità più articolate come per back
		// just crea uno stream fatto di uno solo valore
		// sottoscrivere, dico per ogni elemento cosa voglio fare
	    Flowable.just("Hello world").subscribe(s -> {	    		
	    		log(s);    		
	    });
	    
	    // with inline method
	    
	    Flowable.just("Hello world")
	    	.subscribe(System.out::println);
	    
		// creating a flow (an observable stream) from a static collection
		
	    // simple subscription 
	    
		String[] words = { "Hello", " ", "World", "!" }; 

		// posso creare uno stream da un'array
		Flowable.fromArray(words)
				// ogni elemento viene processato
			.subscribe((String s) -> {
				log(s);
			});
		
		// full subscription: onNext(), onError(), onCompleted()
		
		log("Full subscription...");
		
		Observable.fromArray(words)
			.subscribe((String s) -> {
				log("> " + s);
			},(Throwable t) -> {
				log("error  " + t);
			},() -> {
				log("completed");
			});
		
		// operators

		log("simple application of operators");

		// genera un flusso di range da 1 a 20
		Flowable<Integer> flow = Flowable.range(1, 20)
				// poi voglio un nuovo flusso che contiene tutti i valori al quadrato
			.map(v -> v * v)
				// e voglio quelli divisibili per 3
			.filter(v -> v % 3 == 0);
		
		log("first subscription #1");
		flow.subscribe(System.out::println);

		log("first subscription #2");
		flow.subscribe((v) -> {
			log("" + v);
		});

		// doOnNext for debugging...
		
		log("showing the flow...");
		
		Flowable.range(1, 20)
				// ad ogni step faccio un log di quello che sto facendo
			.doOnNext(v -> log("1> " + v))
			.map(v -> v * v)
			.doOnNext(v -> log("2> " + v))
			.filter(v -> v % 3 == 0)
			.doOnNext(v -> log("3> " + v))
			.subscribe(System.out::println);
						
		
		// simple composition
		
		log("simple composition");
		
		Observable<String> src1 = Observable.fromIterable(Arrays.asList(
				 "the",
				 "quick",
				 "brown",
				 "fox",
				 "jumped",
				 "over",
				 "the",
				 "lazy",
				 "dog"
				));

		Observable<Integer> src2 = Observable.range(1, 5);
		
		src1
			.zipWith(src2, (string, count) -> String.format("%2d. %s", count, string))
			.subscribe(System.out::println);
		
	}
	
	private static void log(String msg) {
		System.out.println("[" + Thread.currentThread().getName() + "] " + msg);
	}
}
