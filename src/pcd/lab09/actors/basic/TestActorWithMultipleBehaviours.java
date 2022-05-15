package pcd.lab09.actors.basic;

import akka.actor.typed.ActorSystem;

public class TestActorWithMultipleBehaviours {
  public static void main(String[] args) throws Exception  {

	  // però se sono nel behaviour A e dichiaro solo di ricevere quel messaggio, se ricevo altri messaggi allora questi
	  // vengono persi, viene fatto vedere un info in cui viene detto che quel messaggio non è stato gestito.
	  // se non vogliamo perdere il messaggio allora vedi esempio ActorWithMultipleBehaviorsAndStashing
	  final ActorSystem<ActorWithMultipleBehaviorsBaseMsg> myActor =
			    ActorSystem.create(ActorWithMultipleBehaviors.create(0), "myActor");

	  /* The next two msgs will be unhandled, because not managed in the initial behaviour */
	  myActor.tell(new ActorWithMultipleBehaviors.MsgOne());  
	  myActor.tell(new ActorWithMultipleBehaviors.MsgTwo());  
	  
	  /* give time to the logging system to setup */
	  Thread.sleep(1000);

	  /* The next msgs will be managed and will cause a transition to new behaviours, up to stopped */
	  myActor.tell(new ActorWithMultipleBehaviors.MsgZero()); 
	  myActor.tell(new ActorWithMultipleBehaviors.MsgOne()); 
	  myActor.tell(new ActorWithMultipleBehaviors.MsgTwo()); 
  
	  /* these msgs will be not delivered since the actor stopped */
	  myActor.tell(new ActorWithMultipleBehaviors.MsgZero()); 
	  myActor.tell(new ActorWithMultipleBehaviors.MsgOne()); 
	  myActor.tell(new ActorWithMultipleBehaviors.MsgTwo()); 

	  System.out.println("done");
  }
}
