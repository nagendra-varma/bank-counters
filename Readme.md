
## Getting Started
 The application is developed on spring boot framework and h2 in-memory database is used.
 
Following are the useful commands

###Build and run tests :
````
 mvn install
 ````
 
####Run App : 
````
mvn spring-boot:run
````


Application runs on 9000 port. All REST APIs are available at http://localhost:9000/swagger-ui.html#/



##Usage of Application
* Currently two default counters are added one for premium users and another for non-premium users
* Employees need to be registered inorder to assign employee for the counter
````
POST /staff, body : { name : "emp_name", "role" : "OPERATOR/MANAGER/GENERAL" }
````
* Assign employee to counter. Only one employee can be assigned to one counter
````
PUT /staff/{emp_id}/counter/{counterNo}

Ex: PUT /staff/1/counter/2 -> assigning employee with id 1 to counter 2
````
* If change in assigning employee to counter is required, first employee should be deassigned from counter and then assign another employee to the counter.
````
DELETE /staff/{emp_id}/counter -> removes the counter assigned to the employee id
````
* Customer can be registered at help desk for the first time with Post request to /helpDesk API
````
 POST /helpDesk with customer details {name : "customername", "serviceType": "PREMIUM/NON_PREMIUM", "address" : "customer_address"}
````
* Afterwards customer can request at help desk for the services they need
````
POST /helpDesk/{customerId}/{ACC_BAL_CHECK/ADDRESS_CHANGE/DEMAND_DRAFT}

Ex: /helpDesk/1/ACC_BAL_CHECK -> Returns token details using which token status can be quiried later
````
* Once token is generated, it will be assigned to counter using Round Robbin algorithm.
* Whenever employee at counter is available to serve next token, call following API and then token will be served.
````
PUT /counters/{counterNo}/serveNextToken -> Changes token status to IN_PROGRESS and the requests in token will be served.
````
* Finally employee can change the token status once request is served completely.
````
PUT /counters/{counterNo}/status/{tokenStatus}

Ex: 
/counters/1/status/COMPLETED
/counters/1/status/CANCELLED
````
* Note that only employees whose role is OPERATORS and MANGERS can change the token status to COMPLETED, CANCELLED
