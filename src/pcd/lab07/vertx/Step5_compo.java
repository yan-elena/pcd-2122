package pcd.lab07.vertx;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;

// Esempio con CompositeFuture
class TestCompo extends AbstractVerticle {
	
	public void start() {
		FileSystem fs = vertx.fileSystem();    		

		Promise<Buffer> p1 = Promise.promise();
		Promise<Buffer> p2 = Promise.promise();

		Future<Buffer> f1 = p1.future();
		Future<Buffer> f2 = p2.future();

		// qui si passa la promise, in cui diciamo che non devi creare la promise, ma te la do io e quando hai finito
		// mettimi i risultati, questo approccio non è tanto bello perchè passiamo un parametro in più
		fs.readFile("build.gradle", p1);
		
		fs.readFile("settings.gradle", p2);
				
		CompositeFuture
		.all(f1,f2) // solo quando i risultati di entrambi sono pronti allora fai questo
		.onSuccess((CompositeFuture res) -> {
			log("COMPOSITE => \n"+res.result().list());			
		}); 
	}

	private void log(String msg) {
		System.out.println("[REACTIVE AGENT] " + msg);
	}
}

public class Step5_compo {

	public static void main(String[] args) {
		Vertx  vertx = Vertx.vertx();
		vertx.deployVerticle(new TestCompo());
	}
}

