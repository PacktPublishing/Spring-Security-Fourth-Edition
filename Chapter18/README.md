# README #

## CAS

Not done


## CAS Server Information ##

### Server project: ###
git clone  -b 7.0 https://github.com/apereo/cas-overlay-template.git chapter18.00-cas-server
gradle createKeystore
gradle build run

### Default URL (SSL Enabled): ###
https://127.0.0.1:9443/cas/


### Default URL (SSL disabled): ###
http://127.0.0.1:9443/cas/


#### Default login Credentials: ####
username: casuser
password: Mellon


### Sections ###

#### 10.00-calendar ####
BASE Line implementation based on chapter08.03


#### 10.01-calendar ####
Implementation using standard X509Configurer:
http.x509().userDetailsService(userDetailsService);

