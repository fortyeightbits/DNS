package edu.wisc.cs.sdn.simpledns;

class IPelement
{
	// Constructor
	public IPelement()
	{
		// member initialization
		ip = 0;
		ipMask = 0;
		regionName = new String();
	}
	
	// Members:
	public int ip;
	public int ipMask;
	public String regionName;
	
	// Methods:
	
	public void setIpMask(int maskSize)
	{
		int numberToShift = 32 - maskSize;
		ipMask = Integer.MAX_VALUE << numberToShift;
	}
	
	
}