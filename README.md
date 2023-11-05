# Hello!

If you're reading these words you're most likely taking a part in my interview process.  
I was tasked with writing some code to introduce myself. Here it is. 

I decided to write a small app that would solve some of the issues I stumbled upon recently. 
Even if I won't succeed in the interview I hope to learn what you think about the ideas I had for solving them and learn something new.

## How to run

Requirements: Java 17, PostgreSQL.

### Gradle / IDE
To run it locally you need to set up the following env variables to configure the PostgreSQL connection:
`SPRING_DATASOURCE_URL=jdbc:postgresql://HOST:PORT/DATABASE;`
`SPRING_DATASOURCE_USERNAME=USERNAME;`
`SPRING_DATASOURCE_PASSWORD=PASSWORD;`

and then `gradle bootRun` should be enough.

By default it listens on port 8080.
[http://localhost:8080/swagger-ui/index.html](http://localhost:8888/swagger-ui/index.html).

### Docker compose

The app image is build using [jib](https://github.com/GoogleContainerTools/Jib).

By default it listens on port 8088.
[http://localhost:8888/swagger-ui/index.html](http://localhost:8888/swagger-ui/index.html).

`gradle jibDockerBuild && docker-compose up`

## What it does?

The APP imitates two services placed in packages `billing` and `offering`. They are meant to model a part of a larger platform.

The goal of `billing` is to demonstrate a downstream system receiving events and providing some API endpoints with aggregated view of those.

The `offering` on the other thing is playing a role of a service storing configuration of services provided by the platform.

The domain has the following concepts:
* *Customer* - more often known as Tenant. Umbrella entity for data segregation and being a payer.
* *Offering* - a service provided by the platform. It may or may not be offered to a given Customer.
* *User* - a concrete user, a person.
* *Subscription* - a declaration of usage of a given Offering by a given User within a Customer.
* *Billing* - we bill for active subscriptions per second.
* *SKUs* - not yet complete idea of having different price components related to offerings.

Using this example as an example I wanted to distill and show some problems I recently stumbled upon such as:
1. Atomically publishing changes to the database and other services (e.g. via MessageBroker) that don't support 2PC. Here played by HTTP calls and showcasing a toy implementation of the Transactional Outbox Pattern and the db-based job scheduling.
2. An idea for domain design, read and write separation, a bit towards CQRS.
3. Aggregating responses from multiple APIs.
4. Idempotency of downstream services.
5. A solution using locks for a real-life case when 2 concurrent transactions insert 2 different objects causing a race condition.
6. Avoiding N+1 queries with FETCH JOIN. Entity graphs or custom queries could also be used.
7. Implementing logic in PostgreSQL.

I hope this will generally show the ideas I'm learning and playing with recently. 
Not all of these solutions are the best possible and multiple alternatives exist for each e.g.:
1. DB Polling is not parallel and if we want to maintain order of messages it can't be. In our case maintaining order wouldn't matter in the long run as long as no message would fail to be sent. 
In real life some solutions like Debezium or message brokers supporting transactions (e.g. Kafka) would be preferable. 
2. A fully-fledged solution could potentially store write and read models in different forms, although quite often for a price of eventual consistency.
3. GraphQL could be a solution for federating APIs and providing graceful degradation on API level.

and so on... :)

## Why?

As an exercise it helped me to think about those problems and solve them by hand, discuss pros and cons, alternatives etc. 

## What I didn't manage to do, but I know is lacking :)

* A lot of tests, some of them even potentially tricky :)
* DB Migrations and indices to optimize queries.
* Some API endpoints, for CRUDs for example. Direct DB access can be used for these.
* Observability.
* Logging.
* Proper commits with messages and stages ;)

Thank you for your time and talk to you soon!
