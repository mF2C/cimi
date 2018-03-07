# General Documentation
 - http://ssapi.sixsq.com/#cimi-api
 - https://repository.atosresearch.eu/owncloud/index.php/apps/files/?dir=%2FmF2C%2FWorking%20Folders%2FWP4%20mF2C%20Gearbox%20block%20design%2FD4.7%2FCIMI_UserGuide_Demo

# mF2C CIMI Server

The CIMI server for the mF2C project.  This project compiles the mF2C
resources (and support code) and then generates a Docker container for
running the service.

## Building

This project uses Maven to control the overall build and Leiningen to
build the clojure-specific resources.  See the [Maven
documentation](https://maven.apache.org/install.html) and [Leiningen
documentation](https://leiningen.org/#install) for installing these
tools.

This build will also create a Docker container for running the CIMI
server.  Consequently, you must have Docker installed; see the [Docker
documentation](https://docs.docker.com/install/) for that. 

Once these tools are installed, you can build the full project
(including unit tests) by running:

```bash
mvn clean install
```

in the top-level directory.

You can turn off the tests or container build by adding the options
`-DskipTests` and `-DskipContainers`, respectively.

### Push to the mF2C Docker Registry

Once the project has been successfully built, you'll be able to push 
the created Docker image into the public mF2C Docker Registry:



## Running

Have a look at the `_demo` folder for an example on how to run the CIMI server.

## Contributors

**Contributors to this repository agree to release their code under
the Apache 2.0 license.**

## License

Copyright by various contributors.  See individual source files for
copyright information.  

DISTRIBUTED under the [Apache License, Version 2.0 (January
2004)](http://www.apache.org/licenses/LICENSE-2.0).
