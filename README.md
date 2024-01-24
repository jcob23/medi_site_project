# MEDI SITE PROJECT WIP
## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Description](#Description)
* [TO DO](#TO-DO)

## General info
This project is a simple online clinic web app created for the Zajavka course.

## Technologies
The project is developed using:
* Java 21
* Spring boot 3.2.0
* Flyway
* Thymelaf
* PostgreSQL
* Lombok
* Gradle

## Setup
Application uses PostgreSQL database, the schema is called medisite. Application works on port 8190
To Start application you can use Docker Compose and then access the site by link: http://localhost:8190/medisite/

## Description
The application caters to both Patients and Doctors.

Patients can review their medical history, while Doctors can document their descriptions and
update patients' medical history. It allows doctors to declare their availability for appointments and
add notes to completed appointments for patients. Patients can search for doctors by name or specialization,
book appointments, or cancel them if necessary.

Every user can update personal information such as name, surname, phone number, and reset the password via email.

To become a patient user has to register to site, Doctors are added by Admin to ensure security.

Application also provides information about pm10 concentration in the biggest cities of Poland.
It's archived by consuming https://api.gios.gov.pl/pjp-api/swagger-ui/, source: https://powietrze.gios.gov.pl/pjp/content/api


## How to use
To use this application, you can access the web app and interact with the web interface or use the REST API.
Api can by accessed by swagger-ui, you can find medisite-contract.json in project folder


## TO DO
* Improve security
* Enhance web interface clarity and functionality
* Develop a mobile version
* Allow doctors to upload their photos
* Add pm10 information to home page from pjp-api
* Expand the app to different countries by implementing different languages and time zones
* Allow users to receive SMS notifications
* Collect more data from api.gios.gov and display it to the user in more appealing way 
* Test Test Tests!
