# Securit-ish API Technical documentation and instructions

This API was written with the following stack, and because of the following reasons:

+ Java (Version 15, as was indicated in the gradle src) -> Java is the perfect language for api rest building, even more when
frameworks like spring exists. If in the future this API needs to be extended, it will be easier due to it.

+ Spring boot -> When we talk about spring boot, we talk about a robust framework, evolved from year to year.
At this day, spring is the first-to-mind framework when we speak about API REST and microservices implementation,
each tool it provees, it takes into account the very best pattern designs and architecture. This is why
in this case we opted for it. Even more when it will gives us the security layer due its spring-security-implementation. 

   + Spring security -> A tool within spring for managing web security, supports lots of options, and comes packed with basic auth out of the box.
 
   + Spring web -> The tool we need for making fast and well designed rest api controllers, including its security.
   + Spring data jpa -> The tool we need for managing database acces, in this case we will be managing a H2 simple database, because
					 We dont need (for now) to give access to the rest of the world to our data. If necessary, spring will handle other db motors we throw at it, and
					 we only will have to manage the properties and drivers for it in the build.gradle file.
   + Spring test -> Provides junit and mockito testing support, we need this to make good tests for our API.
 
   + H2 database driver -> We want a simple database to support our very safebox, and h2 is more than enough for it. This will make possible
   the use of spring data over it.
 
   + Lombok -> Will make our code a lot more readable due its annotations, not having to write the individual constructors, getters and setters (when we dont need to specify some specialty).
   
+ Argon2 hasher for cipher -> Every item in the safebox is encrypted and decrypted using one of the properties password. 
This means the content of the safebox is **unreadable** to hackers, at least until they know we use argon2 and have the property password,
in wich case this password could be API based (provided by the user) when saving or reading items from the safebox.

+ BCrypt encoder is used for the box password encryption, as this is also supported easily in spring security.
And we will be needing this for version 1.0, since we will use JWT with security password verification for JWT.
 
# Architecture and pattern design

+ Hexagonal architecture has been implemented in the project, to provide maintability to the API in the future, 
this makes possible that we can easily replace the framework if needed (hope not, because spring rocks). 

Also this will bring ease of use when reading and adding new use cases to the application (for example, a new DELETE safebox endpoint).

+ DDD Design: 

Application was build from the ground taking domain driven desing in mind, this means
the use cases, domain classes and components were made always taking the whole domain of the API in mind.

+ SOLID design: 

The code takes into account the SOLID principes, for mantaibility in the future. This is very good achieved when using
hexagonal architecture itself, since interface segregation appears almost by itself.

+ Clean code:

There are very few comments in the app, mostly methods and classes, because a great code should serve as comment itself.

# API Usage instructions

+ The api has the endpoints indicated in the v1.api.swagger.yaml. Every API could be accessed trough web (some front end), 
or rest client (postman). If you want to run the application, follow this steps :

+ **Build the gradle app to download the dependencies** -> Make sure you have gradle installed, and run a gradle build task. 
+ **Serve the java application as a springboot APP** -> If neccesary, you could let an **IDE do this for you, like eclipse or intellij**, 
run the application as a spring app. Take in note that you have to select the LOCAL spring profile, for spring to read
the properties from the local environment file. 

If everything is correct, the application will be served at the **url http://localhost:8080/** and it will be accesible
on the: **http://localhost:8080/safebox endpoint (with the same operations specified in the yml file).**

**NOTE: Remember, only a secure password will be considered when generating your safe, use password similar to: S3cur3Passw0rd##** it needs to have numbers, mayusc-minusc, symbols, and to be between 6 and 16 characters long.

Also, remember JWT is implemented for version 1.0, this means you will have to first create the safebox with POST -> /safebox, 
and then open it with the **very same password and id of the safebox**, with this, you will get a valid (but configurable through properties) 
JWT token, that will expire after 3 minutes. Take this JWT token and use it as Bearer token on the header for using the /safebox/{id}/items endpoints, 
but beware with blocking the safebox after 3 fail attempts of opening it.

**IMPORTANT NOTE**: **/safebox/{id}/open endpoint needs a json with the "password" param in the body**, as this is not
especified in the yml especification: 

{
    "password" : "S3cur3Passw0bc##"
}

Id of the safebox and this password will use for JWT generation, so its very important.

# Db2 web access

+ If you want to see the db2 console, theres an endpoint for it that will be served at http://localhost:8080/h2-console
And the credentials for it are (on the properties file):

-user: admin

-password: BBX7yEtsVRqBYVVk

# JWT Auth (Bearer Token) (Version 1.0)

+ As stated before, 1.0 version is implemented in this program, so you have to use a Bearer token
obtained from the /safebox/{id}/open endpoint to call the /safebox/{id}/items endpoints.

It will last 3 minutes, but you can configure it on the properties file through the jwt.token.validity.minutes property.


# Basic auth credentials (Version 0.1) @deprecated

+ Credentials are on the application.properties file, in case theres a need of changing them:

+ Theres basic auth implemented, this means that when you acces to the db2 console, or **use any the endpoint**, you will
need to send a httpheader containing a basic auth, otherwise you will get an **unauthorized error**. 
The user and password for them are (in the properties file):

-user: admin

-password: BBX7yEtsVRqBYVVk

Even if im not arround anymore, it has been a pleasure to leave this api for you (mr CEO), 
so if you need anything else, please do not hesitate to go and submit a issue for me at my personal
[github][githubUrl] :).

# Testing

 Altough i've tested the app a lot when doing it, i've added some simple coverage tests just in case, 
 they are at service and rest layers (could have done for all other classes, but i believe this is enough for this case).

[githubUrl]: https://github.com/droar

