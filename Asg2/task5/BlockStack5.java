package task5;

import common.StackAccessException;

/**
 * Class task1.BlockStack
 * Implements character block stack and operations upon it.
 *
 * $Revision: 1.4 $
 * $Last Revision Date: 2019/02/02 $
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca;
 * Inspired by an earlier code by Prof. D. Probst

 */
class BlockStack5
{
	/**
	 * # of letters in the English alphabet + 2
	 */
	public static final int MAX_SIZE = 28;

	/**
	 * Default stack size
	 */
	public static final int DEFAULT_SIZE = 6;

	/**
	 * Current size of the stack
	 */
	private int iSize = DEFAULT_SIZE;

	/**
	 * Current top of the stack
	 */
	private int iTop  = 3;

	/**
	 * stack[0:5] with four defined values
	 */
	private char acStack[] = new char[] {'a', 'b', 'c', 'd', '*', '*'};

	private int stackAccessCounter;

	/**
	 * Default constructor
	 */
	public BlockStack5()
	{
	}

	/**
	 * Supplied size
	 */
	public BlockStack5(final int piSize)
	{
        if(piSize != DEFAULT_SIZE)
		{
			this.acStack = new char[piSize];

			// Fill in with letters of the alphabet and keep
			// 2 free blocks
			for(int i = 0; i < piSize - 2; i++)
				this.acStack[i] = (char)('a' + i);

			this.acStack[piSize - 2] = this.acStack[piSize - 1] = '*';

			this.iTop = piSize - 3;
                        this.iSize = piSize;
		}
	}

	/**
	 * Picks a value from the top without modifying the stack
	 * @return top element of the stack, char
	 */
	public char pick() throws StackAccessException {
		stackAccessCounter++;
		if (isEmpty()) {
			throw new StackAccessException("Empty stack !!!");
		}
		return this.acStack[this.iTop];
	}

	/**
	 * Returns arbitrary value from the stack array
	 * @return the element, char
	 */
	public char getAt(final int piPosition) throws StackAccessException {
		stackAccessCounter++;
		if(piPosition >= iSize) {
			throw new StackAccessException("Index " + piPosition + " is out of bounds !!!");
		}
		return this.acStack[piPosition];
	}

	/**
	 * Standard push operation
	 */
	public void push(final char pcBlock) throws StackAccessException {
		stackAccessCounter++;
		if(isEmpty()) {
			this.acStack[++this.iTop] = 'a';
		}else if(iTop < iSize - 1) {
			this.acStack[++this.iTop] = pcBlock;
		}else {
			throw new StackAccessException("Full stack !!!");
		}
		System.out.println("Pushed successfully !!!");
	}

	/**
	 * Standard pop operation
	 * @return ex-top element of the stack, char
	 */
	public char pop() throws StackAccessException {
		stackAccessCounter++;
		if(isEmpty()){
			throw new StackAccessException("Empty stack !!!");
		}
		char cBlock = this.acStack[this.iTop];
		this.acStack[this.iTop--] = '*'; // Leave prev. value undefined
		System.out.println("Popped successfully !!!");
		return cBlock;
	}

	/**
	 * Returns the value of iSize
	 * @return iSize, int
	 */
	public int getISize() {
		return iSize;
	}

	/**
	 * Returns the value of iTop
	 * @return iTop, int
	 */
	public int getITop() {
		return iTop;
	}

	/**
	 * Returns the value of acStack
	 * @return acStack, char[]
	 */
	public char[] getACStack() {
		return acStack;
	}

	/**
	 * Returns whether the stack is empty or not
	 * @return bool
	 */
	public boolean isEmpty() {
		return (this.iTop == -1);
	}

	/**
	 * @return stackAccessCounter, int
	 */
	public int getAccessCounter() {
		return stackAccessCounter;
	}
}

// EOF
