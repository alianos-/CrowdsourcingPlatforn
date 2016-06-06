1.INTRODUCTION

This library implements a web based crowdsourcing platform. It allows the developer to prepare questions in physical language, and then serve them to the crowd via a web interface. The library is comprised of two parts; the java code which implements the back-end, and an example web application in javascript that implements a basic UI.

In brief the library works as follows. There is a central controller implemented by the Detective class. There is also an abstract Question class that can be extended to create question templates. A few ready-made templates are already provided (for yes/no questions, multiple-choice questions, and single-answer multiple-choice questions). Wherever needed, the developer can instanciate a question template with the actual question in natural language and the possible answers. He then submits the question to the Detective.

The web interface makes an ajax call to the Detective on behalf of the worker, and inquires if there are any pending questions. If there are they are sent to the browser for rendering. The response is sent back to the Detective. When enough answers have been gathered, the Detective informs the registered QuestionObserver object. Any class can extend QuestionObserver to be able to handle responses from the Detective. 

The code contains examples that simplify all this.
 
The library contains the following packages.

a) domain.questions
The core classes.

b) tools
Varius bits and pieces of code that make the library work

c)examples
which contains an example use of the library, and should be removed.

d) api
The api contains 3 Http Servlets that handle the communication between the web and the app. As this library does not implement user management, the code needs to be ammended to account for that. Spesifically, Admin_API is unsafe and placed here only as an example. If administration functionality is needed, it should be either placed behind a login, or else removed. Client_API manages the communication for logged in users, and PublicClient_API the communication of non-logged in users.

On the web side there are the following files.





