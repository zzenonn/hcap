# Hcap Parser

## Overview
HCAP is a PCAP parser for hadoop. Although it supports several fields, only flow related data is explicitly parsed:

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
