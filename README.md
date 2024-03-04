[![LinkedIn][linkedin-shield]][linkedin-url]
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


## Table of Contents

* [About the Project](#about-the-project)
    * [Built With](#built-with)
* [Getting Started](#getting-started)
    * [Prerequisites](#prerequisites)
    * [Installation](#installation)
* [Usage](#usage)
* [License](#license)
* [Contact](#contact)
* [Acknowledgements](#acknowledgements)

## About The Project
A payment card number, primary account number (PAN), or simply a card
number, is the card identifier found on payment cards.
The PAN is an 8 to 19 digit number displayed on one side of the card.
The first 6 digits of a payment card number (credit cards, debit cards, etc.) are
known as the Issuer Identification Numbers (IIN), previously known as Bank
Identification Number (BIN). These identify the institution that issued the card
to the card holder.

This project is REST API that exposes endpoints that support full CRUD operations in 
the database. It also exposes one endpoint that calculates the clearing cost for payment cards
by utilizing the external API of [binlist](https://binlist.net/) from which the user 
can retrieve the country this card was issued from, among many other information.

### Built With

* [Java 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)
* [Docker](https://www.docker.com/)
* [Docker compose](https://docs.docker.com/compose/)
* [Redis](https://redis.io/)

## Getting Started

### Prerequisites
This application utilizes docker and docker compose for deployment. 
Install [Docker](https://docs.docker.com/get-docker/) and
[Docker Compose](https://docs.docker.com/compose/install/)
on your machine if you haven't done already.
You can use any platform like [Postman](https://www.postman.com/downloads/) or [Insomnia](https://insomnia.rest/download) to send requests.


### Installation

1. cd into the directory of the project

2. Install the application
```sh
mvn clean install
```
3. Build the card-cost-api image
```sh
docker build -t card-cost-api:latest .
```
4. Run docker compose to start the containers
```sh
docker docker-compose up -d
```

## Usage
This application exposes a number of REST endpoints one of which 
performs a GET request to [binlist.net](https://binlist.net/) and retrieves
the country and the cost for a given IIN.
The rest of the endpoints perform CRUD operations in the database of the Clearing Cost matrix.
Bellow are example requests for each of the endpoints:

**GET** request for fetching all countries and costs:
```sh
http://localhost:8087/cost-clearance/get-records
```

**GET** request that returns the clearing cost and the country using country as input:
```sh
http://localhost:8087/cost-clearance/get-by-country/GR
```

**POST** request that adds a new country and cost in the database:
```sh
http://localhost:8087/cost-clearance/DK/20
```

**PUT** request that updates the cost for a specified country:
```sh
http://localhost:8087/cost-clearance/GR/20
```

**DELETE** request that deletes a record from the database using a country as input:
```sh
http://localhost:8087/cost-clearance/US

```
**GET** request to [binlist.net](https://binlist.net/) external API that gets country and cost for 
specified given card:
```sh
http://localhost:8087/cost-clearance/516732
```

In order to eliminate the possibility that a user can request the same card again and again,
the results fetched from [binlist.net](https://binlist.net/) are cached in Redis which runs on a separate container.
If a user requests the same card, the results will be fetched from Redis cache thus making 
the API free from unnecessary traffic. 

## License

Distributed under the Apache 2.0 License. See `LICENSE` for more information.


## Contact

Stefanos Anastasiou - emperor_stef@yahoo.gr

## Acknowledgements
* [Img Shields](https://shields.io)
* [Choose an Open Source License](https://choosealicense.com)

[license-shield]: https://img.shields.io/github/license/saltstack/salt
[license-url]: https://choosealicense.com/licenses/apache-2.0/
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=flat-square&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/stefanosanastasiou/
