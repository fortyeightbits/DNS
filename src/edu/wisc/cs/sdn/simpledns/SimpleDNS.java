package edu.wisc.cs.sdn.simpledns;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;

public class SimpleDNS 
{
	public static final int dnsPort = 8053;
	
	public static void main(String[] args)
	{
        System.out.println("DNS server started, waiting for query..."); 
		
        // Arglist
		String rootServerIP = args[1];
		String ec2Path = args[3];
		
		// Parse EC2List to RAM
		Ec2List ec2 = new Ec2List();
		ec2.setAndInitializeList(ec2Path);
		
		// Setup state machine:
		LinkedList<State> stateList = new LinkedList<State>();
		
		StateReceivePacket stateReceivePacket = new StateReceivePacket();
		stateList.add(stateReceivePacket);
		
		StateProcessPacket stateProcessPacket = new StateProcessPacket();
		stateList.add(stateProcessPacket);
		
		StateRequestOther stateRequestOther = new StateRequestOther(SimpleDNS.ipStringToInt(rootServerIP));
		stateList.add(stateRequestOther);
		
		StateProcessRequest stateProcessRequest = new StateProcessRequest();
		stateList.add(stateProcessRequest);
			
		StateContext mainContext = new StateContext(stateList, stateReceivePacket);
		
		// Loop forever in state machine
		while (true)
		{
			mainContext.runCurrentState();
		}
	}
	
	
	public static int ipStringToInt(String ipAddress)
	{
        if (ipAddress == null)
            throw new IllegalArgumentException("Specified IPv4 address must" +
                "contain 4 sets of numerical digits separated by periods");
        String[] octets = ipAddress.split("\\.");
        if (octets.length != 4)
            throw new IllegalArgumentException("Specified IPv4 address must" +
                "contain 4 sets of numerical digits separated by periods");

        int result = 0;
        for (int i = 0; i < 4; ++i) {
            int oct = Integer.valueOf(octets[i]);
            if (oct > 255 || oct < 0)
                throw new IllegalArgumentException("Octet values in specified" +
                        " IPv4 address must be 0 <= value <= 255");
            result |=  oct << ((3-i)*8);
        }
        return result;
	}
}