### How to run the app
You need
* MongoDB Installed and running on your machine. Tutorial [here](https://docs.mongodb.com/v3.2/tutorial/install-mongodb-on-ubuntu/)
* SBT (mandatory if you don't want to use an IDE)
* IntelliJ to import the project (optional)
* SBT plugin (optional)

Tho ways to run the project:
* Import the project, run SBT and type ```run```  to launch the server.
* cd into the project directory, run SBT and type ```run```  to launch the server or type ```sbt run```.

Then open your favourite browser and go to

```localhost:9000/api-docs```

From the beautiful Swagger-UI interface you can perform the follow operations :
- Create Student
- Find Student by id
- Create Course
- Find Course by id

### How to run the tests
In the tests directory there are tests written with the [ScalaTest](http://www.scalatest.org/) library.  
To launch them just type ```test``` in a running SBT session or simply type ```sbt test```

Enjoy.

Author: [Richard Palomino](www.linkedin.com/in/richard-luis-palomino-felix)