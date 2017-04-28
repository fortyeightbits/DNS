package edu.wisc.cs.sdn.simpledns;


public abstract class State
{
	public State()
	{
		contextControl = new StateContext();
	}
	
	// Members:
	protected StateContext contextControl;
	
	// Methods
	public abstract void runState();
	
	public void setStateContext(StateContext context)
	{
		contextControl = context;
	}
	
	
	
}