# Summary
The main purpose of developing this app is improving my Spring framework skills and build RESTful application, but Spring Boot, Vue.js and token authorization is the main target of learning and also I got a view about Heroku platform. This app created with Spring framework for back-end application (MVC, Security, Data and Boot). I used Vue.js as front-end framework with Vue Router and Vuex libs.
# Application
You can look at this application by this link https://todo-std.herokuapp.com (but remember that the app can be in the 'sleep' mode right now, so you'll need to wait until it wake up, it probably takes around 30 seconds)
# Authorization/Authentication
This repo contains simple RESTful application, which was created for learning JWT. 
To get a token you need to create POST request to */token/login* with a body like this:
```
{
  "username":username,
  "password":password
}
```
If your request is successful in the response you will find two "Access-Token" and "Refresh-Token" headers which contain these tokens. 
All two tokens starting with the next prefix: "Bearer ".
To access the web application you need to use Authorization header with Access token in your requests, as in the next sample:
```
Authorization: Bearer $token
```
Where "$token" is yours Access token.
Also to get new Access token (actually to refresh both tokens) if it's expired you need to create POST request to */token/refresh* with Refresh token in Authorization header.