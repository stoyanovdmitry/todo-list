# Summary
This repo contains simple RESTful application, which was created for learning JWT. 
To get a token you need to create POST request to */token/login* with a body like this:
```
{
  'username':username,
  'password':password
}
```
If your request is successful in the response you will find two "Access-Token" and "Refresh-Token" headers which contain these tokens. 
All two tokens starting with the next prefix: "Bearer ".
To access the web application you need to use Authorization header with Access token in your requests, as in the next sample:
```
'Authorization':'Bearer token'
```
Where "token" is yours Access token.
Also to get new Access token if it's expired you need to create POST request to */token/refresh* with Refresh token in Authorization header.
