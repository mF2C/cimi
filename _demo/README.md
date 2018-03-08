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

2. login as `testuser`, using the `regularUser.json` session template in this directory

```bash
curl -XPOST -k https://localhost/api/session -d @regularUser.json -H 'content-type: application/json' --cookie-jar ~/cookies -b ~/cookies -sS -k
```


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



