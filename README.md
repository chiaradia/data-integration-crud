# API DATA


This application aims to expose a RESTful API to perform CRUD operations with companies data. Besides, it's possible to upload data using a valid CSV file.

| Name (upper case) | Address Zip (five digit text) | Website (lower case) |
| ------ | ------ | ------ |
| COMPANY NAME | 88888 | http://www.site.com |

### Stack
- Docker
- Java 8
- Spring Boot
- MongoDB
- Maven
- Postman

### Set up as a Spring Boot application

You must follow the steps below if you want to run this application only with Maven without containerizing:
- Download and install Java (JDK 8)
- [Download](https://maven.apache.org/download.cgi) and [install](https://maven.apache.org/install.html) the latest version of Maven
- Uncomment [Flapdoodle's](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo) dependency on pom.xml file to run an embbeded MongoDB

```sh
$ cd data-api
$ vim pom.xml
```
- Package and run the application with the following commands:

```sh
cd data-api
mvn package && java -jar target/data-api-1.0.0.jar
```
During the startup of the application, a CSV file located under `src/main/resources` folder will be parsed and its data will be inserted on the database.
##### Endpoints

After the startup, the endpoints beneath will be available listening the port 8080.

| Name | Path | Method | Content-Type | Description |
| ------ | ------ | ------ | ------ | ------ |
| List companies| /v1/companies | GET | application/json | This endpoint aims to retrieve all companies stored in the database. |
| Get company | /v1/companies/{id} | GET | application/json | This endpoint aims to get a single company by its ID. |
| Delete company | /v1/companies/{id} | DELETE | application/json | This endpoint aims to delete a single company by its ID. |
| Update company | /v1/companies/{id} | PUT | application/json | This endpoint aims to update a company by its ID. It's mandatory to send a valid JSON in the body request with the updated informations |
| Create company | /v1/companies/ | POST | application/json | This endpoint aims to create a new company. It's mandatory to send a valid JSON in the body request with these informations: name, zipCode, website |
| Search company | /v1/companies/search?name={value}&zip={value} | POST | application/json | This endpoint provides information based on query parameters values. This resource supports thes parameters: name (part of the company's name) AND zip(the entire zip code of the company) |
| Merge companies | /v1/data/ | POST | multipart/form-data | This endpoints parses a valid CSV file following the structure above and integrate its data with the existent records. If the record doesn't exist, it'll be discarded. The key of the file must be named "csv". The uploaded file cannot be empty or in a different extension. |

##### Tests

To perform tests with Maven, you need to execute the command:
```sh
cd data-api
mvn test
```

Furthermore, it's highly recommended to take a look at the Postman collection. It's possible to test the entire API with it. You just need to download [Postman](https://www.getpostman.com/apps) and import the json file.
 
### Docker

Docker is a Linux container management toolkit with a "social" aspect, allowing users to publish container images and consume those published by others. A Docker image is a recipe for running a containerized process, and in this guide we will build one for a simple Spring boot application.

#### Installation

All the steps to install Docker CE are listed on https://docs.docker.com/install/

#### Containerize It

If you want to run with Docker, execute:

```sh
docker-compose up
```

All the endpoints will be available with the same paths aforesaid. If you didn't change anything, they'll be listening to your requests on port 8080. 

### Todos

 - Write MORE Tests
 - Add Kubernetes support

