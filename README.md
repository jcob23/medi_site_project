# MEDI SITE PROJECT IR
## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Description](#Description)
* [TO DO](#TO DO)

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
To run this project...

## Description
The application caters to both Patients and Doctors.

Patients can review their medical history, while Doctors can document their descriptions and
update patients' medical history. It allows doctors to declare their availability for appointments and
add notes to completed appointments for patients. Patients can search for doctors by name or specialization,
book appointments, or cancel them if necessary.

Every user can update personal information such as name, surname, phone number, and reset the password via email.

To become a patient user has to register to site, Doctors are added by Admin to ensure security.

## How to use
To use this application, you can access the web app and interact with the web interface or use the REST API.
To Start application you can use Docker Compose and then access the site by link: http://localhost:8190/medisite/

## TO DO
* Improve security
* Enhance web interface clarity and functionality
* Develop a mobile version
* Allow doctors to upload their photos
* Expand the app to different countries by implementing different languages and time zones
* Allow users to receive SMS notifications