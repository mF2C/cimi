#!/bin/sh -e

printf '\e[0;36m %-15s \e[0m Starting...\n' [EndpointTests]


function test() {
    (ping $1 -q -c 2 > /dev/null 2>&1 && \
        printf "\e[0;36m %-15s \e[32m SUCCESS:\e[0m able to ping $1\n" [EndpointTests]) || \
            printf "\e[0;36m %-15s \e[0;31m FAILED:\e[0m couldn't ping $1\n" [EndpointTests]
}

### Test Docker network
# test ping to cimi, dcproxy, logicmodule1, and traefik
ENDPOINTS=${ENDPOINTS:-"cimi,dcproxy,logicmodule1,proxy"}

for endpoint in `echo $ENDPOINTS | tr ',' ' '`
do
    test $endpoint
done



