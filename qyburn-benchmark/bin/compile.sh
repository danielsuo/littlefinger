#!/usr/bin/env bash

cd ../qyburn-common
mvn install

cd ../qyburn-master
mvn install

cd ../qyburn-worker
mvn

cd ../demo
mvn
