package pcd.lab07.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

class TestExecBlocking extends AbstractVerticle {

	// private int x = 0;

	public void start() {
		log("before");

		// x++;

		// la lambda passata al executeBlocking non è eseguita all'event loop, ma viene delegata a un altro thread e la
		// lambda ci passa come argomento la promise per specificare quando è completata la computazione
		// executeBlocking utilizza il pool di thread di vert.x
		Future<Integer> res = this.getVertx().executeBlocking(promise -> {
			// Call some blocking API that takes a significant amount of time to return
			log("blocking computation started");
			try {
				// possiamo modificare la variabile x anche in un blocco non dell'event loop
				// x++;
				Thread.sleep(5000);
				
				/* notify promise completion */
				promise.complete(100);
			} catch (Exception ex) {
				
				/* notify failure */
				promise.fail("exception");
			}
		});

		// questo può creare corse critiche a quello prima che viene eseguito in un altro thread, questa cosa non è da fare
		// x++;

		log("after triggering a blocking computation...");

		res.onComplete((AsyncResult<Integer> r) -> {
			log("result: " + r.result());
		});
	}

	private void log(String msg) {
		System.out.println("[REACTIVE AGENT] " + msg);
	}
}

public class Step6_withblocking {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new TestExecBlocking());
	}
}
