package pcd.lab09.actors.basic;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.StashBuffer;

// si può mettere da parte i messaggi StashBuffer<Tipo del messaggio>
public class ActorWithMultipleBehaviorsAndStashing extends AbstractBehavior<ActorWithMultipleBehaviorsBaseMsg> {

	public static class MsgZero implements ActorWithMultipleBehaviorsBaseMsg {}
	public static class MsgOne implements ActorWithMultipleBehaviorsBaseMsg {}
	public static class MsgTwo implements ActorWithMultipleBehaviorsBaseMsg {}

	private int initialState;
	private StashBuffer<ActorWithMultipleBehaviorsBaseMsg> stashBuffer;
	
	private ActorWithMultipleBehaviorsAndStashing(ActorContext<ActorWithMultipleBehaviorsBaseMsg> context, 
													int initialState,
													StashBuffer<ActorWithMultipleBehaviorsBaseMsg> stash) {
		super(context);
		this.initialState = initialState;
		this.stashBuffer = stash;
	}
	

	public static Behavior<ActorWithMultipleBehaviorsBaseMsg> create(int initialState) {
		// lo stashBuffer ce lo passa quando ha gestito il behaviour, lo stash viene creato lui
		return Behaviors.withStash(100,
				stash ->  Behaviors.setup(
						context -> new ActorWithMultipleBehaviorsAndStashing(context, initialState, stash)));
	}

	@Override
	public Receive<ActorWithMultipleBehaviorsBaseMsg> createReceive() {
		return newReceiveBuilder()
				// voglio gestire message 0
				.onMessage(MsgZero.class,this::onMsgZero)
				// ma per altri messaggi (anyMsg, metodo handler) mettere nello stashBuffer
				.onMessage(ActorWithMultipleBehaviorsBaseMsg.class,this::onAnyMsg)
				.build();
	}

	
	private Behavior<ActorWithMultipleBehaviorsBaseMsg> onMsgZero(MsgZero msg) {
		this.getContext().getLog().info("msgZero - state: " + initialState);
		// quando andiamo a transitare un nuovo behaviour e sappiamo che proveniamo da un behaviour con i suoi vecchi
		// messaggi gli diciamo di ripristinare tutti i messaggi che sono stati accumulati
		return stashBuffer.unstashAll(
					Behaviors.setup(context -> new BehaviourA(context, initialState + 1))
			   );
	}

	private Behavior<ActorWithMultipleBehaviorsBaseMsg> onAnyMsg(ActorWithMultipleBehaviorsBaseMsg msg) {
		this.getContext().getLog().info("stashing msg - state " + initialState);
		// metti il messaggio nello stash
		stashBuffer.stash(msg);
		return this;
	}
	
	/* behaviour A */
	
	class BehaviourA extends AbstractBehavior<ActorWithMultipleBehaviorsBaseMsg> {

		private int localState;
		
		private BehaviourA(ActorContext<ActorWithMultipleBehaviorsBaseMsg> context, int localState) {
			super(context);
			this.localState = localState;
		}

		@Override
		public Receive<ActorWithMultipleBehaviorsBaseMsg> createReceive() {
			return newReceiveBuilder()
					.onMessage(MsgOne.class, this::onMsgOne)
					.build();
		}	

		private Behavior<ActorWithMultipleBehaviorsBaseMsg> onMsgOne(MsgOne msg) {
			this.getContext().getLog().info("msgOne - state: " + localState);		
			return Behaviors.setup(context -> new BehaviourB(context, localState + 1));
		}
	}

	/* behaviour B */
	
	class BehaviourB extends AbstractBehavior<ActorWithMultipleBehaviorsBaseMsg> {

		private int localState;

		private BehaviourB(ActorContext<ActorWithMultipleBehaviorsBaseMsg> context, int localState) {
			super(context);
			this.localState = localState;
		}

		@Override
		public Receive<ActorWithMultipleBehaviorsBaseMsg> createReceive() {
			return newReceiveBuilder()
					.onMessage(MsgTwo.class, this::onMsgTwo)
					.build();
		}	

		private Behavior<ActorWithMultipleBehaviorsBaseMsg> onMsgTwo(MsgTwo msg) {
			this.getContext().getLog().info("msgTwo - state: " + localState);		
			return Behaviors.stopped();
		}
	}
	
	
}
