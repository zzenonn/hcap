package edu.ateneo.nrg.hcap.io;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Reporter;

import edu.ateneo.nrg.hcap.util.PcapParser;

import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import java.io.DataInputStream;

import org.apache.hadoop.mapred.RecordReader;

/**
 * 
 * @author zenon
 * This InputFormat uses the old mapred API. This is because Hive does not support InputFormats
 * that use the new mapreduce API. I will soon release another branch that uses the new API.
 */

public class PcapInputFormat extends FileInputFormat<LongWritable, ObjectWritable> {
	public static final Log LOG = LogFactory.getLog(PcapInputFormat.class);

	@Override
	public RecordReader<LongWritable, ObjectWritable> getRecordReader(InputSplit split, JobConf config, Reporter reporter)
			throws IOException {
		FileSplit fileSplit = (FileSplit)split;
		Path path = fileSplit.getPath();
		long start = 0L;
        long length = fileSplit.getLength();
		return initPcapRecordReader(path, start, length, reporter, config);


	}
	 public static PcapRecordReader initPcapRecordReader(Path path, long start, long length, Reporter reporter,Configuration conf) throws IOException {
		FileSystem fs = path.getFileSystem(conf);
		FSDataInputStream baseStream = fs.open(path);
		DataInputStream stream = baseStream;
		CompressionCodecFactory compressionCodecs = new CompressionCodecFactory(conf);
		LOG.info("Processing PCAP: " + path.toString());
			
        final CompressionCodec codec = compressionCodecs.getCodec(path);
        if (codec != null)
        	stream = new DataInputStream(codec.createInputStream(stream));
		PcapParser parser = new PcapParser(stream);
		return new PcapRecordReader( baseStream, stream, reporter, start, length, parser);

	 }
	


    /**
    * (Taken from the RIPE hadoop-pcap library. Splits may be incorporated
    * in the future)
    * A PCAP can only be read as a whole. There is no way to know where to
    * start reading in the middle of the file. It needs to be read from the
    * beginning to the end.
    * @see http://wiki.wireshark.org/Development/LibpcapFileFormat
    */
    @Override
    protected boolean isSplitable(FileSystem fs, Path filename) {
        return false;
}

	
	
}