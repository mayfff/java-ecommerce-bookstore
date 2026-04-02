# java-ecommerce-bookstore

## Environment variables

Application reads local variables from `.env`.

Required variables:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`

Example `.env`:

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=bookstore
DB_USER=postgres
DB_PASSWORD=postgres
```
1. clone the repo
2. create a local '.env' file
3. fill in the database settings
4. create the database using docker compose `docker-compose up -d`
5. start the application using `gradle bootRun`

## Health Check

![img.png](Images/Health%20Check.png)

## Logs Example

~~~
{"timestamp":"2026-03-28T14:06:57","level":"WARN","message":"spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning","logger":"org.springframework.boot.jpa.autoconfigure.JpaBaseConfiguration$JpaWebConfiguration"}
{"timestamp":"2026-03-28T14:06:57","level":"INFO","message":"Starting ProtocolHandler [\"http-nio-8080\"]","logger":"org.apache.coyote.http11.Http11NioProtocol"}
{"timestamp":"2026-03-28T14:06:57","level":"INFO","message":"Tomcat started on port 8080 (http) with context path '/'","logger":"org.springframework.boot.tomcat.TomcatWebServer"}
{"timestamp":"2026-03-28T14:06:57","level":"INFO","message":"Started BookstoreApplication in 2.698 seconds (process running for 2.997)","logger":"kpi.mayfff.bookstore.BookstoreApplication"}
~~~

## Graceful Shutdown

~~~
{"timestamp":"2026-03-28T14:11:37","level":"INFO","message":"Commencing graceful shutdown. Waiting for active requests to complete","logger":"org.springframework.boot.tomcat.GracefulShutdown"}
{"timestamp":"2026-03-28T14:11:37","level":"INFO","message":"Graceful shutdown complete","logger":"org.springframework.boot.tomcat.GracefulShutdown"}
{"timestamp":"2026-03-28T14:11:37","level":"INFO","message":"Closing JPA EntityManagerFactory for persistence unit 'default'","logger":"org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean"}
{"timestamp":"2026-03-28T14:11:37","level":"INFO","message":"HikariPool-1 - Shutdown initiated...","logger":"com.zaxxer.hikari.HikariDataSource"}
{"timestamp":"2026-03-28T14:11:37","level":"INFO","message":"HikariPool-1 - Shutdown completed.","logger":"com.zaxxer.hikari.HikariDataSource"}
~~~