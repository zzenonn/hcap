package edu.ateneo.nrg.hcap.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ateneo.nrg.kaitai.network.ArpPacket;
import edu.ateneo.nrg.kaitai.network.EthernetFrame;
import edu.ateneo.nrg.kaitai.network.Ipv4Packet;
import edu.ateneo.nrg.kaitai.network.Ipv6Packet;
import edu.ateneo.nrg.kaitai.network.Packet;
import edu.ateneo.nrg.kaitai.network.PcapHeader;
import edu.ateneo.nrg.kaitai.network.TcpSegment;
import edu.ateneo.nrg.kaitai.network.UdpDatagram;
import io.kaitai.struct.KaitaiStream;

public class PcapParser {
	
	public static final byte[] MAGIC_NUMBER = {(byte)0xd4, (byte)0xc3, (byte)0xb2, (byte)0xa1};
	public static final int HEADER_SIZE = 24;
	public static final int PACKET_HEADER_SIZE = 16;
	public static final Log LOG = LogFactory.getLog(PcapParser.class);
	
	
	private PcapHeader hdr;
	private final DataInputStream stream;
	
	long count = 0;
	long damaged = 0;
	long onlyIp = 0;

	public PcapParser(DataInputStream is) throws IOException {
		this.stream = is;
		if (this.stream.available() < HEADER_SIZE) {
			LOG.warn("Skipping empty or broken file");
			return;
		}
		byte[] rawHdr = new byte[HEADER_SIZE];
		this.stream.readFully(rawHdr);
		this.hdr = new PcapHeader(new KaitaiStream(rawHdr));
		if(!Arrays.equals(MAGIC_NUMBER, hdr.magicNumber()))
		{
			throw new IOException("Not a PCAP file (Couldn't find magic number)");
		}
		

	}
	
	public Flow next() throws IOException {
		Flow flow = getNext();
		while(flow == null)
			flow = getNext();
		return flow;
	}
	
	public boolean hasNext() throws IOException {
		if(this.stream.available() > 0)
			return true;
		else {
			LOG.info(count + " packets were parsed completely.");
			LOG.warn(onlyIp + " packets were not parsed fully. Only the IP header was parsed.");
			LOG.warn(damaged + " packets were skipped because of damage or lack of support.");
			return false;
		}
	}
	
    public static String bytesToHexMac(byte[] bytes){

    	return (String.format("%02x:", bytes[0] & 0xff)) + (String.format("%02x:", bytes[1] & 0xff)) 
    			+ (String.format("%02x:", bytes[2] & 0xff)) + (String.format("%02x:", bytes[3] & 0xff)) 
    			+  (String.format("%02x:", bytes[4] & 0xff)) +  (String.format("%02x", bytes[5] & 0xff));

    }  
    
    public static String bytesToIpString(byte[] bytes) throws UnknownHostException{
    	return InetAddress.getByAddress(bytes).getHostAddress();
    }
	
	private Flow getNext() throws IOException {
		byte[] rawPacket = new byte[16];
		Flow flow = new Flow();
		
		Object packet = null;
		this.stream.readFully(rawPacket);
		
		int ipVersion = 0;
		int protocol = 0;
		
		Packet packetHdr = new Packet(new KaitaiStream(rawPacket));
		
		byte[] rawPacketData = new byte[(int) packetHdr.inclLen()];
		this.stream.readFully(rawPacketData);
		flow.put(Flow.TIMESTAMP, packetHdr.tsSec());
		flow.put(Flow.uTIMESTAMP, packetHdr.tsUsec());
		switch(hdr.network()) {
		case ETHERNET: {
			EthernetFrame frame = null;
			try {
				frame = new EthernetFrame(new KaitaiStream(rawPacketData));
			} catch (Exception e) {
				damaged++;
				return null;
			}

			flow.put(Flow.SRCMAC, bytesToHexMac(frame.srcMac()));
			flow.put(Flow.DSTMAC, bytesToHexMac(frame.dstMac()));
			flow.put(Flow.ETHERTYPE, frame.etherType().toString());
			switch(frame.etherType()) {
			case ARP: {
				ArpPacket arp = new ArpPacket(new KaitaiStream((byte[]) frame.body()));
				flow.put(Flow.SRCIP, bytesToIpString(arp.spa()));
				flow.put(Flow.DSTIP, bytesToIpString(arp.tpa()));
				count++;
				return flow;
			}
			case IPV4: {
    			flow.put(Flow.IPVERSION, 4);
    			ipVersion = 4;
    			packet = frame.body();
				break;
			}
			case IPV6: {
    			flow.put(Flow.IPVERSION, 6);
    			ipVersion = 6;
				break;
			}
			default:
				return flow;
			}
			break;
		}
		case RAW: {
        	byte ipv =  (byte) (rawPacketData[0] & 0xf0);
        	switch (ipv) {
        	case 0x40: {
    			flow.put(Flow.IPVERSION, 4);
    			ipVersion = 4;
    			try {
					packet = new Ipv4Packet(new KaitaiStream(rawPacketData));
				} catch (Exception e) {
					damaged++;
					return null;
				}
        		break;
        	}
        	case 0x60: {
    			flow.put(Flow.IPVERSION, 6);
    			ipVersion = 6;
    			try {
					packet = new Ipv6Packet(new KaitaiStream(rawPacketData));
				} catch (Exception e) {
					damaged++;
					return null;
				}
        		break;
        	}
        	default: {
        		LOG.error("IP version "+ String.format("%02x:", ipv & 0xff) +" not supported. Packet skipped.");
        		damaged++;
        		return null;
        	}
        	}
			break;
		}
		default: {
			LOG.error("Linktype" + hdr.network() + " not supported. Packet skipped.");
			damaged++;
			return null;
		}
		}
		
		switch(ipVersion) {
		case 4: {
			flow.put(Flow.SRCIP, bytesToIpString(((Ipv4Packet) packet).srcIpAddr()));
			flow.put(Flow.DSTIP, bytesToIpString(((Ipv4Packet) packet).dstIpAddr()));
			flow.put(Flow.PROTOCOL, ((Ipv4Packet) packet).protocol().toString());
			protocol = (int) ((Ipv4Packet) packet).protocol().id();
			flow.put(Flow.LENGTH, ((Ipv4Packet) packet).totalLength());
			flow.put(Flow.TTL, ((Ipv4Packet) packet).ttl());
			flow.put(Flow.ID, ((Ipv4Packet) packet).identification());
			packet = ((Ipv4Packet) packet).body();
			break;
		}
		case 6: {
			flow.put(Flow.SRCIP, bytesToIpString(((Ipv6Packet) packet).srcIpv6Addr()));
			flow.put(Flow.DSTIP, bytesToIpString(((Ipv6Packet) packet).dstIpv6Addr()));
			flow.put(Flow.PROTOCOL, ((Ipv6Packet) packet).nextHeaderType().toString());
			protocol = (int) ((Ipv6Packet) packet).nextHeaderType().id();
			flow.put(Flow.LENGTH, ((Ipv6Packet) packet).payloadLength());
			flow.put(Flow.TTL, ((Ipv6Packet) packet).hopLimit());
			flow.put(Flow.ID, ((Ipv6Packet) packet).flowLabel());
			packet = ((Ipv6Packet) packet).nextHeader();
			if(packet == null)
				protocol = 0;
			break;
		}
		default: {
			LOG.error("IP version not supported. Packet skipped.");
			damaged++;
			return null;		}
		}
		
		switch(protocol) {
		/**
		 * TCP
		 */
        case 6: {
        	try {
				flow.put(Flow.SRCPORT, ((TcpSegment) packet).srcPort());
				flow.put(Flow.DSTPORT, ((TcpSegment) packet).dstPort());
			} catch (Exception e) {
				onlyIp++;
			}
            break;
        }
        /**
         * UDP
         */
        case 17: {
        	try {
				flow.put(Flow.SRCPORT, ((UdpDatagram) packet).srcPort());
				flow.put(Flow.DSTPORT, ((UdpDatagram) packet).dstPort());
			} catch (Exception e) {
				onlyIp++;
			}
            break;
        }
		}
		count++;
		return flow;
	}

	
	
}
