package edu.wisc.cs.sdn.simpledns;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.LinkedList;

import edu.wisc.cs.sdn.simpledns.packet.DNS;

public class StateContext
{
	public StateContext(LinkedList<State> states, State startingState)
	{
		stateList = states;
		currentState = startingState;
		
		buffer = new byte[512];
		exitString = new String();
		dataPacket = new DatagramPacket(buffer, 512);
		dnsClientToServer  = new DNS();
		dnsRemoteToServer = new DNS();
		dnsPacketBuffer = new DNS();
		
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
	
	// Data to pass between states:
	private String exitString;
	public byte [] buffer;
	public DatagramPacket dataPacket;
	public DNS dnsClientToServer;
	public DatagramSocket hostOutSocket;
	public DNS dnsRemoteToServer;
	
	protected DNS dnsPacketBuffer;
	
	
	// Methods
	public void proceedToNextState(StateEnumTypes nextState)
	{
		switch (nextState)
		{
		case STATE_RECEIVE_PACKET:
			currentState = stateList.get(0);
			break;
		case STATE_PROCESS_PACKET:
			currentState = stateList.get(1);
			break;
		case STATE_REQUEST_OTHER:
			currentState = stateList.get(2);
			break;
		case STATE_PROCESS_REQUEST:
			currentState = stateList.get(3);
			break;
		case STATE_CHECK_EC2:
			currentState = stateList.get(4);
			break;
		}
		
		return;
	}
	
	public void runCurrentState()
	{
		currentState.runState();
	}
	
	public void copyToDnsPacketBuffer(DNS dnsIn)
	{
		dnsPacketBuffer = DNS.deserialize(dnsIn.serialize(), dnsIn.getLength());
	}
	
	public DNS getDnsPacketBuffer()
	{
		return dnsPacketBuffer;
	}
	
	public void setIpToQuery(int i)
	{
		StateRequestOther requestState = ((StateRequestOther)stateList.get(2));
		requestState.setCurrentServerIp(SimpleDNS.ipStringToBytes(SimpleDNS.fromIPv4Address(i)));
	}
	
	public void resetIpToQuery()
	{
		StateRequestOther requestState = ((StateRequestOther)stateList.get(2));
		requestState.resetRemoteIp();
	}
	
	public void setExitString(String exit)
	{
		exitString = exit;
	}
	
	public String getExitString()
	{
		return exitString;
	}
	
//	STATE_RECEIVE_PACKET,
//	STATE_PROCESS_PACKET,
//	STATE_REQUEST_OTHER,
//	STATE_PROCESS_REQUEST,
//	STATE_CHECK_EC2,
}