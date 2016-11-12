# CS 474: HW3

Written by Kyle Almryde


#### Preamble

This README documents the installation and usage for the cs474 HW3 tasked with using the [Akka](https://akka.io), [SciTools](https://scitools.com)
Understand API, [GitHub Developer API](https://developer.github.com/), and [jGraphT](https://github.com/jgrapht/jgrapht) to extract repositories of
a user defined language and using the listed APIs extract information about a repositories commit history and make general reccommendations


### What is this repository for? ###

* Using the [Scala language](http://www.scala-lang.org/) and Akka Actor system, this application


### Libraries
* [json4s](https://github.com/json4s/json4s) was used to parse JSON like a boss
* [Akka](https://akka.io) was used for the Actor system and HttpRequests
* [SciTools](https://scitools.com) Understand API was used to perform the analysis necessary to generate the dependecy graph
* [jGraphT](https://github.com/jgrapht/jgrapht) was used to produce a dependency graph
* [GitHub Developer API](https://developer.github.com/) was used to query github for open-source projects

### How do I get set up?
To run the application, (assuming you are using Intellij) simply execute the SBT task **'Main'**. You will then be prompted
to enter the name of one of the languages presented to you. If you have trouble entering a valid language name, the program
will simply ask you again until you do (or kill the process by hitting enter)

 **<start the application>**

 **<JVM does some stuff>**

 **----------------------------------------------------------------------------**
 **Hello! From the following list:**
 **ada, assembly, c, c++, c#, fortran, java, jovial, delphi, pascal, pl, m, vhdl, cobol, php, html, css, javascript, python**
 **Please enter Language: \>** *java*

**NOTE: Quotation is not necessary. The application assumes you are passing a string.**

If you would like to run my test application files, make it easy on yourself and type the following at the prompt:

 "./src/main/resources/SMSSync-2.7.udb"

 "./src/main/resources/SMSSync-3.0.5.udb"

####Setting up from IntelliJ ####

1) If no project is currently open in IntelliJ IDEA, click **Import Project** on the Welcome screen. Otherwise, select **File | New | Project from Existing Sources**.

2) In the dialog that opens, select the directory that contains the project to be imported, or a file that contains an appropriate project description. Click **OK**.

3) On the first page of the **Import Project** wizard, select SBT, and click **Next**. (This page is not shown if IntelliJ IDEA has guessed what you are importing.)

4) On the next page of the wizard, specify SBT project settings and global SBT settings, click **Finish**.


#### Development Testing
For testing purposes, I utilized the latest release, ***version 3.0.5*** and ***version 2.7***

*Be aware, these projects are pretty big and will take some time to parse.

To expedite the development, and to test the functionality of the application and the **Understand API** I used a
handful of very small projects I developed in past courses. Those files are used for the actual Unit Tests proper.


#### Unit Testing
For Unit-testing I utilized [ScalaTest](http://www.scalatest.orge) using the [FunSuite](http://doc.scalatest.org/3.0.0/#org.scalatest.FunSuite). Its fun and was surprisingly simple to get it up and running.

Go figure.

To run the tests, in Intellij simplly select the **SBT** task "test" and youll be up and running! Que sera


## Discussion:
### Actor Model
I produced my Actor model using the following something something Concurrency something something complete


### Who do I talk to? ###

* If you have any specific questions contact me via [kyle.almryde@gmail.com](mailto:kyle.almryde@gmail.com)
* If you have any complaints, please direct them to this [Handsome devil](mailto:drmark@uic.edu)

![drmark@uic.edu](https://www.cs.uic.edu/~drmark/index_htm_files/3017.jpg)




