package edu.ateneo.nrg.hcap.util;

import java.util.HashMap;

public class Flow extends HashMap<String, Object>{
	/**
	 * A HashMap is used to store the flow data to make deserialization easier
	 */
	private static final long serialVersionUID = 1541687412830666926L;
	public static final String TIMESTAMP 	= "timestamp";
	public static final String uTIMESTAMP 	= "microtime";
	public static final String SRCMAC 		= "src_mac"; //from ethernet header 
	public static final String DSTMAC 		= "dst_mac"; //from ethernet header 
	public static final String SRCIP 		= "src_ip"; //from IP header 
	public static final String DSTIP 		= "dst_ip"; //from IP header 
	public static final String PROTOCOL 	= "protocol"; //from IP header 
	public static final String LENGTH 		= "length"; //from IP header 
	public static final String TTL 			= "ttl"; //from IP header 
	public static final String ID 			= "id"; //from IP header 
	public static final String SRCPORT 		= "src_port"; //from protocol header 
	public static final String DSTPORT 		= "dst_port"; //from protocol header 
	public static final String LINKTYPE 	= "linktype";  // from pcap header 
	public static final String ETHERTYPE 	= "ethertype"; // from ethernet header
	public static final String IPVERSION 	= "ip_version";
	
	
}
