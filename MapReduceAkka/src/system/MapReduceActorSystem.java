package system;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class MapReduceActorSystem {
	ActorSystem system;
	ActorRef master;
	public MapReduceActorSystem(ArrayList<User>users){
		system = ActorSystem.create("system");
		master = system.actorOf(Props.create(Master.class,4,users), "master");
		
	}
	public void start(){
		master.tell("start", null);
	}
	
	
}
