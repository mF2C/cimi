#!/bin/sh -e

workdir=$1
CFG=$workdir/cfgfiles

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

(cd DCCLASSES && jar -xf $workdir/lib/cimi-*.jar) 

cp -r DCCLASSES/CIMI $TMPDIR

function dc_command {
    (cd $workdir && java -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF -cp $workdir/lib/dataclay-*.jar $@)
}

until dc_command dataclay.tool.NewAccount $USER $PASSWORD
do
    echo "Waiting for DataClay to be ready"
    sleep 2
done

dc_command dataclay.tool.NewDataContract $USER $PASSWORD $DATASET $USER
dc_command dataclay.tool.NewNamespace $USER $PASSWORD $NAMESPACE java
dc_command dataclay.tool.NewModel $USER $PASSWORD $NAMESPACE $TMPDIR

rm -fr $TMPDIR DCCLASSES

mkdir -p $STUBSPATH

dc_command dataclay.tool.AccessNamespace $USER $PASSWORD $NAMESPACE
dc_command dataclay.tool.GetStubs $USER $PASSWORD $NAMESPACE $STUBSPATH
