package task6;

import common.BaseThread;

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;
	
	/**
	 * Added:
	 * Double that gives the percentage chance of a phil talking
	 */
	private static double chanceOfTalking = 0.5;

	/**
	 * Modified: include pepper shaker
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - Use the pepper shaker
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			// Changed
			System.out.println("Philosopher " + getTID() + " has started eating.");
			addPepper();
			yield();
			sleep();
			yield();
			System.out.println("Philosopher " + getTID() + " has finished eating.");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * Modified:
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		// Changed
		try
		{
			System.out.println("Philosopher " + getTID() + " has started thinking.");
			yield();
			sleep();
			yield();
			System.out.println("Philosopher " + getTID() + " has finished thinking.");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * Modified:
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		System.out.println("Philosopher " + getTID() + " has started talking.");
		yield();
		saySomething();
		yield();
		System.out.println("Philosopher " + getTID() + " has finished talking.");
	}

	/**
	 * Added:
	 * The act of sleeping.
	 * - Print the fact that a given phil (their TID) has started sleeping for a random interval
	 * - Then print that they are done sleeping.
	 */
	public void sleep() throws InterruptedException
	{
		DiningPhilosophers.soMonitor.requestSleep(getTID());
		System.out.println("Philosopher " + getTID() + " has started sleeping.");
		sleep((long)(Math.random() * TIME_TO_WASTE));
		DiningPhilosophers.soMonitor.endSleep(getTID());
		System.out.println("Philosopher " + getTID() + " has finished sleeping.");
	}
	
	/**
	 * Added:
	 * The act of adding pepper while eating.
	 * - Print the fact that a given phil (their TID) has started using the pepper shaker.
	 * - Then print that they are done sleeping.
	 */
	public void addPepper() throws InterruptedException
	{
		DiningPhilosophers.soMonitor.requestPepperShaker(getTID());
		System.out.println("Philosopher " + getTID() + " is using a pepper shaker.");
		sleep((long)(Math.random() * TIME_TO_WASTE));
		DiningPhilosophers.soMonitor.endPepperShaker(getTID());
		System.out.println("Philosopher " + getTID() + " has finished using the pepper shaker.");
	}
	
	/**
	 * Modified:
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		try {
			for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
			{
				DiningPhilosophers.soMonitor.pickUp(getTID());
	
				eat();
	
				DiningPhilosophers.soMonitor.putDown(getTID());
	
				think();
	
				/**
				 * Added:
				 * A decision is made at random whether this particular
				 * philosopher is about to say something terribly useful.
				 * There is a 50% chance that the philosopher has something
				 * useful to say.
				 */
				if(Math.random() < chanceOfTalking)
				{
					// Use monitor to queue for a talk
					DiningPhilosophers.soMonitor.requestTalk(getTID());
					talk();
					DiningPhilosophers.soMonitor.endTalk(getTID());
				}
	
				yield();
			}
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}

// EOF
