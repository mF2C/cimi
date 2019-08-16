#!/bin/bash -xe

# check that the server is responding
curl -k https://localhost/api/cloud-entry-point 

# check number of session templates available; should only be 'internal'
curl -k https://localhost/api/session-template 

# add the jwt session template
curl -k -H 'slipstream-authn-info: internal ADMIN' -H content-type:application/json -d @session-template-jwt.json https://localhost/api/session-template 

# verify that it is there and available anonymously
curl -k https://localhost/api/session-template/jwt

# login with a token
#
# NOTE: This will fail with a 400 unless you provide a valid JWT!
#       Add your token to session-jwt-login.json.
#
curl -k -XPOST -H content-type:application/json -d @session-jwt-login.json https://localhost/api/session
