package Task2;
import Task1.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.Semaphore;

public class Application {
	
	public static int n;
	public static int[] y;
	public static int[] p;
	public static Process[] processes;
	public static Semaphore mutex;
	
	public Application(int n) {
		//When initialised, the Application sets up the necessary arrays for the amount of processes passed
		if(n<2){n=2;}
		this.n= n;
		this.y = new int[n];
		this.p = new int[n+1];
		this.processes =  new Process[n+1];
		this.mutex = new Semaphore(1);
		
		//initial values of all arrays
		p[0] = 0;
		processes[0] = null;
		for(int j = 0; j<n; j++){
			int i = j+1;
			y[j] = i;
			processes[i] = new Process(i);
			processes[i].setCurrentLevel(0);
			p[i] = processes[i].getCurrentLevel();
		}
	}
	
	//Checks that the process parameter is the at the highest level (true is yes, false if no)
	public static boolean areAllProcessesAtALowerLevel(Process p){
		for(int i=1;i<=n;i++){
			if(i!=p.getI()){
				if(processes[i].getCurrentLevel()>p.getCurrentLevel()){
					return false;
				}
			}
		}
		return true;
	}
	
	//toString with just the p and y arrays
	public static void printPAndYArrays(String process){
		String pArray = "["+process;
		String yArray = "[";
		for(int x=0;x<n;x++){
			pArray += ""+p[x+1];
			yArray += ""+y[x];
		}
		pArray+="]";
		yArray+="]";
		System.out.print("p: "+pArray);
		System.out.println("| y:  "+yArray);
	}
	
	
	public static void main(String[] args) throws IOException{
		//All work done on implementing the processes was so that we could understand what the algorithm was doing
		//Below is a program which, when given n, produces a Transition System (Object + .txt file outlining it) 
		//of n processes working in parallel composition
		
		//User enters number of processes 'n'
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the number of processes:");
		int n = in.nextInt();
		
		//All arraylists needed to create TransitionSystem Object are created
		ArrayList<State> stateArrayList = new ArrayList<State>();
		ArrayList<Action> actionArrayList = new ArrayList<Action>();
		ArrayList<Transition> transitionArrayList = new ArrayList<Transition>();
		ArrayList<State> initialArrayList = new ArrayList<State>();
		ArrayList<AP> apArrayList = new ArrayList<>();
		
		//Creates States based on 'n' bits, which are represented as 0's if they are not in the critical section,
		//and 1 if they are (n+1 states for all values of n)
		String initialState = "";
		for(int i=0;i<n;i++){
			initialState+="0";
		}
		State iState = new State(initialState);
		stateArrayList.add(iState);
		String stateDesign = "";
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(stateDesign.length()==i){
					stateDesign+="1";
				}else{
					stateDesign+="0";
				}
				
			}
			State newState = new State(stateDesign);
			stateArrayList.add(newState);
			stateDesign = "";
		}
		
		//Only ever 2 actions, entering the critical section and exiting the critical section
		Action enter = new Action("enter");
		Action exit = new Action("exit");
		actionArrayList.add(enter);
		actionArrayList.add(exit);
		
		//Creates all transitions for the Transition System
		for(State s : stateArrayList){
			if(s != stateArrayList.get(0)){
				Transition out = new Transition(stateArrayList.get(0), actionArrayList.get(0), s);
				Transition into = new Transition(s, actionArrayList.get(1), stateArrayList.get(0));
				stateArrayList.get(0).addTransition(out);
				s.addTransition(into);
				transitionArrayList.add(out);
				transitionArrayList.add(into);
			}
			
		}
		
		//Creates all AP for noncrit, wait and crit for n processes
		for(int i=1;i<=n;i++){
			AP nCurrent = new AP("n"+i);
			apArrayList.add(nCurrent);
		}
		for(int i=1;i<=n;i++){
			AP wCurrent = new AP("w"+i);
			apArrayList.add(wCurrent);
		}
		for(int i=1;i<=n;i++){
			AP cCurrent = new AP("c"+i);
			apArrayList.add(cCurrent);
		}
		
		for(int i=0;i<n+1;i++){
			for(int j=0;j<n;j++){
				if(i==0){
					stateArrayList.get(i).getApList().add(apArrayList.get(n+j));
				}else{
					if(i==j+1){
						stateArrayList.get(i).getApList().add(apArrayList.get((n*2)+j));
					}else{
						stateArrayList.get(i).getApList().add(apArrayList.get(n+j));
					}
				}
			}
		}
		
		initialArrayList.add(stateArrayList.get(0));
		
		//Transition System object made
		TransitionSystem ts = new TransitionSystem(stateArrayList, actionArrayList, transitionArrayList, initialArrayList, apArrayList);
		System.out.println(ts);
		
		//Writen to text file
		String states = "S={";
		String actions = "Act={";
		String transitions = "->={";
		String initialStates = "I={";
		String APs = "AP={";
		String labels = "";
		
		File f = new File("MutualExclusion.txt");
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(State s : stateArrayList){
			if(s!=stateArrayList.get(stateArrayList.size()-1)){
				states+=s.getStateName()+",";
			}else{
				states+=s.getStateName()+"}";
			}
		}
		bw.write(states);
		bw.newLine();
		
		for(Action a : actionArrayList){
			if(a!=actionArrayList.get(actionArrayList.size()-1)){
				actions+=a.getActionName()+",";
			}else{
				actions+=a.getActionName()+"}";
			}
		}
		bw.write(actions);
		bw.newLine();
		
		for(Transition t : transitionArrayList){
			transitions+="<";
			if(t!=transitionArrayList.get(transitionArrayList.size()-1)){
				transitions+=t.getStartState()+","+t.getAction()+","+t.getEndState()+">,";
			}else{
				transitions+=t.getStartState()+","+t.getAction()+","+t.getEndState()+">}";
			}
		}
		bw.write(transitions);
		bw.newLine();
		
		initialStates += stateArrayList.get(0)+"}";
		bw.write(initialStates);
		bw.newLine();
		
		for(AP ap : apArrayList){
			if(ap!=apArrayList.get(apArrayList.size()-1)){
				APs+=ap.getAP()+",";
			}else{
				APs+=ap.getAP()+"}";
			}
		}
		bw.write(APs);
		bw.newLine();
		
		for(State s : stateArrayList){
			labels+="L("+s.getStateName()+")={";
			for(AP ap : s.getApList()){
				if(ap!=s.getApList().get(s.getApList().size()-1)){
					labels+=ap+",";
				}else{
					labels+=ap+"}";
				}
			}
			if(s!=stateArrayList.get(stateArrayList.size()-1)){
				labels+=",";
			}
		}
		
		bw.write(labels);
		try {
			if (bw != null)
				bw.close();
			if (fw != null)
				fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
