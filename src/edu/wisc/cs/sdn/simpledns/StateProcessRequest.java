package edu.wisc.cs.sdn.simpledns;


public class StateProcessRequest extends State
{

	@Override
	public void runState() 
	{
		System.out.println("StateProcessRequest");
		
		contextControl.proceedToNextState(StateEnumTypes.STATE_CHECK_EC2);
	}
	
	
	
}