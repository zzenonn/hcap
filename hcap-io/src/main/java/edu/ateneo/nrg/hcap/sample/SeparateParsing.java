package edu.ateneo.nrg.hcap.sample;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.ateneo.nrg.hcap.util.Flow;
import edu.ateneo.nrg.hcap.util.PcapParser;

public class SeparateParsing {

	public static void main(String[] args) throws IOException {
	long startTime = System.nanoTime();
	InputStream in = new FileInputStream("<path/to/pcap/file>");
	PcapParser parser = new PcapParser(new DataInputStream(in));
	while(parser.hasNext()) {
		Flow flow = parser.next();
		System.out.println(flow);
	}
	System.out.println("Done");
	long endTime = System.nanoTime();
	System.out.println("Took "+(endTime - startTime) + " ns"); 
	}
}
