#!/bin/bash -xe

# $1 must be the desired version
# $2 is the next version

mvn -DskipTests versions:set -DnewVersion=$1 -DgenerateBackupPoms=false
sed -i "" 's/(def +version+ .*/(def +version+ "'$1'")/g' server/project.clj
sed -i "" 's/(defproject eu.mf2c-project.cimi\/server.*/(defproject eu.mf2c-project.cimi\/server "'$1'"/g' server/project.clj

mvn clean install

git tag $1

docker push mf2c/cimi-server:$1

###

mvn -DskipTests versions:set -DnewVersion=$2 -DgenerateBackupPoms=false
sed -i "" 's/(def +version+ .*/(def +version+ "'$2'")/g' server/project.clj
sed -i "" 's/(defproject eu.mf2c-project.cimi\/server.*/(defproject eu.mf2c-project.cimi\/server "'$2'"/g' server/project.clj

mvn clean install

docker push mf2c/cimi-server:$2
sed -i "" 's/    image: mf2c\/cimi-server.*/    image: mf2c\/cimi-server:'$2'/g' _demo/docker-compose.yml

git add _demo/docker-compose.yml _demo/README.md server/pom.xml server/project.clj container/pom.xml pom.xml
git commit -m "release $1"

echo "please do a git push"