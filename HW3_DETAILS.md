## Homework 3: Instructions

### Technologies To Be Used
* [Akka.io](http://akka.io/)


will want to use __(un)become__ in order to "reset" the Actors state at startup
or awakend


### Github API
* https://help.github.com/articles/searching-repositories/
  Gives details of search string syntax for the __q__ parameter


### Actor Model

* __Actor: Request Github Projects__
    Calls the Github Restful API performing search queries on some language(s)

* __Actor: <Language> List manager__
  + Language specific Actor which wraps functionality of generic list handling
    Should keep track of projects queried in the specified language
    Make approximately 7-10 classes that do this (which ever ones are supported by understand)

* __Actor: Extract Contents__
   + Responsible for downloading the contents of the provided repository

* __Actor: .udb Builder__
   + Responsible for generating the Understand .udb

* __Actor: Understand Parser__
   + Performs dependency analysis on the selected *.udb project


* Actor: Check Content Type
* * Actor: Get File Contents
* * Actor: Get Directory Contents

* Actor: Understand Actor

                           Mine_Github-Actor
                    /         |         |         \
          Java-Actor   Python-Actor   C++Actor    Perl-Actor
                    \                             /
                             |          |
                        content_handler-Actor(s)
                                  ||
                             Understand API



In your Scala program, you will create multiple actors that will handle each other's requests by sending and processing messages, which will be desribed using case classes. For example, one actor will handle the list of public repositories of projects written in Java, some other actors will obtain the content of a file or a directory in a repository, and other actors will automatically analyze the relationships among types in the obtained software application using the Application Programming Interface (API) of the tool called Understand (https://scitools.com/non-commercial-license/), a static code analysis tool that supports many programming languages and it is used by many Fortune 500 companies. Those of you who completed homework 2 (yes, a few students did not!) know how to use this tool already. If you haven't yet, please apply for a non-commercial license immediately, install the tool, and investigate its IDE and its API libraries. You will use the latest community version of IntelliJ IDE for this assignment.


### Github Integration
Input to application is a language which is used directly in the querying of Open-Source projects (and streaming). A way to mitigate the rate limit, utilize recursive calls such as  `GET tree recursively`









##### Actor roles:
* Some actors will obtain the content of various Java open-source
* Other actors will use __Understand API__ to process the content + patches of OSS, generate dependency graphs

As some actors obtain the content of various Java open-source projects, the other actors in your program will use the Understand API calls to process the content of the obtained software applications and their patches and construct the dependency graphs.