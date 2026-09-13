# Messaging Component Requirements

## Overview

The Messaging component supports communication between users of the Cupid dating application.

The implementation is designed around the Week 9 Messaging requirements for COIT13235 Enterprise Software Development.

## Functional Requirements

### FR_Messages

The system shall allow one user to send a message to another user.

The current implementation provides:

- sender input
- receiver input
- message content input
- message persistence
- display of stored messages

Messages are submitted through:

`POST /messages/send`

## FR_Messages_History

The system shall allow users to retrieve conversation history between two users.

The implementation retrieves messages in both directions:

- user1 → user2
- user2 → user1

Conversation history is ordered by the message timestamp.

The conversation page is available through:

`GET /messages/conversation`

Required parameters:

- `user1`
- `user2`

## FR_Messages_Threads

The system shall allow a user to retrieve their existing message threads.

A thread is identified by the other user involved in a conversation.

The implementation:

- retrieves messages where the selected user is either sender or receiver
- orders messages by most recent first
- removes duplicate conversation partners
- presents each conversation partner as one thread

Threads are available through:

`GET /messages/threads`

Required parameter:

- `user`

## Input Sanitisation

All user-supplied messaging values shall be sanitised before persistence.

The following fields are sanitised:

- sender
- receiver
- message content

The sanitiser escapes potentially unsafe HTML characters and trims surrounding whitespace.

## User Encouragement

The system shall support an optional feature that encourages users to begin conversations.

The feature is controlled using one setting:

`messaging.encouragement.enabled`

When the setting is `true`, an encouragement message is displayed.

When the setting is `false`, the encouragement message is disabled.

## Persistence

Messages shall be persisted using Spring Data JPA.

The current implementation uses an H2 database.

Each persisted message contains:

- automatically generated identifier
- sender
- receiver
- content
- sent timestamp

## Persistence Failure Handling

The Messaging component shall fail gracefully when a persistence operation cannot be completed successfully.

Database access failures are converted into a `MessagingException`.

A global Messaging exception handler displays a user-friendly error page instead of exposing internal application errors.

## Persistence Timeout

Persistence operations shall not be allowed to wait indefinitely.

The timeout is configured using:

`messaging.persistence.timeout.seconds`

The current configuration is:

`5`

If a persistence operation exceeds this limit, the Messaging component raises a controlled `MessagingException`.

## Message Display

The main Messaging interface shall display stored messages.

For each message, the interface displays:

- sender
- receiver
- content
- time sent

## Conversation Display

The conversation interface shall display messages exchanged between two selected users.

Messages are shown in chronological order.

## Thread Display

The thread interface shall display the conversation partners associated with a selected user.

Selecting a thread opens the corresponding conversation history.

## Error Display

If a controlled Messaging error occurs, the system shall display a dedicated Messaging error page containing a user-friendly error message.

## Non-Functional Requirements

### Maintainability

The component uses a layered architecture:

- Controller
- Service
- Repository
- Model
- View

Additional responsibilities such as sanitisation and exception handling are separated into dedicated classes.

### Security

User-supplied messaging text is sanitised before storage.

### Reliability

Persistence failures and timeout conditions are handled through controlled exceptions.

### Testability

The controller, service, repository, sanitiser and timeout behaviour can be tested independently.

### Performance

The Messaging component includes a performance test simulating 100 simultaneous users sending messages.

## Current Implementation Limitations

The current Messaging component stores sender and receiver as String usernames.

It does not currently use foreign-key relationships to a shared User/Profile entity.

This keeps the component independent while other Cupid components are still being developed.

Future integration may replace these String values with shared application entities.