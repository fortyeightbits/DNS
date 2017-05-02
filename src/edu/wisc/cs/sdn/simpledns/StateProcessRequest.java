package edu.wisc.cs.sdn.simpledns;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;

import edu.wisc.cs.sdn.simpledns.packet.DNS;
import edu.wisc.cs.sdn.simpledns.packet.DNSQuestion;
import edu.wisc.cs.sdn.simpledns.packet.DNSResourceRecord;

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
			// If recursion is not complete, do recursion processing:
			if (!isRecursionComplete())
			{
	//			System.out.println("RECURSION IS DESIREDDD!!!!!!");
				List<DNSResourceRecord> answerList = contextControl.dnsRemoteToServer.getAnswers();
				
				// Check for CNAME
				for (DNSResourceRecord answerRecord : answerList)
				{
					if (answerRecord.getType() == DNS.TYPE_CNAME)
					{
						// Set the question to send out cname if cname is found
						contextControl.copyToDnsPacketBuffer(contextControl.dnsClientToServer);
						List<DNSQuestion> existingQuestions = contextControl.getDnsPacketBuffer().getQuestions();
						DNSQuestion cnameQuestion = existingQuestions.get(0);
						cnameQuestion.setName(answerRecord.getName());
						
						contextControl.getDnsPacketBuffer().setQuestions(existingQuestions);
						break;
					}
				}
				
				// recurse on A:
				List<DNSResourceRecord> additionalList = contextControl.dnsRemoteToServer.getAdditional();

				for (DNSResourceRecord aEntry : additionalList)
				{
					if (aEntry.getType() == DNS.TYPE_A)
					{
						contextControl.setIpToQuery(SimpleDNS.ipStringToInt(aEntry.getData().toString()));
						contextControl.copyToDnsPacketBuffer(contextControl.dnsClientToServer);
						contextControl.proceedToNextState(StateEnumTypes.STATE_REQUEST_OTHER);
						break;
					}
				}

				
				
			}
			else
			{
				contextControl.proceedToNextState(StateEnumTypes.STATE_CHECK_EC2);
			}
		}
		else
		{
//			System.out.println("RECURSION IS NOT DESIRED =(");
			contextControl.proceedToNextState(StateEnumTypes.STATE_CHECK_EC2);
		}
		
	}
	
	public boolean isRecursionComplete()
	{
		boolean retVal = false;
		DNSQuestion question = contextControl.dnsClientToServer.getQuestions().get(0);
		String name = question.getName();
		
		// Check for end of recursion
		for (DNSResourceRecord record : contextControl.dnsRemoteToServer.getAnswers())
		{
			if(record.getName().equals(name))
			{
				retVal = true;
				break;
			}
		}
		return retVal;
	}
	
}