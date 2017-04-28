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
		
		return retVal;
	}
}