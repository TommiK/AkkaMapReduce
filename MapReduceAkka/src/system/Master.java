package system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.Broadcast;
import akka.routing.RoundRobinRouter;
import akka.routing.SmallestMailboxRouter;

/*
========Problem=========

There is a list of users.
Each User has a sequence/list of places he has visited in the past 
A place in the Sequence is represented by just an id(int). So one place can occur several times in the sequence.

Now I want to determine how often each place has been visited.
The main requirement is to exploit parallelism. 

=======My Solution=========

A Master Actors gets the list of users and he dispatches the users by sending them to a router.
The routees build a hashmap for each received user (place:numberOfVisits) and send each entry 
of the map to a reducer (decision by modulo <numberOfReducer>) which sums up all entrys he receives.
When the master sent all Users, it sends a "finish" which is read by the mapper and forwarded to the reducer.
 When the reducers finally got from each Mapper the "finish"-> they send their maps back to the Master which sums up all Maps.

1) List of Users -------> Master ------->mapperRouter====
======>all routees: - createMap; -----> send Each entry to reducer -----> sum up entries
2) Master ---"finish"--->mapperRouter---->routees
3)routees =====all Messages "finish"====> reducer -----maps----> master ----> fertig


 */

public class Master extends UntypedActor {
	boolean printResult = false;
	private ActorRef mapperRouter;
	private ArrayList<ActorRef> reducers;
	private ArrayList<User> users;
	private HashMap<Integer, Integer> frequency = new HashMap<Integer, Integer>();
	private int receivedResults = 0;
	private int numberOfMapper;
	private long start;

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	// number of mappers = number of reducer
	public Master(int numberOfMapper, ArrayList<User> users) {
		this.users = users;
		this.numberOfMapper = numberOfMapper;
		reducers = new ArrayList<ActorRef>();
		for (int i = 0; i < numberOfMapper; i++) {
			reducers.add(getContext().actorOf(
					Props.create(Reducer.class, numberOfMapper)));
		}
		mapperRouter = getContext().actorOf(
				Props.create(Mapper.class, reducers).withRouter(
						new SmallestMailboxRouter(numberOfMapper)));

	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof String) {
			String message = (String) msg;
			if (msg.equals("start")) {
				start = System.currentTimeMillis();
				for (User u : users) {
					mapperRouter.tell(u, null);

				}
				mapperRouter.tell(new Broadcast("sentAll"), null);
			}
		} else if (msg instanceof HashMap) {
			receivedResults++;
			HashMap map = (HashMap) msg;
			frequency.putAll(map);
			if (receivedResults == numberOfMapper) {
				System.out.println("time needed:"
						+ (System.currentTimeMillis() - start));
				if (printResult) {
					for (Entry<Integer, Integer> e : frequency.entrySet()) {
						System.out.println("key:" + e.getKey() + ","
								+ e.getValue());
					}
				}

			}
		}
	}

}
