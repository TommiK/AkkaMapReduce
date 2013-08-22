package system;

import java.util.HashMap;
import java.util.Map.Entry;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
public class Reducer extends UntypedActor  {
	HashMap<Integer,Integer>frequency= new HashMap<Integer,Integer>();
	private int numberOfMapper;
	private int receivedsentAll=0;
	public Reducer(int numberOfMapper){
		this.numberOfMapper=numberOfMapper;
	}
	
	@Override
	public void onReceive(Object msg) throws Exception {
		
		if(msg instanceof Entry){
			Entry entry=(Entry)msg;
			if(frequency.get(entry.getKey())==null){
				frequency.put((Integer) entry.getKey(),(Integer)entry.getValue());
			}else{
				frequency.put((Integer) entry.getKey(), frequency.get(entry.getKey())+(Integer)entry.getValue());
			}
			
		}else if (msg instanceof String) {
			if(msg.equals("sentAll")){
				receivedsentAll++;
				if (receivedsentAll == numberOfMapper) {
					context().parent().tell(frequency,null);
				}

			}
		}
		
		
		
	}

}
