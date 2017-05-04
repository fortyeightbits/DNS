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
		byte[] localBuffer = new byte[1024];
		DatagramPacket localPacket = new DatagramPacket(localBuffer, localBuffer.length);
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
				boolean hasCname = false;
				contextControl.copyToDnsPacketBuffer(contextControl.dnsClientToServer);
				List<DNSResourceRecord> answerList = contextControl.dnsRemoteToServer.getAnswers();
				// Check for CNAME
				for (DNSResourceRecord answerRecord : answerList)
				{
					if (answerRecord.getType() == DNS.TYPE_CNAME)
					{
						System.out.println("CNAME FOUND!!!");
						// Set the question to send out cname if cname is found
						List<DNSQuestion> existingQuestions = contextControl.getDnsPacketBuffer().getQuestions();
						DNSQuestion cnameQuestion = existingQuestions.get(0);
						contextControl.setCNameRecord(answerRecord, cnameQuestion);
						cnameQuestion.setName(answerRecord.getData().toString());
						
						contextControl.getDnsPacketBuffer().setQuestions(existingQuestions);
						contextControl.dnsClientToServer.setQuestions(existingQuestions);
						contextControl.setExitString(answerRecord.getData().toString());
//							contextControl.setIpToQuery(contextControl.);
//							
						contextControl.proceedToNextState(StateEnumTypes.STATE_REQUEST_OTHER);
						hasCname = true;
						break;
					}
				}
				
				if (!hasCname)
				{
					// recurse on A:
					List<DNSResourceRecord> additionalList = contextControl.dnsRemoteToServer.getAdditional();
	
					for (DNSResourceRecord aEntry : additionalList)
					{
						if (aEntry.getType() == DNS.TYPE_A)
						{
//							List<DNSQuestion> existingQuestions = contextControl.getDnsPacketBuffer().getQuestions();
//							DNSQuestion cnameQuestion = existingQuestions.get(0);
//							cnameQuestion.setName(contextControl.getExitString());
//							
//							contextControl.getDnsPacketBuffer().setQuestions(existingQuestions);
							contextControl.setIpToQuery(SimpleDNS.ipStringToInt(aEntry.getData().toString()));
							contextControl.proceedToNextState(StateEnumTypes.STATE_REQUEST_OTHER);
	
							break;
						}
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
		String name = contextControl.getExitString();
		
		// Check for end of recursion
		for (DNSResourceRecord record : contextControl.dnsRemoteToServer.getAnswers())
		{
			if(record.getName().equals(name) && ((record.getType() == DNS.TYPE_A)||(record.getType() == DNS.TYPE_AAAA)))
			{
				retVal = true;
				break;
			}
		}
		return retVal;
	}
	
}