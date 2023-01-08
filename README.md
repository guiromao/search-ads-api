Search Ads API
---

An API to search Ads according to 'text', priceFrom, and priceTo.

Endpoint: GET /search
---

Parametres:
----
- query: the text to query for (mandatory)
- priceFromEuro: the base price in Euro (not mandatory)
- priceToEuro: the upper limit price in Euro (not mandatory)

Example: GET /search?query=yourtext&priceFromEuro=123&priceToEuro=321

Exceptions:
- Empty text query
- Negative prices
- 'priceFrom' higher than 'priceTo'

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
