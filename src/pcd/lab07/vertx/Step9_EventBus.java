package pcd.lab07.vertx;

import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;

// modo per far comunicare due event loop, in questo modo comunicano attraverso la generazione e il consumo di eventi
class MyAgent1 extends AbstractVerticle {

	// possiamo anche specificare una promise nel metodo start in cui possiamo sapere quando il componente è stato completato
	// quindi quando ci interessa sincronizzarsi di sapere che api è stato iniziato, possiamo mettere la promise
	 public void start(Promise<Void> startPromise) {
		log("started.");
		EventBus eb = this.getVertx().eventBus();
		// una volta che abbiamo un eventBus possiamo dire che siamo interessati ad agire ogni volta che nel topic è
		 // arrivato un argomento
		eb.consumer("my-topic", message -> {
			log("new message: " + message.body());
		});		
		log("Ready.");
		startPromise.complete();
	}

	private void log(String msg) {
		System.out.println("[REACTIVE AGENT #1] " + msg);
	}
}

class MyAgent2 extends AbstractVerticle {
	
	public void start() {
		log("started.");
		EventBus eb = this.getVertx().eventBus();
		// qui fa la post, mette sul canale la stringa test
		eb.publish("my-topic", "test");
	}

	private void log(String msg) {
		System.out.println("[REACTIVE AGENT #2] " + msg);
	}
}

public class Step9_EventBus {

	public static void main(String[] args) {
		Vertx  vertx = Vertx.vertx();
		vertx.deployVerticle(new MyAgent1(), res -> {
			/* deploy the second verticle only when the first has completed */
			vertx.deployVerticle(new MyAgent2());
		});
	}
}

