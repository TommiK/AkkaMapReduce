package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import system.MapReduceActorSystem;
import system.Master;
import system.User;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

//single Threaded Akka
public class Main {
	public static void main(String[] args) {
		int numberOfPlaces=100;
		int numberOfUsersPerSequence=100;
		
//		createSequence("sequenceA.txt",numberOfPlaces,20000);
//		createSequence("sequenceB.txt",numberOfPlaces,20000);
//		createSequence("sequenceC.txt",numberOfPlaces,20000);
		
		int [] visitedA=readSequence("sequenceA.txt");
		int [] visitedB=readSequence("sequenceB.txt");
		int [] visitedC=readSequence("sequenceC.txt");
		ArrayList<User>users=new ArrayList<User>();
		addUsers(users,visitedA,numberOfUsersPerSequence);
		addUsers(users,visitedB,numberOfUsersPerSequence);
		addUsers(users,visitedC,numberOfUsersPerSequence);

		new MapReduceActorSystem(users);//implicitly start System
		
	}
	
	public static void addUsers(ArrayList<User>list, int[] visitedPlaces, int times){
		for (int i = 0; i < times; i++) {
			list.add(new User(visitedPlaces));
		}
	}
	
	private static int[] readSequence(String fileName){
		BufferedReader reader=null;
		try {
			reader = new BufferedReader( new FileReader (fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	   

	    try {
			while( ( line = reader.readLine() ) != null ) {
			    stringBuilder.append( line );
			    
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    String[]numbers= stringBuilder.toString().split(",");
	    int [] results= new int[numbers.length];
	    
	    for (int i = 0; i < results.length; i++) {
			results[i]=Integer.parseInt(numbers[i]);
		}
	    try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   // System.out.println("endRead");
	    return results;
	    
	}

	private static void createSequence(String fileName,int max,int length) {
		BufferedWriter out=null;
		try {
			out= new BufferedWriter(new FileWriter(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			for (int i = 0; i < length; i++) {
				
				if(i==length-1){
					
					out.write(""+(int)Math.round(Math.random()*length));
				}else{
					out.write(""+(int)Math.round(Math.random()*length)+",");
					
				}
					
			}
			out.flush();
			
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}