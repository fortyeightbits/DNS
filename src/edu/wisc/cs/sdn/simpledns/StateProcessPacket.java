package edu.wisc.cs.sdn.simpledns;
import java.util.List;

import edu.wisc.cs.sdn.simpledns.packet.DNS;
import edu.wisc.cs.sdn.simpledns.packet.DNSQuestion;

public class StateProcessPacket extends State
{

	@Override
	public void runState() 
	{
		System.out.println("StateProcessPacket");
		
		// Deserialize and store dns packet
		contextControl.dnsClientToServer = DNS.deserialize(contextControl.dataPacket.getData(), contextControl.dataPacket.getLength());
		
		System.out.println(contextControl.dnsClientToServer.toString());
//		contextControl.proceedToNextState(StateEnumTypes.STATE_RECEIVE_PACKET);
		if (contextControl.dnsClientToServer.getOpcode() != 0)
		{
			contextControl.proceedToNextState(StateEnumTypes.STATE_RECEIVE_PACKET);
		}
		else
		{
			contextControl.copyToDnsPacketBuffer(contextControl.dnsClientToServer);
			contextControl.proceedToNextState(StateEnumTypes.STATE_REQUEST_OTHER);
		}
	}
	
	
}