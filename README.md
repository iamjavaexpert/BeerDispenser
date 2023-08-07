
Self-Service Beer Tap Dispenser API
---

 This is the README.md file for the Self-Service Beer Tap Dispenser
 API. The API allows festival organizers to set up self-service beer
 tap counters for attendees to serve themselves, and it keeps track of
 the flow consumption and calculates the total amount of money spent.

Table of Contents
=================

-   Introduction

-   How It Works

-   Workflow

-   Getting Started

    -   Prerequisites

    -   Installation

-   Running the App

-   API Endpoints

    -   Create a Beer Dispenser

    -   Update Dispenser Status

    -   Update a Beer Dispenser

    -   Get a Beer Dispenser

    -   Delete a Beer Dispenser

-   Tests

-   Simplifications

Introduction
============

 The Self-Service Beer Tap Dispenser API is designed to facilitate
 faster service at festivals by allowing attendees to serve themselves
 beer from designated tap counters. The API keeps track of flow
 consumption and calculates the total amount spent by attendees. This
 results in shorter queues at bars and happier festival-goers.

How It Works
============

 The API allows administrators to create dispensers by specifying a
 total volume, flow volume, and price per milliliter (ml) of beer. When
 an attendee opens the tap, the API starts counting the time and
 calculating the money spent based on the flow volume and price per
 milliliter. When the attendee closes the tap, the API stops counting
 and marks the dispenser as closed. At the end of the event, the API
 provides information on dispenser usage, total time open, and money
 made for reporting.

Workflow
========

1.  Admins create a dispenser by specifying the total volume, flow
    volume, and price per milliliter (ml).

2.  Attendees open the tap, and the API starts counting the time and
    calculating the money spent based on the flow volume and price per
    milliliter (ml).

3.  Attendees close the tap, and the API stops counting and marks the
    dispenser as closed.

4.  The API provides information on dispenser usage, time open, and
    money made for reporting.

Getting Started
===============

Prerequisites
-------------

 To run the application, you will need the following:

-   Java JDK (version 11 or higher)

-   Maven

-   Database (e.g., MySQL) with appropriate configurations (username,
    password, port)

Installation
------------

1.  Clone the project in the local repository.

2.  Import the project into your preferred IDE (e.g., IntelliJ IDEA).

3.  Configure the database settings in the application.properties
    file eg. create database, change
    password (src/main/resources/application.properties) with your
    database credentials.

Running the App
===============

1.  Build the project to resolve dependencies.

2.  Run the application using the IDE or use the following command in
    the terminal:

> bash
>
> mvn spring-boot:run

The application should now be running on a local server.

API Endpoints
=============

1.  ### Create a Beer Dispenser

    -   **POST** /addBeerDispenser

    -   Request Body:

> json

>   {
> "totalVolume": 100.0,
>
> "flowVolume": 2.0,
>
> "pricePerML": 0.05
>
> }

-   Response:

    -   Success (HTTP status 200) - The response will contain the
        created beer dispenser's details.

    -   Error (HTTP status 500) - If there is an error during the
        creation process, the response will contain an error message.

    -   Curl :

> curl --location --request POST
> '*http://localhost:8081/addBeerDispenser'* \\
>
> --header 'Content-Type: application/json' \\
>
> --header 'Cookie: GUEST\_LANGUAGE\_ID=en\_US' \\
>
> --data-raw '{ "totalVolume":2000, "flowVolume":10, "pricePerML":5
>
> }'

2.  ### Update Dispenser Status

    -   **PUT** /updateDispenserStatus

    -   Request Parameters:

        -   dispenserId (long) - The unique identifier of the dispenser.
        -   status (enum) - The status of the dispenser.


-   Response:

    -   Success (HTTP status 200) - The response will contain the
        updated beer dispenser's details.

-   Error (HTTP status 500) - If there is an error during the update
    process, the response will contain an error message.

<!-- -->

-   Curl:

> curl --location --request PUT
> '*http://localhost:8081/updateDispenserStatus?dispenserId=5&status=OPEN'*
> \\--header 'Cookie: GUEST\_LANGUAGE\_ID=en\_US'

3.  ### Update a Beer Dispenser

    -   **PUT** /updateBeerDispenser/{dispenserId}

    -   Request Parameters:

        -   dispenserId (long) - The unique identifier of the dispenser
            to be updated.

    -   Request Body:

> json

>  {
> "totalVolume": 120.0,
>
> "flowVolume": 3.0,
>
> "pricePerML": 0.07
>
> }

-   Response:

    -   Success (HTTP status 200) - The response will contain the
        updated beer dispenser's details.

-   Error (HTTP status 500) - If there is an error during the update
    process, the response will contain an error message.

<!-- -->

-   Curl :

> curl --location --request PUT
> '*http://localhost:8081/updateBeerDispenser/4'* \\
>
> --header 'Content-Type: application/json' \\
>
> --header 'Cookie: GUEST\_LANGUAGE\_ID=en\_US' \\
>
> --data-raw '{ "flowVolume":10, "pricePerML":5
>
> }'

4.  ### Get a Beer Dispenser

    -   **GET** /getDispenser/{dispenserId}

    -   Request Parameters:

        -   dispenserId (long) - The unique identifier of the dispenser
            to be retrieved.

    -   Response:

        -   Success (HTTP status 200) - The response will contain the
            beer dispenser's details.

        -   Error (HTTP status 500) - If there is an error during the
            retrieval process, the response will contain an
            error message.

    -   Curl :

> curl --location --request GET '*http://localhost:8081/getDispenser/5'* \\
> --header 'Cookie: GUEST\_LANGUAGE\_ID=en\_US'

5.  ### Get a Beer Dispenser Live Data

    -   **GET** /getLiveData

    -   Response:

        -   Success (HTTP status 200) - The response will contain the
            beer dispenser's live usage details.

        -   Error (HTTP status 500) - If there is an error during the
            retrieval process, the response will contain an
            error message.

    -   Curl :

> curl --location --request GET 'http://localhost:8081/getLiveData/' \
> --header 'Cookie: GUEST_LANGUAGE_ID=en_US'

Simplifications
===============

 To keep the development process manageable, the following
 simplifications were made:

-   **Database**: For this MVP, the MySQL database is used, any other
    ralational database or nosql database or an in-memory database can
    also be used.

-   **Authentication & Authorization**: This MVP does not include user
    authentication and authorization. In a production-ready application,
    secure authentication and authorization mechanisms should be implemented to protect sensitive data and restrict access to certain endpoints.

-   **Error Handling**: Basic error handling is implemented, but in a
    real-world application, more robust error handling and validation would be required.

-   **Logging**: Detailed logging is essential in production
    applications for troubleshooting and monitoring. For simplicity,
    logging is kept minimal in this MVP.
