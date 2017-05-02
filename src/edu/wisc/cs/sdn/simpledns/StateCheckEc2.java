package edu.wisc.cs.sdn.simpledns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

import edu.wisc.cs.sdn.simpledns.packet.DNS;
import edu.wisc.cs.sdn.simpledns.packet.DNSRdataString;
import edu.wisc.cs.sdn.simpledns.packet.DNSResourceRecord;

public class StateCheckEc2 extends State
{
	public StateCheckEc2(Ec2List listIn)
	{
		list = listIn;
	}
	
	public Ec2List list;
	
	@Override
	public void runState(){
		System.out.println("StateCheckEc2");
		
		// Get the last answer:
		ArrayList<DNSResourceRecord> additionalList = (ArrayList<DNSResourceRecord>)(contextControl.dnsRemoteToServer.getAdditional());

		for (DNSResourceRecord record : additionalList)
		{
//			System.out.println(record.getData().toString());
			if (record.getType() == DNS.TYPE_A)
			{
				DNSRdataString dataString = list.searchForIp(SimpleDNS.ipStringToInt(record.getData().toString()));
//				System.out.println(dataString.getString());
				if (dataString.getString() != "")
				{
					// Add records and stuff
					DNSResourceRecord txtRecord = new DNSResourceRecord();
					txtRecord.setData(dataString);
					txtRecord.setType(DNS.TYPE_TXT);
					contextControl.dnsRemoteToServer.addAnswer(txtRecord);
					txtRecord.setName(record.getName());
					txtRecord.setTtl(record.getTtl());
				}
			}
		}
		
		// Now send back to client:
		
		InetAddress clientAddress = contextControl.dataPacket.getAddress();
		int clientPort = contextControl.dataPacket.getPort();
		System.out.println("ClientAdd: "+clientAddress.toString());
		try 
		{
			System.out.println("Before returning to client, packet looks like this:");
			System.out.println(contextControl.dnsRemoteToServer.toString());
			DatagramSocket socketToClient = new DatagramSocket(SimpleDNS.dnsPort);
			DatagramPacket packetToClient = new DatagramPacket(contextControl.dnsRemoteToServer.serialize(), contextControl.dnsRemoteToServer.getLength(), clientAddress, clientPort);
			socketToClient.send(packetToClient);
			socketToClient.close();
		}
		catch (SocketException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		//		DNSResourceRecord lastAnswer = answerList.get(answerList.size() - 1);
//		
//		// Check if query is of A type
//		if (lastAnswer.getType() == DNS.TYPE_A)
//		{
//			
//		}
		
		
		contextControl.resetIpToQuery();
		contextControl.proceedToNextState(StateEnumTypes.STATE_RECEIVE_PACKET);
	}
	
	
	
}