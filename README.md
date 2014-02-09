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
This demo has dependencies on *rxjava-core*, *jetty-websocket* and *jetty-server* 
artifacts and their dependencies. [RxJava](https://github.com/Netflix/RxJava/wiki) 
is a great addition to a streaming solution because it simplifies cancelling 
subscriptions and enables functional transformations (and all the other superb 
RxJava goodness like retries, timeouts, caching, multicast, and a heap more). 

The code in this project is super simple and brief thanks to jetty libraries.

* [index.html](https://github.com/davidmoten/websockets-log-tail/blob/master/src/main/webapp/index.html) - calls `ViewerServlet` as an html5 websocket service
* [web.xml](https://github.com/davidmoten/websockets-log-tail/blob/master/src/main/webapp/WEB-INF/web.xml) - defines url for `ViewerServlet`
* [ViewerServlet](https://github.com/davidmoten/websockets-log-tail/blob/master/src/main/java/com/github/davidmoten/websocket/ViewerServlet.java) - implements jetty `WebSocketServlet.doWebSocketConnect` and returns a `StreamWebSocket`
* [StreamWebSocket](https://github.com/davidmoten/websockets-log-tail/blob/master/src/main/java/com/github/davidmoten/websocket/StreamWebSocket.java) - implements jetty `WebSocket`, starts and stops streaming to browser


