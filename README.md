# Introduction

Take all the customer transactions on Starling bank since a specific date and round them up to the nearest pound.

For example with spending of £4.35, £5.20 and £0.87, the round-up would be £1.58. This amount should then be transferred into a s​ avings goal​, helping the customer save for future adventures.

# Project

## Requirements

* [JDK 11](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Gradle](https://gradle.org)

## Dependencies

I tried to limit to a maximum the number of dependencies to use the bare minimum.

* [Spark](http://sparkjava.com) web application framework
* [slf4j](https://www.slf4j.org): logging library (required by Spark)
* [Jackson](https://github.com/FasterXML/jackson): standard JSON library for Java
* [Junit 5](https://junit.org/junit5/): testing framework (testing dependency only)

## building

The build of the project is managed by Gradle

`
gradle build
`

## running

`
gradle run
`

you should see something like

  > [Thread-0] INFO org.eclipse.jetty.util.log - Logging initialized @319ms to org.eclipse.jetty.util.log.Slf4jLog

  > [Thread-0] INFO spark.embeddedserver.jetty.EmbeddedJettyServer - == Spark has ignited ...

  > [Thread-0] INFO spark.embeddedserver.jetty.EmbeddedJettyServer - >> Listening on 0.0.0.0:8080

  > [Thread-0] INFO org.eclipse.jetty.server.Server - jetty-9.4.18.v20190429; built: 2019-04-29T20:42:08.989Z; git: e1bc35120a6617ee3df052294e433f3a25ce7097; jvm 12+33

  > [Thread-0] INFO org.eclipse.jetty.server.session - DefaultSessionIdManager workerName=node0

  > [Thread-0] INFO org.eclipse.jetty.server.session - No SessionScavenger set, using defaults

  > [Thread-0] INFO org.eclipse.jetty.server.session - node0 Scavenging every 600000ms

  > [Thread-0] INFO org.eclipse.jetty.server.AbstractConnector - Started ServerConnector@5e5197a9{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}

  > [Thread-0] INFO org.eclipse.jetty.server.Server - Started @429ms


The application list on port 8080


# Usage

Note: The token generation is not implemented yet, so you need a user token to speak to the Starling API, and pass it into the request header

## Request

curl -X "PUT" "http://localhost:8080/transactions/roundUp" \
     -H 'Authorization: Bearer boGzM093o9AUnpi77w5jwNZ1L200qg37CUL2oFFYcavoJc1ZDAlT823FrXRpXXNA' \
     -H 'Content-Type: text/plain; charset=utf-8' \
     -d $'{
	"savingGoalName": "christmas",
	"since": "2019-06-14T00:00:00Z"
}'

* savingGoalName: it will send money from the account into the saving goal with the provided name (will create a new one if not found)
* since: it will roundUp all the transactions since the provided date

Note: The application will send money in the saving goal for each account the custome has.



