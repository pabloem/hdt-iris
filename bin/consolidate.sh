#!/bin/bash
java -classpath './:bin/:./lib/:../lib/*:../conf/:./conf/:./Java-BloomFilter/dist/*:../Java-BloomFilter/dist/*' consolidate $*
