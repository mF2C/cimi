#!/bin/bash -xe

export ss_curl="curl --cookie-jar ~/cookies -b ~/cookies -sS"

# directly create a normal user (username: mf2c, password: mf2c-mf2c)
curl -k \
     -X POST \
     -H "slipstream-authn-info:super ADMIN" \
     -H content-type:application/json \
     -d @mf2c-user.json \
     localhost:8201/api/user

# log into server with the mf2c account
curl --cookie-jar ~/cookies -b ~/cookies -sS \
     -k \
     -X POST \
     -H content-type:application/json \
     -d @mf2c-login.json \
     localhost:8201/api/session

# show the created user via direct access
curl -k \
     -H "slipstream-authn-info:super ADMIN" \
     -H content-type:application/json \
     localhost:8201/api/user 

# try to list user with mf2c account 
curl --cookie-jar ~/cookies -b ~/cookies -sS \
     -k \
     https://localhost/api/user 
