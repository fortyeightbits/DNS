package edu.wisc.cs.sdn.simpledns;

import java.util.LinkedList;

public class StateContext
{
	public StateContext(LinkedList<State> states, State startingState)
	{
		stateList = states;
		currentState = startingState;
		// Tell all states that you're the controller:
		for (State selectedState : states)
		{
			selectedState.setStateContext(this);
		}
	}
	
	public StateContext() 
	{
		// Stub overload constructor. Do not populate.
	}

	//Members
	protected LinkedList<State> stateList;
	
	protected State currentState;
	
	// Methods
	public void proceedToNextState(StateEnumTypes nextState)
	{
		switch (nextState)
		{
		case STATE_RECEIVE_PACKET:
			currentState = stateList.get(0);
		case STATE_PROCESS_PACKET:
			currentState = stateList.get(1);
		case STATE_REQUEST_OTHER:
			currentState = stateList.get(2);
		case STATE_PROCESS_REQUEST:
			currentState = stateList.get(3);
		case STATE_CHECK_EC2:
			currentState = stateList.get(4);
		}
	}
	
	public void runCurrentState()
	{
		currentState.runState();
	}
	
//	STATE_RECEIVE_PACKET,
//	STATE_PROCESS_PACKET,
//	STATE_REQUEST_OTHER,
//	STATE_PROCESS_REQUEST,
//	STATE_CHECK_EC2,
}