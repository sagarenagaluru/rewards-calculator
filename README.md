# Rewards Calculator

A retailer offers a rewards program to its customers, awarding points based on each recorded purchase.

Pre-requisites : Install Java version 8 or above and install Maven

# Commands to run application
**c:\<dir-path>:** mvn clean install

**c:\<dir-path>:** mvn spring-boot:run

# Accessing REST Endpoints

Get calculated rewards by accessing URL http://localhost:8123/customers/{id}/calculate by passing CustomerId

E.g. http://localhost:8123/customers/1/calculate

To save transaction access URL http://localhost:8123/customers/saveTransaction
```json
{
    "itemName" : "e.g. ItemName",
    "transactionDate" : "e.g.2021-12-27",
    "totalAmount" : 120,
    "customerId" : 2
}
```


# Access Swagger

Enabled Swagger to provide more details about REST endpoints, access http://localhost:8123/swagger-ui.html#/

# Additional Information
* Used h2 in memory database (JDBC URL : jdbc:h2:mem:testdb, User Name : sa)
* Database pre-loaded with one year transactions information
* Three Customers ID’s  (1,2,3)are available to validate with different information
* Provided URI “/customers/saveTransaction” to add transaction details if any one wish to validate logic
* As we have high level requirement on decimal values in transaction amount, we consider to round off (ceil/floor when > or < 0.5 ) to nearest number


