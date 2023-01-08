Search Ads API
---

An API to search Ads according to 'text', priceFrom, and priceTo.

GET /search?query=yourtext&priceFromEuro=123&priceToEuro=321

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/searchAdsApi-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
