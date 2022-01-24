# iban-validator

Simple rest endpoint to validate IBANs

** **
### How to compile and run the application

-```make compile``` will test/compile the application.

-```make build-jar``` will create to uber jar and test/compile the application

-```make docker-build``` will create the docker image with the tag name ```iban-validator-service```.

-```docker-start``` will start the application in a docker container. 
Note: ```docker-build``` should be run first to create the docker image.

** **

Server port: ```9081```

API: ```/iban```

Http Method: ```POST```

Request example:
``` 
{
    "iban": "AT26 3621 8477 6967 5322"
}
```
Response example:
``` 
{
    "message": "IBAN is valid"
}
```

** **

The main entry of the application is ```com.pfc.RestServer```. 
