# Integration Tests

To test the integration of the CIMI server with the DataClay backend, 
you'll find in this folder a compose file which references multiple services,
spread throughout this repository, aiming at testing and validating 
different aspects of the data management workflows.

In summary, these tests will: 
 - verify that all relevant endpoints are up, running and reachable
 - test all the basic CIMI operations against DataClay

## Run the integration tests

These tests are deployed through Docker Compose:

```bash
# Make sure the ProjName is the same as the deployed Data Management (CIMI+DataClay) stack
docker-compose -p $ProjName up --build --no-color | grep -v exited
```