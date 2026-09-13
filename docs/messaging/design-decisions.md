# Messaging Component Design Decisions

## Overview

This document records the main design decisions made for the Cupid Messaging component.

The implementation follows a layered Spring Boot architecture and is designed to satisfy the Week 9 Messaging requirements while keeping the component simple, testable, and easy to integrate with the other Cupid components later.

## Layered Architecture

The Messaging component is separated into:

- Controller layer
- Service layer
- Repository layer
- Model layer
- View layer

This separation was chosen so that HTTP handling, business logic, persistence, data representation, and presentation are not mixed together.

The structure also makes each layer easier to test independently.

## Spring MVC Controller

`MessagingController` handles web requests for the Messaging component.

The controller does not contain persistence logic.

Its main responsibility is to:

- receive request parameters
- call `MessagingService`
- add data to the model
- return the appropriate Thymeleaf view

This keeps business logic out of the web layer.

## Service Layer

`MessagingService` contains the main Messaging business logic.

The service is responsible for:

- creating messages
- sanitising input
- saving messages
- retrieving all messages
- retrieving conversation history
- building message thread lists
- providing encouragement configuration
- applying persistence timeouts
- converting persistence failures into Messaging-specific errors

Keeping this logic in the service layer avoids placing business logic directly inside controllers or repositories.

## Repository Pattern

`MessageRepository` extends:

`JpaRepository<Message, Long>`

Spring Data JPA was selected because it provides standard persistence operations without requiring SQL to be written manually.

Derived repository methods are used for:

- retrieving conversations between two users
- finding messages involving one selected user
- ordering messages by sent time

This keeps database access simple and consistent with the layered architecture.

## H2 Database

H2 was selected as the current database for the Messaging component.

Reasons include:

- lightweight setup
- no separate database installation required
- integration with Spring Boot
- suitable for development and automated testing
- easy to reset during testing

The current H2 configuration uses an in-memory database, meaning data is reset when the application restarts.

This is acceptable for the current development stage.

## Message Entity

The persistent model currently contains one entity:

`Message`

The entity stores:

- `id`
- `sender`
- `receiver`
- `content`
- `sentAt`

The `id` is automatically generated.

`sentAt` records when the message was created.

## Sender and Receiver as Strings

The sender and receiver are currently stored as `String` usernames instead of foreign keys to a User or Profile entity.

This decision was made because the Messaging component is being developed independently from the Profile and Matching components.

Using String values reduces coupling during Week 9 development.

Future group integration may replace these fields with relationships to a shared user model.

## Conversation History

A conversation is defined as all messages exchanged between two users in either direction.

For example:

- Safwan → Alex
- Alex → Safwan

Both belong to the same conversation.

The repository retrieves both directions and orders them by `sentAt` ascending so that the conversation is displayed chronologically.

## Message Threads

Threads are built from all messages involving a selected user.

For each message:

- if the selected user is the sender, the receiver becomes the conversation partner
- if the selected user is the receiver, the sender becomes the conversation partner

A `LinkedHashSet` is used to remove duplicate conversation partners while keeping the order in which they are encountered.

Because messages are retrieved newest-first, the thread list reflects recent conversations first.

## Input Sanitisation

Input sanitisation was separated into a dedicated `MessageSanitizer` component.

This class sanitises:

- sender
- receiver
- message content

Unsafe characters are converted into safe text values before persistence.

Separating sanitisation from `MessagingService` keeps the sanitisation responsibility reusable and independently testable.

## Encouragement Feature

The requirement to encourage users to start conversations is controlled through:

`messaging.encouragement.enabled`

This was implemented as a configuration property rather than hard-coding the feature.

This allows the feature to be turned on or off by changing one setting without modifying Java code.

## Persistence Timeout

The persistence timeout is configured using:

`messaging.persistence.timeout.seconds`

The default value is five seconds.

Persistence operations are executed through an asynchronous wrapper using `CompletableFuture`.

If an operation exceeds the configured timeout, the service raises a `MessagingException`.

This design prevents messaging requests from waiting indefinitely when persistence is unavailable or excessively slow.

## Custom Messaging Exception

`MessagingException` was introduced so Messaging-related failures can be represented using a component-specific exception.

This keeps persistence implementation details away from the controller and user interface.

The service converts timeout and data-access problems into `MessagingException`.

## Global Exception Handling

`MessagingExceptionHandler` uses Spring `@ControllerAdvice`.

It catches `MessagingException` and returns the Messaging error page.

This was selected instead of putting repeated try/catch blocks inside each controller method.

The approach centralises user-facing Messaging error handling.

## Thymeleaf Views

The Messaging component uses Thymeleaf for server-side rendering.

The current pages are:

- `messages.html`
- `conversation.html`
- `threads.html`
- `error.html`

Thymeleaf was selected because it integrates directly with Spring MVC and allows controller model values to be displayed without requiring a separate front-end framework.

## User Interface Design

The interface was kept simple but structured using cards, forms, message sections, and consistent page styling.

The focus is on:

- clarity
- easy demonstration
- easy navigation
- maintaining code that can still be explained during assessment interviews

## Testing Strategy

The implementation uses several kinds of tests.

### Unit Tests

Used for:

- sanitisation
- Messaging service behaviour

### Repository Integration Tests

Used to verify:

- JPA persistence
- conversation queries
- thread queries

### Controller Tests

Used to verify:

- controller routes
- returned views
- model attributes

### Timeout Test

A mocked repository intentionally delays persistence longer than five seconds.

The test confirms that a controlled `MessagingException` is raised.

### Performance Test

The performance test simulates 100 simultaneous users sending messages.

This provides evidence that the Messaging component can handle concurrent use in the assessment test scenario.

## Current Limitations

The current implementation does not yet include:

- authentication
- Profile entity integration
- Match entity integration
- permanent external database storage
- unread message status
- message deletion
- file attachments

These were intentionally kept outside the current Week 9 Messaging scope.

The design leaves room for these features to be added during later group integration.