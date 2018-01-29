package edu.ateneo.nrg.hcap.serde;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.Deserializer;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.SerDeStats;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.Writable;

import edu.ateneo.nrg.hcap.util.Flow;

public class PcapDeserializer implements Deserializer {
	public static final Log LOG = LogFactory.getLog(PcapDeserializer.class);
	
	ObjectInspector inspector;
	ArrayList<Object> row;
	int numColumns;
	List<String> columnNames;

	public void initialize(Configuration conf, Properties tbl) throws SerDeException {
		String columnNameProperty = tbl.getProperty(serdeConstants.LIST_COLUMNS);
		columnNames = Arrays.asList(columnNameProperty.split(","));
		numColumns = columnNames.size();

		String columnTypeProperty = tbl.getProperty(serdeConstants.LIST_COLUMN_TYPES);
		List<TypeInfo> columnTypes = TypeInfoUtils.getTypeInfosFromTypeString(columnTypeProperty);

		// Ensure we have the same number of column nameConstantss and types
		assert numColumns == columnTypes.size();

        List<ObjectInspector> inspectors = new ArrayList<ObjectInspector>(numColumns);
        row = new ArrayList<Object>(numColumns);
        for (int c = 0; c < numColumns; c++) {
        	ObjectInspector oi = TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(columnTypes.get(c));
            inspectors.add(oi);
            row.add(null);
        }
        inspector = ObjectInspectorFactory.getStandardStructObjectInspector(columnNames, inspectors);
		
	}

	public Object deserialize(Writable w) throws SerDeException {
		ObjectWritable obj = (ObjectWritable)w;
		Flow flow = (Flow)obj.get();
        for (int i = 0; i < numColumns; i++) {
            String columName = columnNames.get(i);
            Object value = flow.get(columName);
           	row.set(i, value);
        }
        return row;
	}

	public ObjectInspector getObjectInspector() throws SerDeException {
		return inspector;
	}

	public SerDeStats getSerDeStats() {
		return new SerDeStats();
	}

}
