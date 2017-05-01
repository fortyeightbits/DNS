package edu.wisc.cs.sdn.simpledns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class StateReceivePacket extends State
{
	@Override
	public void runState() 
	{
		System.out.println("StateReceivePacket");
		// Attempt to create new socket listening on dnsPort
		try 
		{
			DatagramSocket datagramSocket = new DatagramSocket(SimpleDNS.dnsPort);
			
			datagramSocket.receive(contextControl.dataPacket);
			
			datagramSocket.close();
		}
		catch (SocketException e) 
		{
			System.out.println("Problem with socket!");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println("IOException!");
			e.printStackTrace();
		}
		
		contextControl.proceedToNextState(StateEnumTypes.STATE_PROCESS_PACKET);
	}
	
	
	
}