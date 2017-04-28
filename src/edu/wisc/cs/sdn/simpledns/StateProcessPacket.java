package edu.wisc.cs.sdn.simpledns;


public class StateProcessPacket extends State
{

	@Override
	public void runState() 
	{
		System.out.println("StateProcessPacket");
		
		contextControl.proceedToNextState(StateEnumTypes.STATE_REQUEST_OTHER);
	}
	
	
	
}