# Java Full-Stack Learning Workspace

This workspace contains independent Java projects for practicing backend development and core Java concepts. It is a learning portfolio, not one combined application.

My current focus is the Java backend part of full-stack development. These projects cover Java, Gradle, local JSON persistence, Spring Boot, AOP concepts, dynamic proxies, and TCP networking. A frontend and relational database are future learning goals.

## Project Overview

| Project | Focus | Current state |
| --- | --- | --- |
| `TicketBookingSystem/` | Console train-booking application | Implements sign-up, login, train and seat search, booking, booking lookup, and cancellation. Jackson reads and writes local JSON files; BCrypt hashes and checks passwords. Booking and cancellation include rollback handling if a file write fails. |
| `SIngleThreadedWebServer/` | Java TCP client and server | A client sends a line to a server on localhost port `8010`. The server replies to one connection at a time; the `shutdown` message stops it. |
| `MultiThreadedWebServer/` | Server concurrency | Starter placeholder. The server implementation is still to be built. |
| `Spring_Boot_Project1/` | Spring Boot MVC and bean configuration | Java 21 / Spring Boot 4.1.2 project. `GET /user-config` returns the configured `UserConfig` through a controller and service. XML bean definitions are loaded with `@ImportResource`; the service receives its config bean through constructor injection. |
| `spring_boot_proxies/` | Java dynamic proxies | Starter code explores an interface and `InvocationHandler`. Creating proxy instances and delegating calls to the target object remain in progress. |
| `springAOP/` | Spring AOP and REST | Java 21 / Spring Boot 4.1.2 project with AOP and Web MVC dependencies. The `/` endpoint calls `UserService.logIn()`; a Spring-managed `@Aspect` applies `@Before` and `@After` advice to that method. The source compiles with `./gradlew compileJava`; runtime behavior has not yet been verified. |
| `AuthService/` | Spring Boot authentication service starter | Java 21 / Spring Boot 3.2.2 app subproject. `app/build.gradle` matches the supplied dependency file; `gradle/libs.versions.toml` defines its Guava and JUnit aliases. The app source compiles and Gradle resolved its compile, runtime, annotation processor, and test dependency configurations. Authentication, JWT behavior, persistence, and API features remain to be implemented. |

## Skills Practiced

- **Java and object-oriented programming:** classes, objects, interfaces, entities, constructors, getters and setters, methods, and packages.
- **Collections and data flow:** `List` and `ArrayList`, searching a collection, returning results, and changing an object through a reference.
- **Gradle:** project initialization, Gradle Wrapper, Java toolchains, plugins, repositories, dependencies, and project structure.
- **JSON persistence:** mapping JSON data to Java entities and saving updated data with Jackson.
- **Authentication basics:** storing BCrypt password hashes and checking a password against a saved hash.
- **Spring Boot and MVC:** application startup, REST controllers, services, constructor injection, and exposing a configured bean through an endpoint.
- **Spring configuration:** XML bean definitions, property injection, constructor arguments, and importing XML into a Boot application.
- **AOP and proxies:** pointcut expressions, before/after advice around a Spring-managed service, proxy concepts, and the `InvocationHandler` API (the dynamic proxy exercise remains in progress).
- **Networking and I/O:** `ServerSocket`, `Socket`, `BufferedReader`, `PrintWriter`, line-based messages, and try-with-resources.
- **Error handling and state:** handling I/O failures and restoring in-memory booking or seat state when persistence fails.

## Interview Discussion Notes

### Booking cancellation and complexity

Cancellation searches the signed-in user's ticket list for a ticket ID, removes the matching ticket, releases its seat, and saves the updated JSON data. The lookup is a linear scan, so it takes **O(n)** time for `n` tickets in that user's list. If a getter returns the underlying list, changing that list changes the object's in-memory state; writing the updated data is a separate step required to persist it.

### Password handling

The booking project stores a BCrypt hash rather than the plain-text password. At login, BCrypt checks the entered password against the saved hash. Hashing is one-way; the password is not decrypted to log the user in.

### Socket communication

The socket exercise uses raw TCP sockets. `ServerSocket` listens for connections, `Socket` represents a client connection, and the reader and writer exchange newline-terminated text. The server currently handles one client at a time. This exercise is not an HTTP server or a WebSocket application.

### Spring dependency injection

The Spring Boot configuration project uses a controller that receives a service through its constructor. XML defines the `UserConfig` and service beans; `@ImportResource` loads those bean definitions into the Spring application context. This is a way to see how Spring creates beans and supplies their dependencies.

### AOP and proxies

The AOP project registers a Spring `@Aspect` with before and after advice around `UserService.logIn()`, and its REST endpoint invokes that method. Build/runtime behavior still needs verification. The separate proxy exercise is incomplete; it is useful for discussing the purpose of an invocation handler, but is not yet a finished proxy implementation.

## Workspace Structure

```text
JFS/
├── TicketBookingSystem/
├── SIngleThreadedWebServer/
├── MultiThreadedWebServer/
├── Spring_Boot_Project1/
├── spring_boot_proxies/
├── springAOP/
├── AuthService/
│   ├── app/
│   │   ├── build.gradle
│   │   └── src/main/java/authservice/App.java
│   ├── gradle/
│   │   ├── libs.versions.toml
│   │   └── wrapper/
│   ├── gradlew
│   ├── gradlew.bat
│   └── settings.gradle
└── README.md
```

Each Gradle project has its own build files and wrapper. Run Gradle commands from that project's directory rather than from the workspace root.

## Current Learning Goals

- Finish and exercise the Spring AOP advice against a Spring-managed service.
- Complete the dynamic proxy example and compare it with Spring AOP proxies.
- Implement concurrent client handling in `MultiThreadedWebServer/`.
- Build out `AuthService/` with authentication, JWT, MySQL persistence, and REST endpoints.
- Continue from local JSON persistence toward a database-backed service and a frontend client.

## Maintenance

This README is the index for the JFS workspace. Keep the project descriptions, dependencies, implementation status, and practiced concepts aligned with the code as it changes. Work in progress is labeled as such so interview discussions remain accurate.
