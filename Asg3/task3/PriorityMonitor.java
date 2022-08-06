package task3;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class PriorityMonitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	// total number of philosophers at the table
	private int numOfPhilosopher;

	// possible states the philosophers can be in
	private enum State { EATING, THINKING, TALKING, WAITING, HUNGRY, SLEEPING };

	// store states of each philosopher
	private State[] state;

	// store state of chopsticks; true means available, false means unavailable
	private boolean[] chopstick;

	// boolean to determine if someone is talking
	private boolean isTalking;

	// count of number of people sleeping
	private int numOfSleeping;

	//eating queue
	PriorityQueue<Integer> eatingQueue;

	//talking queue
	private int[] priorities;


	/**
	 * Modified: set up the dining table with philosophers and chopsticks, introduce priority queue
	 * Constructor
	 */
	public PriorityMonitor(int piNumberOfPhilosophers, int... priorities)
	{		
		// set attributes
		numOfPhilosopher = piNumberOfPhilosophers;
		numOfSleeping = 0;
		isTalking = false;
		
		// set default states
		// set appropriate number of chopsticks based on the # of philosophers
		state = new State[numOfPhilosopher];
		Arrays.fill(state, State.THINKING);
		chopstick = new boolean[numOfPhilosopher];
		Arrays.fill(chopstick, true);
		this.priorities = priorities;
		eatingQueue = new PriorityQueue<>(piNumberOfPhilosophers,(p1, p2) -> p1-p2);
		//priorityQueue = IntStream.of(piNumberOfPhilosophers).boxed().collect(Collectors.toCollection(()->new PriorityQueue<>(numOfPhilosopher,(p1, p2) -> Integer.compare(priorities[p1] , priorities[p2]))));
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Modified: to included priority queue
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID) throws InterruptedException
	{
		// get philosopher ID indexed at 0
		int index = piTID - 1;
		eatingQueue.add(index);
		//System.out.println("Waiting list: :"+ eatingQueue);
		
		// test if both chopsticks are available
		while((chopstick[index] == false) || chopstick[(index + 1) % numOfPhilosopher] == false || eatingQueue.peek() != index) {
			// wait for chocksticks to be available
			state[index] = State.HUNGRY;
			wait();
			
		}
		// both chopsticks are available and set both chopsticks as taken
		chopstick[index] = false;
		chopstick[(index + 1) % numOfPhilosopher] = false;
		state[index] = State.EATING;
		//System.out.println("Allowing: " + index);
		eatingQueue.remove(index);
		//System.out.println("Waiting list: :"+ eatingQueue);
	}

	/**
	 * Modified:
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		// get philosopher ID indexed at 0
		int index = piTID - 1;
		
		// set both chopsticks as free
		chopstick[index] = true;
		chopstick[(index + 1) % numOfPhilosopher] = true;
		
		// set state to thinking
		state[index] = State.THINKING;

		// notify others that the chopsticks are available
		notifyAll();
	}

	/**
	 * Modified:
	 * Only one philosopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk(final int piTID) throws InterruptedException
	{
		// get philosopher ID indexed at 0
		int index = piTID - 1;
		
		// test if some one is talking
		while(isTalking || numOfSleeping > 0) {
			// wait to talk
			state[index] = State.WAITING;
			wait();
		}
		// no one is talking
		state[index] = State.TALKING;
		isTalking = true;
	}

	/**
	 * Modified:
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk(final int piTID)
	{
		// get philosopher ID indexed at 0
		int index = piTID - 1;
		
		// go back to thinking
		isTalking = false;
		state[index] = State.THINKING;
		
		// notify others that they are free to talk
		notifyAll();
	}
	
	/**
	 * Added:
	 * Only allowed to sleep when no one is talking
	 */
	public synchronized void requestSleep(final int piTID) throws InterruptedException
	{
		// get philosopher ID indexed at 0
		int index = piTID - 1;
		
		// test if some one is talking
		while(isTalking) {
			// wait for person to finish talking
			wait();
		}
		// no one is talking
		state[index] = State.SLEEPING;
		numOfSleeping++;
	}

	/**
	 * Added:
	 * When philosopher is done sleeping, others
	 * can feel free to start talking.
	 */
	public synchronized void endSleep(final int piTID)
	{
		// get philosopher ID indexed at 0
		int index = piTID - 1;
		
		// go back to thinking
		numOfSleeping--;
		state[index] = State.THINKING;
		
		// notify others that they are done sleeping
		notifyAll();
	}
}

// EOF
