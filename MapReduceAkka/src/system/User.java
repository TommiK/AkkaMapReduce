package system;

import java.util.ArrayList;

public class User {
	private ArrayList<Integer> visitedPlaces=new ArrayList<Integer>();
	
	public User(){
		
	}
	
	public User(int[]places){
		createPlaces(places);
	}
	
	public void createPlaces(int[] places){
		for (int i = 0; i < places.length; i++) {
			visitedPlaces.add((Integer)places[i]);
		}
	}

	public ArrayList<Integer> getVisitedPlaces() {
		return visitedPlaces;
	}

	public void setVisitedPlaces(ArrayList<Integer> visitedPlaces) {
		this.visitedPlaces = visitedPlaces;
	}
	
	
	
	
	
}
