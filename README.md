# Room Occupancy Manager - a simple projekt and recruitment task for the SH

# Design:
- using Spring Boot + MVC for the REST support
- using JUnit5 for unit tests and Spring MVC test module for REST unit tests
- No storage needed, simple stateless service
- 2 layers separated on a package level:
  - domain with the business logic
  - rest for the REST API support
- Domain layer implementation design favours easy extensibility (more than 2 type of room type or different occupation calculation algorithm) 
- REST layer is rather written for the specific case (only premium economy) then favours faster development and simplicty 

# Running & testing:
### run tests from CLI with:
  ```./gradlew test```
  
### run application from CLI: 
  ``` ./gradlew bootRun ``` 
and then use `curl` to test a REST API:
``` curl -XPOST http://localhost:8080/occupancy -d'{"freeEconomyRooms":3, "freePremiumRooms":3, "priceOffers":[23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]}' -H "Content-Type: application/json"```
