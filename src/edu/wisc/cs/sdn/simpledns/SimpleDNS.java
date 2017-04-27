package edu.wisc.cs.sdn.simpledns;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class SimpleDNS 
{
	public static final int dnsPort = 8053;
	
	public static void main(String[] args)
	{
		String rootServerIP = args[1];
		String ec2Path = args[3];
		
		Ec2List ec2 = new Ec2List();
		ec2.setAndInitializeList(ec2Path);
		// Attempt to create new socket listening on dnsPort
		try 
		{
			DatagramSocket datagramSocket = new DatagramSocket(dnsPort);
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}

//		receive(DatagramPacket p)
        System.out.println("Hello, DNS!"); 
	}
}