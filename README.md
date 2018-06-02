# Hcap Parser

## Overview
Hcap is a PCAP parser for Hadoop. It uses the [Kaitai Struct](http://kaitai.io/) library to create the binary parsers for the packets, and it is also based on RIPE's [hadoop-pcap](https://github.com/RIPE-NCC/hadoop-pcap) library.

Although it supports several fields, only flow related data is explicitly parsed:

```java
  TIMESTAMP
  uTIMESTAMP
  SRCMAC
  DSTMAC
  SRCIP
  DSTIP
  PROTOCOL
  LENGTH
  TTL
  ID
  SRCPORT
  DSTPORT
  LINKTYPE
  ETHERTYPE
  IPVERSION
```
The library, however, may be easily edited to parse other network fields based on different project needs.

## Building
Simple run `mvn clean install` in the project root directory.

## Usage
It may be used either on its own as an InputFormat, or on Hive to analyze the PCAP files in tabular format.

## Additional Tips
    `bash
#!/bin/bash

pcap_files='*.pcap'

tcpdump_cmd='tcpdump'
mkdir splitfiles

for file in $pcap_files
do
	mkdir splitfiles/$file
	$tcpdump_cmd -r $file -w splitfiles/$file/$file.split -C 127
done      
