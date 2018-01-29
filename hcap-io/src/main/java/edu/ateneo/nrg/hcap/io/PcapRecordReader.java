package edu.ateneo.nrg.hcap.io;

import java.io.DataInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Seekable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TaskAttemptContext;

import edu.ateneo.nrg.hcap.util.PcapParser;

import org.apache.hadoop.mapred.RecordReader;


public class PcapRecordReader implements RecordReader<LongWritable, ObjectWritable> {

	public static final Log LOG = LogFactory.getLog(PcapRecordReader.class);
    
	TaskAttemptContext context;
    Seekable baseStream;
    DataInputStream stream;
    Reporter reporter;
    PcapParser parser;
	
	private LongWritable key = new LongWritable();
    private ObjectWritable value = new ObjectWritable();
    long packetCount = 0;
    long start, end;
    

    
	
	/**
	 * @param context
	 * @param baseStream
	 * @param stream
	 * @param reporter
	 * @param parser
	 */
	public PcapRecordReader(Seekable baseStream, DataInputStream stream, Reporter reporter, long start, long end,
			PcapParser parser) {
		this.baseStream = baseStream;
		this.stream = stream;
		this.reporter = reporter;
		this.parser = parser;
		this.start = start;
		this.end = end;
	}
	@Override
	public boolean next(LongWritable key, ObjectWritable value) throws IOException {
		if (!this.parser.hasNext())
			return false;

		key.set(++packetCount);
		value.set(parser.next());

		reporter.setStatus("Read " + getPos() + " of " + end + " bytes");
		reporter.progress();

		return true;
	}
	@Override
	public LongWritable createKey() {
		return key;
	}
	@Override
	public ObjectWritable createValue() {
		return value;
	}

	@Override
	public long getPos() throws IOException {
		return baseStream.getPos();
	}
	@Override
	public void close() throws IOException {
		stream.close();
		
	}
	@Override
	public float getProgress() throws IOException {
        if (start == end)
            return 0;
        return Math.min(1.0f, (getPos() - start) / (float)(end - start));
	}

    


}