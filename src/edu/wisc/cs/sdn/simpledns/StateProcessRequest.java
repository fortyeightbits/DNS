package edu.wisc.cs.sdn.simpledns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

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
			DatagramSocket socketOut = new DatagramSocket(53);
			socketOut.receive(localPacket);
			contextControl.dnsRemoteToServer = DNS.deserialize(localPacket.getData(), localPacket.getLength());
			System.out.println(contextControl.dnsRemoteToServer.toString());
			socketOut.close();
		} 
		catch (IOException e) 
		{
			System.out.println("IOException! Unable to send out!");
			e.printStackTrace();
		}
		
		
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