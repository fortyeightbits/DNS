package edu.wisc.cs.sdn.simpledns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class StateRequestOther extends State
{
	public StateRequestOther(byte[] rootServerIpIn)
	{
		for (byte test : rootServerIpIn)
		{
			System.out.println("Bytes: " + test);
		}
		currentServerIp = rootServerIpIn;
	}
	
	// Members
	public byte[] currentServerIp;
	
	// Methods
	@Override
	public void runState() 
	{
		System.out.println("StateRequestOther");	
		
		try 
		{	
//			System.out.println(InetAddress.getByAddress(currentServerIp).toString());
			DatagramSocket socketOut = new DatagramSocket(53);
			InetAddress add = InetAddress.getByAddress(currentServerIp);
//			InetSocketAddress sockAdd = new InetSocketAddress(add, 53);
//			socketOut.bind(sockAdd);
//			contextControl.hostOutSocket = socketOut;
			
			contextControl.dnsClientToServer.setQuery(true);
//			System.out.println("THis is what we're sending out: " + contextControl.dnsClientToServer.toString());
			DatagramPacket dataOut = new DatagramPacket(contextControl.dnsClientToServer.serialize(), contextControl.dnsClientToServer.getLength(), add, 53);
			dataOut.setAddress(InetAddress.getByAddress(currentServerIp));
			socketOut.send(dataOut);
			
			socketOut.close();
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		contextControl.proceedToNextState(StateEnumTypes.STATE_PROCESS_REQUEST);
	}
	
	public void setCurrentServerIp(byte[] ip)
	{
		currentServerIp = ip;
	}
	
	
}