package pcd.lab09.actors.basic;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

// API Vecchia, c'era ancora l'astrazione di attore, vedi ViewActor
public class ActorsWithGUIMain {
  public static void main(String[] args) throws Exception  {
    ActorSystem system = ActorSystem.create("MySystem");
    
    ActorRef act = system.actorOf(Props.create(ViewActor.class));
	new ViewFrame(act).display();
  }
}
