package task6;

import java.util.Arrays;

/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
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

	// number of available pepper shakers
	private int numOfPepperShakers = 2;
	
	/**
	 * Modified: set up the dining table with philosophers and chopsticks
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
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
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Modified:
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID) throws InterruptedException
	{
		// get philosopher ID indexed at 0
		int index = piTID - 1;
		
		// test if both chopsticks are available, this prevents deadlock
		while((chopstick[index] == false) || chopstick[(index + 1) % numOfPhilosopher] == false) {
			// wait for chocksticks to be available
			state[index] = State.HUNGRY;
			wait();
		}
		// both chopsticks are available and set both chopsticks as taken
		chopstick[index] = false;
		chopstick[(index + 1) % numOfPhilosopher] = false;
		state[index] = State.EATING;
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
	 * Only one philopher at a time is allowed to philosophy
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
	
	/**
	 * Added:
	 * Request for a pepper shaker while eating
	 */
	public synchronized void requestPepperShaker(final int piTID) throws InterruptedException
	{
		// test if there's an available pepper shaker
		while(numOfPepperShakers == 0) {
			// everyone is using pepper shaker
			wait();
		}
		// there's an available pepper shaker
		numOfPepperShakers--;
	}

	/**
	 * Added:
	 * Put down the pepper shaker after using
	 */
	public synchronized void endPepperShaker(final int piTID)
	{
		// put pepper shaker back
		numOfPepperShakers++;
		
		// notify others that they are done using pepper shaker
		notifyAll();
	}
}

// EOF
