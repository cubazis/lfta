Lightning fast media analysis
=============================

![LFMA](docs/lfsa.gif)

### Abstract

In our days amount of information growth dramatically fast and ability to extract valuable information from it is mission critical for businesses.

This project is an attempt to build a system that can monitor data streams in natural language from social media and give valuable insights to user in real time. 
Real time, horizontal scalability, availability and fault tolerance are mandatory requirements for the system. 

In out case we monitor social media for specific topic, provide sentiment analysis and use heuristics to extract meaning from text to provide reasons for negative events.
As data gathered in system grows we can produce more accurate reasoning.


### Development

To run in dev mode with hot reloading of scala `docker-compose up` and check [localhost:3000](http://localhost:3000)
That command creates 3 docker images (webUI, cassandra storage, kafka messaging queue)


@authors Iskandar Sitdikov, Ivan Grebenkin
