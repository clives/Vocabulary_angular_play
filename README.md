#Vocabulary PlayFramework-AngularJS-AngularFileUpload-Bootstrap-ReactiveMongoDB-Howler Seed Project

## Introduction

Crud application to manage vocabulary Br to Fr. Each words contains his translation to french, some examples of use and his sound.

## Features
Add/Delete/Edit vocabulary. 


## Ingredients
The seed uses the following software components:

1. Play Framework 2.3

2. reactive MongoDB 0.11.0

3. AngularJS 1.3.0

4. Angular_file_upload 3.2.5

## Deployment
Follow these steps in order to deploy the seed on your machine:

1. activator run

2. [http://localhost:9001/vocabulary](http://localhost:9001/vocabulary) 

## MongoDB configuration     

configurating application.conf:
```
mongodb = {
  db = "test"
  servers = [ "localhost:27017" ]
}
```
or defining in your environment: ```MONGOHQ_URL```    
example:   
```set MONGOHQ_URL=mongodb://id:password@serverurl:39251/name```


## Screenshots

### Add
![Add](./screenshots/Screenshot_Add.png)

### All words:
![All](./screenshots/Screenshot_View.png)
