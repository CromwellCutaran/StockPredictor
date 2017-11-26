#!/bin/sh

mvn package

scp -P 2222 target/CS185-jar-with-dependencies.jar user01@localhost:/home/user01/