# Testing CIMI

To test CIMI, you'll need:

 - `docker-compose`

## Deploying CIMI with Docker

You'll need to provide CIMI over HTTPS. For this we use the 
Traefik reverse proxy which can automatically request and renew 
Let's Encrypt certificates, given that you have a FQDN.

For local testing however, self signed certificates can be used.

1. go to the `_demo` folder (where this README file sits)

2. generate the self signed certificates

`cd traefik/cert && openssl req  -nodes -new -x509  -keyout server.pem -out server.crt`

3. if using a FQDN, edit `traefik/traefik_https.toml`, changing the $$ placeholders and making sure `docker-compose.yml` points to it

4. Finally, run `docker-compose`

`docker-compose up`

Make sure that you've allocated enough memory to Docker; 4-6 GB of memory 
should be sufficient (this is due to the temporary ES test backend).

After waiting some time for everything to start, you should be able to 
see the CloudEntryPoint at the address `https://localhost/api/cloud-entry-point`.
You'll get the usual browser warning about the security of the certificates, 
if you used the self-signed certificates for the server.

## Using CIMI

The first thing you'll want to do is to create a user so that you can login and manage your system resources.

### Regular User

1. create a regular user `testuser` with password `testpassword` (according to the `addRegularUser.json` file in this directory)

```bash
curl -XPOST -k -H "Content-type: application/json" https://localhost/api/user -d @addRegularUser.json
```

2. an email will be sent to you (if you haven't provided a SMTP server to CIMI, then **you might have to check your SPAM folder**). Copy the API address in that email, and paste it in your browser. Ex: `https://localhost/api/CALLBACK_ENDPOINT`.

3. login as `testuser`, using the `regularUser.json` session template in this directory

```bash
curl -XPOST -k https://localhost/api/session -d @regularUser.json -H 'content-type: application/json' --cookie-jar ~/cookies -b ~/cookies -sS -k
```

 #### API Keys
 
API keys are a safer way to have robots (scripts) interacting with CIMI on behalf of a user, since the same user can issue multiple API keys, and every one of them can be revoked without interfering with the original user access. 

Basically CIMI distinguishes between _internal_ logins and _api_key_ logins, even though they might be associated with the same user account.

Before using API keys, create the session-template for it (only do it once):
```bash
curl -XPOST -H content-type:application/json -d '
{
   "method": "api-key",
   "instance": "api-key",

   "name" : "Login with API Key and Secret",
   "description" : "Authentication with API Key and Secret",
   "group" : "Login with API Key and Secret",

   "key" : "key",
   "secret" : "secret",

   "acl": {
             "owner": {"principal": "ADMIN",
                       "type":      "ROLE"},
             "rules": [{"principal": "ADMIN",
                        "type":      "ROLE",
                        "right":     "ALL"},
                       {"principal": "ANON",
                        "type":      "ROLE",
                        "right":     "VIEW"},
                       {"principal": "USER",
                        "type":      "ROLE",
                        "right":     "VIEW"}]
          }
}' https://localhost/api/session-template -k -H 'slipstream-authn-info: super ADMIN'
```

To create an API key, once you've logged in as `testuser` (step 3 above), do the following:

1. request the creation of an API key:

```bash
curl https://localhost/api/credential -X POST -H 'content-type: application/json' -d @generateAPIKey.json --cookie-jar ~/cookies -b ~/cookies -sS -k
```

you'll get something like

```json
{
  "status" : 201,
  "message" : "credential/4f8b8f66-2e15-4570-a14e-f9d3582425ad created",
  "resource-id" : "credential/4f8b8f66-2e15-4570-a14e-f9d3582425ad",
  "secretKey" : "nehrHa.V9Ppzb.vHf4BG.5vxv3j.DzLtqb"
}
```

2. take the key and secret:

```bash
export CIMI_API_KEY=credential/4f8b8f66-2e15-4570-a14e-f9d3582425ad
export CIMI_API_SECRET=nehrHa.V9Ppzb.vHf4BG.5vxv3j.DzLtqb
```

3. create another session login, `regularUserAPIKey.json`:

```bash
cat >regularUserAPIKey.json <<EOF
{
    "sessionTemplate": {
        "href": "session-template/api-key",
        "key": "$CIMI_API_KEY",
        "secret": "$CIMI_API_SECRET"
    }
}
EOF
```

4. login, same as testuser, but use `regularUserAPIKey.json` instead of `regularUser.json`



## Testing new changes

If you've added new resources to CIMI and want to test them:

1. build the project - go to the root folder and

`mvn clean install`

on the ouput you should see something like 

```
[INFO] DOCKER> [mf2c/cimi-server:1.0.0-SNAPSHOT] "cimi-server": Created docker-build.tar in 171 milliseconds
[INFO] DOCKER> [mf2c/cimi-server:1.0.0-SNAPSHOT] "cimi-server": Built image sha256:596b0
[INFO] DOCKER> [mf2c/cimi-server:1.0.0-SNAPSHOT] "cimi-server": Removed old image sha256:d245c
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO]
[INFO] server parent ...................................... SUCCESS [  0.762 s]
[INFO] server dep ......................................... SUCCESS [ 15.424 s]
[INFO] container .......................................... SUCCESS [  4.195 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

2. find the docker-compose file

`cd _demo`

3. run it

`docker-compose up`

4. after a while you can find your new resources listed at `https://localhost/api/cloud-entry-point`

5. make a PR to merge your new changes to master

6. re-build everything in master, and push the generated Docker Image into the mF2C Registry

```bash
cd container
mvn -Ddocker.username=YOUR_DOCKER_USERNAME -Ddocker.password=YOUR_DOCKER_PWD docker:push
```

# Examples

Examples moved to [https://mf2c-project.readthedocs.io/en/latest/developer_guide/testing.html](https://mf2c-project.readthedocs.io/en/latest/developer_guide/testing.html)
