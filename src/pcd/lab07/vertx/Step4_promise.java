package pcd.lab07.vertx;

import java.util.Random;
import io.vertx.core.*;


class TestPromise extends AbstractVerticle {
	
	public void start() {
		log("pre");		

		// creazione di una promise con una factory in cui chiedo di creare una promise specificando il risultato che ci
		// aspettiamo
		Promise<Double> promise = Promise.promise();
		
		this.getVertx().setTimer(1000, res -> {
			log("timeout from the timer...");
			Random rand = new Random();
			double value = rand.nextDouble();
			if (value > 0.5) {
				log("...complete with success.");
				// poi qui andiamo a utilizzare la promise per specificare un completamento, la promise ha successo e ti
				// passo il risultato
				promise.complete(value);
			} else {
				log("...complete with failure.");
				// situazione di fallimento
				promise.fail("Value below 0.5 " + value);
			}
		});

		// lato utilizzatore

		// quando abbiamo una promise, possiamo creare una future corrispondente alla promise
		Future<Double> fut = promise.future();

		// qui le stampe sono fatte da sempre uno stesso thread, ma non sempre è così, perchè vert.x ha un pool di thread
		// e alcune volte potrebbe cambiare il thread per motivi di ottimizzazione
		fut
		.onSuccess((Double res) -> {
			// chiamata solo nel caso di successo e contiene il risultato
			log("reacting to timeout - success: " + res);
		})
		.onFailure((Throwable t) -> {
			log("reacting to timeout - failure: " + t.getMessage());
		})
		.onComplete((AsyncResult<Double> res) -> {
			// chiamata in tutte e due i casi
			log("reacting to completion - " + res.succeeded());
		});
		
		log("post");
	}

	private void log(String msg) {
		System.out.println("[REACTIVE AGENT " + Thread.currentThread() + "]" + msg);
	}
}

public class Step4_promise {
	public static void main(String[] args) {
		
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new TestPromise());
	}
}

