package pcd.lab07.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;

public class Step1_basic {

	public static void main(String[] args) {

		//factory per ottenere il riferimento all'event loop
		Vertx  vertx = Vertx.vertx();

		// permettere di accedere al fileSystem in modo asincrono
		FileSystem fs = vertx.fileSystem();    		

		log("started");
		
		/* version 4.0 - future (promise) based API */

		// qui si usa le promise, che sono introdotte dal 4.0, prima c'era la versione con il callback
		// le promise in vert.x vengono restituite come le Future, quindi creiamo le promise chiamate in modo asincrono,
		// poi al client che vuole reagire con il risultato, gli diamo una future. La future non ci da la possibilità di
		// agire, modificare sul risultato, mentre la promise sì
		Future<Buffer> fut = fs.readFile("build.gradle");
		fut.onComplete((AsyncResult<Buffer> res) -> {
			// questo viene fatto dopo la stampa di done e con un thread di vert.x
			log("BUILD \n" + res.result().toString().substring(0,160));
		});

		/* old versions - callback style, still supported */
		
		fs.readFile("settings.gradle", (AsyncResult<Buffer> res) -> {
			log("SETTINGS \n" + res.result().toString().substring(0,160));
		});

		/*
		try {
			// ma se io aspetto 1 secondo, la stampa done viene fatto alla fine, quidni in questo caso ci possono essere
			// delle corse critiche, qui c'è il non determinismo
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 */

		// questa stampa viene fatto prima
		log("done");
	}
	
	private static void log(String msg) {
		System.out.println("" + Thread.currentThread() + " " + msg);
	}
}

