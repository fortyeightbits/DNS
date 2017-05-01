package edu.wisc.cs.sdn.simpledns;

import java.util.ArrayList;
import java.util.Iterator;

import edu.wisc.cs.sdn.simpledns.packet.DNS;
import edu.wisc.cs.sdn.simpledns.packet.DNSResourceRecord;

public class StateCheckEc2 extends State
{
	public StateCheckEc2(Ec2List listIn)
	{
		list = listIn;
	}
	
	public Ec2List list;
	
	@Override
	public void runState() {
		System.out.println("StateCheckEc2");
		
		// Get the last answer:
		ArrayList<DNSResourceRecord> answerList = (ArrayList<DNSResourceRecord>)(contextControl.dnsRemoteToServer.getAnswers());

		for (DNSResourceRecord record : answerList)
		{
			System.out.println(record.toString());
		}
		
		
		//		DNSResourceRecord lastAnswer = answerList.get(answerList.size() - 1);
//		
//		// Check if query is of A type
//		if (lastAnswer.getType() == DNS.TYPE_A)
//		{
//			
//		}
		
		
		
		contextControl.proceedToNextState(StateEnumTypes.STATE_RECEIVE_PACKET);
	}
	
	
	
}