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

## Running

[Information on running the
container](https://github.com/slipstream/SlipStreamServer/blob/master/README.md)
can be found in the
[slipstream/SlipStreamServer](https://github.com/slipstream/SlipStreamServer)
repository.

## Contributors

**Contributors to this repository agree to release their code under
the Apache 2.0 license.**

## License

Copyright by various contributors.  See individual source files for
copyright information.  

DISTRIBUTED under the [Apache License, Version 2.0 (January
2004)](http://www.apache.org/licenses/LICENSE-2.0).
