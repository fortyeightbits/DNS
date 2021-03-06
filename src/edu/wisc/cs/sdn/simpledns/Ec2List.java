package edu.wisc.cs.sdn.simpledns;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import edu.wisc.cs.sdn.simpledns.packet.DNSRdataString;

public class Ec2List
{
	// Members:
	public LinkedList<IPelement> elementList;
	
	
	// Constructor:
	public Ec2List()
	{
		elementList = new LinkedList<IPelement>();
	}
	
	// Methods:
	public void setAndInitializeList(String listLocation)
	{
		// Attempt to open the file given to the filereader. We do not
		// scrub the input.
		try
		{
			// Open a new reader and start parsing the file
			String line;
			BufferedReader bufferedReader = new BufferedReader(new FileReader(listLocation));
			
			while ((line = bufferedReader.readLine())!= null)
			{
				IPelement elementToFill = new IPelement();
				String[] ec2_data = line.split(",");
				// Store the region name
				elementToFill.regionName = ec2_data[1];
				
				// Handle the IP and mask.
				String [] ipStrings = ec2_data[0].split("/");
				elementToFill.ip = SimpleDNS.ipStringToInt(ipStrings[0]);
				elementToFill.setIpMask(Integer.valueOf(ipStrings[1]));
				
				// After we've completed filling the element, add to the list
				elementList.add(elementToFill);
			}
		
			// Close the opened reader when reads are complete
			bufferedReader.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public DNSRdataString searchForIp(int ipToSearchFor)
	{
		DNSRdataString retVal = new DNSRdataString();
		
//		int numberToShift = 32 - 24;
//		int ipMask = Integer.MAX_VALUE << numberToShift;
//		System.out.println(SimpleDNS.fromIPv4Address(ipToSearchFor & ipMask));
		for (IPelement entry : elementList)
		{
			// & with mask to check if it's within range. If it is, it will match IP.
			if ((ipToSearchFor & entry.ipMask) == (entry.ip & entry.ipMask))
			{
				retVal.setString(entry.regionName + "-" + SimpleDNS.fromIPv4Address(entry.ip));
				System.out.println("I found IP in ec2 region!");
				break;
			}
			else
			{
				retVal.setString("");
			}
			
		}
		
		return retVal;
	}
}