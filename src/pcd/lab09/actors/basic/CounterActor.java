package pcd.lab09.actors.basic;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

// tutto è attore, tutto diventa concorrente, attore, non abbiamo più componenti attivi e passivi
                           // estende dalla classe che rappresenta il comportamento del contatore, abbiamo un'idea di
                           // tipo di dati. CounterMsg è un'interfaccia base che definisce il tipo di messaggio che può
                           // ricevere intendendo che sono due tipi di messaggi IncMsg e altri (vedi sotto classi statiche)
public class CounterActor extends AbstractBehavior<CounterMsg> {

	private int count;
	
	/* constructor called indirectly */

	// costruttore privato
	private CounterActor(ActorContext<CounterMsg> context) {
		super(context);
		count = 0;
	}

	// è il punto per dichiarare quali sono i messaggi che deve ricevere, come questo behaviour
	// riceve questi messaggi
	@Override
	public Receive<CounterMsg> createReceive() {
		return newReceiveBuilder()
				.onMessage(IncMsg.class, this::onIncMsg) // questo messaggio è associato a questo handler
				.onMessage(GetValueMsg.class, this::onGetValueMsg)
				.build();
		
	}

	private Behavior<CounterMsg> onIncMsg(IncMsg msg) {
		this.getContext().getLog().info("inc");
		count++;
		return this;
	}

	private Behavior<CounterMsg> onGetValueMsg(GetValueMsg msg) {
		this.getContext().getLog().info("getValue");
		// replyTo, li trovo a chi devo inviare
		// anche tell è tipato rispetto al tipo di messaggio che abbiamo specificato, quindi passo quel tipo di messaggio
		msg.replyTo.tell(new CounterValueMsg(count));
		return this;
	}

	/* messages */
	
	static public class IncMsg implements CounterMsg {}
	
	static public class GetValueMsg implements CounterMsg {
		// con <CounterUserMsg> sto esprimendo che tutti i messaggi che ricevo sono di questo tipo, e quindi ci costringe
		// un'analisi di tutti i tipi ---> in Akka si può passare agli Adapter
		public final ActorRef<CounterUserMsg> replyTo;
		public GetValueMsg(ActorRef<CounterUserMsg> replyTo) {
			this.replyTo = replyTo;
		}
	}

	static public class CounterValueMsg implements CounterUserMsg {
		public final int value;
		public CounterValueMsg(int value) {
			this.value = value;
		}
	}

	
	/* public factory to create the actor */

	public static Behavior<CounterMsg> create() {
		// setup crea il comportamento dell'attore
		return Behaviors.setup(CounterActor::new);
	}
}
