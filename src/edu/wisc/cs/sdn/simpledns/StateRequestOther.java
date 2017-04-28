package edu.wisc.cs.sdn.simpledns;


public class StateRequestOther extends State
{
	public StateRequestOther(int rootServerIpIn)
	{
		currentServerIp = rootServerIpIn;
	}
	
	// Members
	public int currentServerIp;
	
	// Methods
	@Override
	public void runState() 
	{
		System.out.println("StateRequestOther");	
		
		contextControl.proceedToNextState(StateEnumTypes.STATE_PROCESS_REQUEST);
	}
	
	
	
}