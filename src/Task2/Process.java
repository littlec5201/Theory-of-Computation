package Task2;


public class Process implements Runnable{
	private int i;
	private int currentLevel;
	private boolean threadRunning = true;
	private boolean waiting = true;
	private boolean flag = false;
	//private int turn;
	
	public Process(int i){
		this.i = i;
	}

	@Override
	public void run() {
		//Practical example of Petersons mutual exclusion algorithm, each process is a new thread
		while(threadRunning){
			try{
				Thread.currentThread().sleep(2000);
			}catch(Exception e){}
			System.out.println("								PROCESS "+i);
			for(int j = 1; j< Application.n -1; j++){
				Application.p[i] = j;
				Application.y[j] = i;
				setWaiting(true);
				while(waiting){
					Application.printPAndYArrays("Process "+this.getI()+": ");
					if((Application.y[j] != i) || (Application.areAllProcessesAtALowerLevel(this))){
						setWaiting(false);
					}
				}
			}
			try{
				Application.mutex.acquire();
				System.out.println("Critical Section Entered, Process: " + i);
				flag = true;
				setCurrentLevel(0);
				Application.p[i] = 0;
				Application.mutex.release();
				flag = false;
			}catch(Exception e){}
			
		}
	}
	
	public boolean isThreadRunning() {
		return threadRunning;
	}

	public void setThreadRunning(boolean threadRunning) {
		this.threadRunning = threadRunning;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public boolean isWaiting() {
		return waiting;
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

	public int GetProcessNo() {
		return this.i;
	}
	
	public void setCurrentLevel(int level){
		this.currentLevel  = level;
	}
	
	public int getCurrentLevel()
	{
		return this.currentLevel;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}
