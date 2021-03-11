# LUMS
An online license and user management system based on React and SpringBoot.

This is the back end for `LUMS`. The front end can be found at https://github.com/ysz951/lums_react.

## Features
* Security login based on JWT Authentication with auto logout client side.
* Dynamic web page change based on user's role.
* RESTful API services authorization implemented by Spring Boot.
* Data validation on both client-side and server-side.
* Provided with several templates to send email more conveniently.
* Displayed the action logs in real time.

## Demo
* [Live Demo](https://lums-react.vercel.app/)

### Demo user automatically log in
* Click `Demo user login` button on `login` page.

### Demo credentials 
* `User name`: `lums@admin.com`
* `Password`: `admin123`

## Screenshoots
### Home page: 
![image](https://github.com/ysz951/lums/blob/master/demo_images/main_page.jpg)

## Setting Up

- Create development and test databases: `createdb lums`, `createdb lums-test`
- Create database user: `createuser testuser`
- Grant privileges to new user in `psql`:
  - `GRANT ALL PRIVILEGES ON DATABASE "lums" TO testuser`
  - `GRANT ALL PRIVILEGES ON DATABASE "lums-test" TO testuser`
- Create `applictaion.yml` under `/src/main/resources` folder
  - `applictaion.yml` should include: 
    ```yml
      jwtSecret:
        YourSecret
    ```
## Scripts

- Install dependencies: `mvn clean install`
- Run tests: `mvn clean test`
- Start application: `mvn spring-boot:run`
- Package application: `mvn package`

## API Endpotins
### POST `/api/auth/signin`
### POST `/api/auth/signup`

