Webserver:
-has multithreading architecture
-answers to HTTP-get requests
-downloads initial config from xml
-writes logs
-has some specific API


API-1: Return file size
API-2: Return MD5 hash string based on the file content
API-3: in progress...
API-4: Print server runtime statistics



examples of calling APIs:

http://localhost:8080/api-1?file=c:/index.html
http://localhost:8080/api-2?file=c:/index.html
http://localhost:8080/api-4

