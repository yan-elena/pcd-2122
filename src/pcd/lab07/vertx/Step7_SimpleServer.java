package pcd.lab07.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

// esempio di un web server
class SimpleServer extends AbstractVerticle {

	private int numRequests;
	private int port;

	public SimpleServer(int port) {
		numRequests = 0;
		this.port = port;
	}
	
	public void start() {
		// qui non sto creando un nuovo event loop, ma stiamo usando l'event loop corretne di AbstractVerticle e andiamo
		// ad registrare un handler che permette di gestire la richiesta
		vertx.createHttpServer().requestHandler(req -> {
			numRequests++;

			String fileName = req.path().substring(1);
			log("request " + numRequests + " arrived for file: " + fileName);

			// chiedo la lettura del file con nome specificato nel path
			this.getVertx().fileSystem().readFile(fileName)
			.onComplete(result -> {
				log("result ready");
				if (result.succeeded()) {
					log(result.result().toString());
					req.response().putHeader("content-type", "text/plain").end(result.result().toString());
				} else {
					log("Oh oh ..." + result.cause());
					// mandiamo la risposta
					req.response().putHeader("content-type", "text/plain").end("File not found");
				}
			});
			
		}).listen(port);
	}

	private  void log(String msg) {
		System.out.println("" + Thread.currentThread() + " " + msg);
	}

}

public class Step7_SimpleServer {
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		// il server viene istanziato su 8081 e permette di leggere un file nella cartella root del progetto
		// es. http://localhost:8081/build.gradle
		SimpleServer myVerticle = new SimpleServer(8081);
		vertx.deployVerticle(myVerticle);
	}
}
