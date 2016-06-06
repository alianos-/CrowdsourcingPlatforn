Author          : Andreas Lianos
Library version : 1.0


1.INTRODUCTION
---------------
This library implements a web based crowdsourcing platform. It allows the 
developer to prepare questions in physical language, and then serve them to the 
crowd via a web interface. The library is comprised of two parts; the java code 
which implements the back-end, and an example web application in JavaScript 
that implements a basic UI.

In brief the library works as follows. There is a central controller implemented 
by the Detective class. There is also an abstract Question class that can be 
extended to create question templates. A few ready-made templates are already 
provided (for yes/no questions, multiple-choice questions, and single-answer 
multiple-choice questions). Wherever needed, the developer can instantiate a 
question template with the actual question in natural language and the possible 
answers. He then submits the question to the Detective.

The web interface makes an ajax call to the Detective on behalf of the worker, 
and inquires if there are any pending questions. If there are they are sent to 
the browser for rendering. The response is sent back to the Detective. 
When enough answers have been gathered, the Detective informs the registered 
QuestionObserver object. Any class can extend QuestionObserver to be able to 
handle responses from the Detective. 

# The code contains examples that simplify all this. #

2. CONTENTS
------------
On the java side the library contains the following packages.

a) domain.questions
The core classes.

b) tools
Varius bits and pieces of code that make the library work

c)examples
which contains an example use of the library, and should be removed.

d) api
The api contains 3 Http Servlets that handle the communication between the web 
and the app. As this library does not implement user management, the code needs 
to be amended to account for that. Specifically, Admin_API is unsafe and placed 
here only as an example. If administration functionality is needed, it should be 
either placed behind a login, or else removed. Client_API manages the 
communication for logged in users, and PublicClient_API the communication of 
non-logged in users.


On the web side there are the following files.
Please note that because these files are offered within a demo UI, they might
need steps to adjust them to your website.

cp.js contains a few example calls to the java server. They would need to be
      amended to properly match your code.
questionHandling.js is used to render the questions based on their properties.
      You should include this file in your code, and amend based on any 
      extensions that are required.

The remaining web files implement the demo UI and you should remove them.



3. USAGE
--------------
The server side requires google's gson. Version 2.6.2 was used.
The client side requires jQuery. Version 1.8.0 was used.

To use the library download the java source files and add them to your code.
Have a look at the test question created in Admin_API. This sets AppleCounter
as the observer where the example continues. Once you are confident you
understand how to use the Questions and the Detective you can remove all
the example files.

The Detective is built as a singleton, which means you can always access it 
through Detective.SHERLOCK

You can look at Q_* classes to see how to extend the Question class to handle
any type of question.

The web interface communicates with the server via the api files. 
In your web.xml configuration you will need to add proper configuration for
Client_API and PublicClient_API. Alternatively you can transfer those functions
to your own http servlet.

example configuration

 <servlet>
     <servlet-name>Client_API</servlet-name>
     <servlet-class>api.Client_API</servlet-class>
 </servlet>
 <servlet-mapping>
     <servlet-name>Client_API</servlet-name>
     <url-pattern>/Client_API</url-pattern>
 </servlet-mapping>


The client side depends a lot the specific implementation. The included files
build a demo UI site. If you have your own website you can look at cp.js on
how to make calls to the above servlets. The most important file is
questionHandling.js. This file takes the json formatted question returned by 
the server, and renders it with the proper options in the browser. You should
include this file as is, but you will need to amend the ajax call that 
pushes the answers to the server.


