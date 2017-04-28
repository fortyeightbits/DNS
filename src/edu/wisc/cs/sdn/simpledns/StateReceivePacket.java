package edu.wisc.cs.sdn.simpledns;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class StateReceivePacket extends State
{
	@Override
	public void runState() 
	{
		System.out.println("StateReceivePacket");
//		// Attempt to create new socket listening on dnsPort
//		try 
//		{
//			DatagramSocket datagramSocket = new DatagramSocket(SimpleDNS.dnsPort);
//			
//			DatagramPacket dataPacket = new Datagrampacket
//			
//			datagramSocket.receive(p);
//			
//			
//			
//			datagramSocket.close();
//		} 
//		catch (SocketException e) 
//		{
//			
//			e.printStackTrace();
//		}
		contextControl.proceedToNextState(StateEnumTypes.STATE_PROCESS_PACKET);
	}
	
	
	
}