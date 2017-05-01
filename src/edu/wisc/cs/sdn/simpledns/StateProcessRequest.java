package edu.wisc.cs.sdn.simpledns;

import java.io.IOException;
import java.net.DatagramPacket;

import edu.wisc.cs.sdn.simpledns.packet.DNS;

public class StateProcessRequest extends State
{

	@Override
	public void runState() 
	{
		System.out.println("StateProcessRequest");
		byte[] localBuffer = new byte[512];
		DatagramPacket localPacket = new DatagramPacket(localBuffer, 512);
		try 
		{
			contextControl.hostOutSocket.receive(localPacket);
			contextControl.dnsRemoteToServer = DNS.deserialize(localPacket.getData(), localPacket.getLength());
			System.out.println(contextControl.dnsRemoteToServer.toString());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		contextControl.hostOutSocket.close();
		
		// Handle depending on recursion
		if (contextControl.dnsClientToServer.isRecursionDesired())
		{

			contextControl.proceedToNextState(StateEnumTypes.STATE_REQUEST_OTHER);
		}
		else
		{
			
			contextControl.proceedToNextState(StateEnumTypes.STATE_CHECK_EC2);
		}
		
	}
	
	
	
}