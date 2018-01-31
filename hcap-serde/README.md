# Hcap SerDe

## Overview
The hcap SerDe may be used in Hive only for the deserialization of PCAPs. It does not support writing into PCAP files and uses the `HiveIgnoreKeyTextOutputFormat`.

## Usage
First add the library to your classpath.

```
ADD JAR /opt/hivejars/hcap-serde-1.0-SNAPSHOT-jar-with-dependencies.jar;
```
The path to the jar is based on its path on the Edge Node.

### Creating a Table
You may crete a table based on the available fields.

```sql
CREATE EXTERNAL TABLE pcaps (timestamp bigint,
protocol string,
src_ip string,
src_port int,
dst_ip string,
dst_port int,
length int,
ttl int,
id int)
PARTITIONED BY [PARTITIONS]
ROW FORMAT SERDE 'edu.ateneo.nrg.hcap.serde.PcapDeserializer'
STORED AS INPUTFORMAT 'edu.ateneo.nrg.hcap.io.PcapInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
LOCATION '/user/user/input/pcaps;
```

### Conversion
After a table is created, you may also use this to create another table stored in a format that Hadoop can process faster. For example, this converts the data into Parquet.

```sql
CREATE TABLE pcaps_parquet STORED AS PARQUET AS SELECT * FROM pcaps;
```
