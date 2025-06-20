###


## Document DB
To enable the replica set (for streams capture), run:

`docker exec -it <container_name> mongosh -u root -p password`

then

`rs.initiate()`


## Execution
To execute:
- Build with Gradle using `./gradlew clean build`.
- Dockerise with `./gradlew dockerBuildImage`.

Then, just do a `docker-compose up`. Everything is in the docker-compose needed to get this working.

## Sample Request
Make a POST to http://localhost:8080/api/v1/template/add

with a body looking something like:
`{
    "templateId": 123,
    "customerId" : 56,
    "documentData": {
        "name": "Chris",
        "address": "123 Bob Street, Bobtown"
    }
}`

You can also run the JMeter tests in the test/resources directory.

Run Grafana via http://localhost:3000, make sure the data source is set to http://prometheus:9000 and there
is a dashboard you can import from https://grafana.com/api/dashboards/14430/ that shows some stuff.

The metrics are at /metrics, and prometheus in the docker compose is set up to scrape from here.


## Notes
Make sure to have:
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key

set as environment variables.