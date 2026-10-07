# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A learning/practice project: a Spring Boot 3.5 (Java 21) SOAP service that accepts account and payment requests, pushes payments through Apache Camel and an Artemis JMS queue, and persists accounts to Oracle via JPA. It is a Maven multi-module build structured as hexagonal (ports & adapters) architecture.

## Commands

No Maven wrapper is checked in; use a local `mvn`. Run from the repo root.

**Build with JDK 21.** On JDK 23+ `javac` no longer runs annotation processors found on the classpath by default, so Lombok is skipped and `AccountEntity`'s getters/setters are missing (compile errors in `AccountMapper`). Prefix commands with e.g. `JAVA_HOME=~/.sdkman/candidates/java/21.0.9-amzn`.

```bash
mvn clean install                                  # build all modules (also generates JAXB classes)
mvn -pl practice-platform -am generate-sources     # regenerate Java classes from the XSDs only
mvn -pl practice-platform -am spring-boot:run      # run the app
mvn -pl practice-platform test -Dtest=ClassName#method   # run a single test (no tests exist yet)
```

Running the app requires external services on localhost (see `practice-platform/src/main/resources/application.properties`):
- Artemis broker at `tcp://localhost:61616` (user/pass `artemis`)
- Oracle at `jdbc:oracle:thin:@//localhost:1521/ee.oracle.docker` (user/pass `practice`)

Locally both run as Docker containers named `oracle` and `artemis` (`docker start oracle artemis`). App listens on port 8081 (`server.port`).

`spring.jpa.hibernate.ddl-auto=validate`, so the `Account` table must already exist in Oracle — Hibernate will not create it.

SOAP endpoints are served under `/ws/*`; WSDLs at `/ws/account.wsdl` and `/ws/makePayment.wsdl` (bean names in `WebServiceConfig`).

## Architecture

### Module boundary (the key rule)

- **`practice-domain`** — framework-free business core. Its `pom.xml` is deliberately dependency-free: no Spring, JPA, JAXB, Camel, or Lombok. Contains records (`model`), use cases (`usecase`), services that dispatch to use cases (`service`), and outbound ports (`port`, e.g. `AccountPort`). Logging uses `java.lang.System.Logger`.
- **`practice-platform`** — the Spring Boot app and all adapters. Anything needing a framework lives here behind a domain port.

Consequences of this split:
- Domain classes have no `@Component`; they are wired as beans manually in `platform/config/DomainConfig`. A new use case/service needs a `@Bean` method there.
- Transactions can't be declared in the domain, so the transaction boundary sits in the adapter (`PaymentQueueHandler` is `@Transactional` around `PaymentService.makePayment`).
- Each adapter has its own mapper between domain records and its wire/storage types (`SoapPaymentMapper`, `AccountMapper`).

### Payment flow

1. `PaymentEndpoint` (Spring-WS) receives `MakePaymentRequest`, maps it to a domain `PaymentCommand`, and calls `producerTemplate.requestBody("direct:payment", ...)` — a request/reply call that waits for a `PaymentResult`.
2. Route `payment-dispatch` (`FromDirectToWaitingPaymentQueue`): `direct:payment` → JSON marshal → `jms:queue:WaitingPayment`.
3. Route `payment-consume` (`FetchWaitingPaymentQueue`): consumes the queue, unmarshals JSON to `PaymentCommand`, invokes `PaymentQueueHandler.handle`.
4. `PaymentService` routes by `bankShortName`: equal to `BankConstants.BANK_SHORT_NAME` ("BMCT") → `OnusPayment` (debits payer, credits beneficiary via `AccountPort`); otherwise → `OffusPayment` (currently a stub returning SUCCESS).

The account read (`AccountEndpoint` → `AccountService` → `GetAccountInfo`) is synchronous and does not go through Camel. `GetAccountInfo` is still a stub returning a hard-coded account; it is not connected to `AccountPort`.

### SOAP contracts (contract-first)

XSDs in `practice-platform/src/main/resources/xsd/` are the source of truth. `jaxb2-maven-plugin` generates request/response classes at build time into `com.spring.soap.platform.adapter.soap.account` and `...soap.payment` (under `target/`, not committed). Each schema has its own execution, its own package, its own `staleFile`, and `clearOutputDir=false` because the two schemas use different target namespaces — keep that pattern when adding a schema. Each XSD also needs an `XsdSchema` bean and a `DefaultWsdl11Definition` in `WebServiceConfig`, injected by `@Qualifier` since there are multiple `XsdSchema` beans.

Namespaces: account = `http://example.com/account`, payment = `http://bmct.com/makePayment`. Endpoint `@PayloadRoot` namespaces must match these.

### Persistence

`AccountPersistenceAdapter` implements `AccountPort` using `AccountJpaRepository` and `AccountEntity`. The entity has no `currency`/`status` columns, so `AccountMapper.toDomain` sets those to `null`.

## Known quirks

- Don't add explicit versions for Spring artifacts managed by the Boot parent: a pinned `spring-data-jpa:4.0.5` (built for Spring Framework 7) previously made startup fail with `NoSuchMethodError` on `RuntimeBeanReference`.
- `OnusPayment` calls `Optional.get()` with no presence or balance checks.
- The old `soap-consumer`/`soap-producer` modules are being replaced by `practice-domain`/`practice-platform`; references to them in git history are to the previous layout.
