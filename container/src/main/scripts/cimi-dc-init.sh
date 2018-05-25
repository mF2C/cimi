#!/bin/sh -e

CFG=$1/cfgfiles

mkdir -p $CFG

cat >$CFG/client.properties <<EOF
HOST=logicmodule1
TCPPORT=1034
EOF

USER=cimi
PASSWORD=`cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1`
DATASET=cimids
NAMESPACE=cimins
STUBSPATH="${1}/stubs"

cat >$CFG/session.properties <<EOF
### StorageItf.init variables
Account=$USER
Password=$PASSWORD
StubsClasspath=./stubs
DataSets=$DATASET
DataSetForStore=$DATASET
DataClayClientConfig=./cfgfiles/client.properties
EOF

mkdir -p DCCLASSES
TMPDIR=`mktemp -d`

(cd DCCLASSES && jar -xf $1/lib/cimi-*.jar) 

cp -r DCCLASSES/CIMI $TMPDIR

(cd $1 && java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp $1/lib/dataclay-*.jar dataclay.tool.NewAccount $USER $PASSWORD)

(cd $1 && java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp $1/lib/dataclay-*.jar dataclay.tool.NewDataContract $USER $PASSWORD $DATASET $USER)

(cd $1 && java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp $1/lib/dataclay-*.jar dataclay.tool.NewNamespace $USER $PASSWORD $NAMESPACE java)

(cd $1 && java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp $1/lib/dataclay-*.jar dataclay.tool.NewModel $USER $PASSWORD $NAMESPACE $TMPDIR)

rm -fr $TMPDIR DCCLASSES

mkdir -p $STUBSPATH

(cd $1 && java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp $1/lib/dataclay-*.jar dataclay.tool.AccessNamespace $USER $PASSWORD $NAMESPACE)

(cd $1 && java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp $1/lib/dataclay-*.jar dataclay.tool.GetStubs $USER $PASSWORD $NAMESPACE $STUBSPATH)