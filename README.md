websockets-log-tail
===================

Follow a stream from a server in an HTML5 compatible browser (like tailing a log file).

Status: *pre-alpha*

This project holds demonstration java source code that uses 
websockets to send a sample stream of 10 lines of text a second
to a browser. 

While viewing the web page scroll to the bottom 
happens automatically but can be paused by hitting the space bar.
Auto scrolling is resumed by hitting the space bar again.

Run the demo
----------------

    git clone https://github.com/davidmoten/websockets-log-tail.git
    cd websockets-log-tail
    mvn test jetty:run
    
Go to [http://localhost:8080](http://localhost:8080).

Technical details
---------------------
This demo has dependencies on *jetty-websocket* and *jetty-server* artifacts and their dependencies. 

