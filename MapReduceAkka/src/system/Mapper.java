package system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class Mapper extends UntypedActor{
	ArrayList<ActorRef> reducers;
	public Mapper(ArrayList<ActorRef> reducers){
		this.reducers=reducers;
	}
	@Override
	public void onReceive(Object msg) throws Exception {
		HashMap<Integer,Integer> frequency= new HashMap<Integer,Integer>();
		if(msg instanceof User){
			for(Integer i: ((User)msg).getVisitedPlaces()){
				if (frequency.get(i)==null) {
					frequency.put(i, 1);
				} else {
					frequency.put(i,frequency.get(i)+1);
				}
				
			}
			
			for(Entry<Integer,Integer> entry:frequency.entrySet()){
				reducers.get(entry.getKey()%reducers.size()).tell(entry,null);
			}
		}else if(msg instanceof String){
			if(msg.equals("sentAll")){
				for (ActorRef ref:reducers) {
					ref.tell("sentAll",null);
				}
			}
		}
		
	}

}
