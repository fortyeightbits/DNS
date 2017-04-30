package edu.wisc.cs.sdn.simpledns;


public class StateCheckEc2 extends State
{

	@Override
	public void runState() {
//		System.out.println("StateCheckEc2");
		
		contextControl.proceedToNextState(StateEnumTypes.STATE_RECEIVE_PACKET);
	}
	
	
	
}