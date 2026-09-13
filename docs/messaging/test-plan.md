# Messaging Component Test Plan

## Overview

This test plan covers the Cupid Messaging component implemented for COIT13235 Enterprise Software Development.

The purpose of testing is to verify that the component correctly supports messaging, conversation history, message threads, sanitisation, persistence, failure handling, timeout behaviour, and concurrent use.

## Test Environment

The Messaging component is tested using:

- Java
- Spring Boot
- Spring Data JPA
- H2 Database
- JUnit 5
- Mockito
- Spring MockMvc
- Maven

The current persistence layer uses an in-memory H2 database.

## Test Objectives

The main testing objectives are to verify that:

- messages can be created and stored
- message content is sanitised before persistence
- conversation history can be retrieved
- message threads can be retrieved
- repository queries return the expected data
- controller endpoints return the expected views
- persistence failures are handled gracefully
- slow persistence operations trigger the configured timeout
- the component can handle 100 simultaneous message requests

## Test Cases

### TC01 – Sanitise Unsafe HTML

Purpose:

Verify that unsafe HTML characters are escaped before message content is stored.

Input:

`<script>alert('hello')</script>`

Expected Result:

The value is converted to:

`&lt;script&gt;alert(&#39;hello&#39;)&lt;/script&gt;`

Test Class:

`MessageSanitizerTest`

## TC02 – Trim Message Input

Purpose:

Verify that unnecessary leading and trailing whitespace is removed.

Input:

`   Hello   `

Expected Result:

`Hello`

Test Class:

`MessageSanitizerTest`

## TC03 – Handle Null Sanitisation Input

Purpose:

Verify that null input does not cause an error.

Input:

`null`

Expected Result:

An empty String is returned.

Test Class:

`MessageSanitizerTest`

## TC04 – Send and Sanitise Message

Purpose:

Verify that the Messaging service sanitises content and creates a message correctly.

Input:

Sender: `Safwan`

Receiver: `Alex`

Content: `<b>Hello</b>`

Expected Result:

The returned Message contains:

- sender: `Safwan`
- receiver: `Alex`
- content: `&lt;b&gt;Hello&lt;/b&gt;`

Test Class:

`MessagingServiceTest`

## TC05 – Retrieve Conversation History

Purpose:

Verify that conversation history is returned from the service.

Input:

User 1: `Safwan`

User 2: `Alex`

Expected Result:

Messages between the selected users are returned.

Test Class:

`MessagingServiceTest`

## TC06 – Persist and Retrieve Conversation

Purpose:

Verify that JPA can save messages and retrieve both directions of a conversation.

Test Data:

- Safwan → Alex: `Hello Alex`
- Alex → Safwan: `Hi Safwan`

Expected Result:

Two messages are returned in chronological order.

Test Class:

`MessageRepositoryTest`

## TC07 – Retrieve Thread Messages

Purpose:

Verify that repository queries can retrieve messages where a selected username appears as either sender or receiver.

Test Data:

- Safwan → Alex
- John → Safwan

Expected Result:

Two matching messages are returned.

Test Class:

`MessageRepositoryTest`

## TC08 – Display Messaging Page

Purpose:

Verify that the Messaging controller returns the correct view and model attributes.

Endpoint:

`GET /messages`

Expected Result:

View:

`messaging/messages`

Expected model attributes:

- `messages`
- `encouragementEnabled`
- `encouragementMessage`

Test Class:

`MessagingControllerTest`

## TC09 – Display Conversation History Page

Purpose:

Verify that the conversation endpoint returns the correct view and data.

Endpoint:

`GET /messages/conversation`

Parameters:

- `user1=Safwan`
- `user2=Alex`

Expected Result:

View:

`messaging/conversation`

Expected model attributes:

- `conversation`
- `user1`
- `user2`

Test Class:

`MessagingControllerTest`

## TC10 – Persistence Timeout

Purpose:

Verify that the Messaging component fails gracefully when persistence takes longer than the allowed limit.

Configured Timeout:

5 seconds

Simulated Persistence Delay:

6 seconds

Expected Result:

A `MessagingException` is raised rather than allowing the request to wait indefinitely.

Test Class:

`MessagingTimeoutTest`

## TC11 – 100 Simultaneous Users

Purpose:

Verify that the Messaging component can process a simulated workload of 100 concurrent users.

Method:

100 asynchronous requests are created using `CompletableFuture`.

Each simulated user attempts to send one message.

Expected Result:

All 100 requests complete successfully.

Test Class:

`MessagingPerformanceTest`

## Full Test Suite Result

The current complete Maven test suite produced:

- Tests run: 11
- Failures: 0
- Errors: 0
- Skipped: 0
- Build result: SUCCESS

This confirms that all currently implemented automated Messaging tests passed together.

## Manual Functional Tests

Manual browser testing was also performed.

### Send Message

Steps:

1. Open `/messages`
2. Enter sender
3. Enter receiver
4. Enter message content
5. Select Send Message

Expected Result:

The message appears in the messages list.

Result:

Passed.

### Conversation History

Steps:

1. Send messages between two users in both directions
2. Open the conversation endpoint for those users

Expected Result:

Both users' messages appear in the same conversation.

Result:

Passed.

### Message Threads

Steps:

1. Create conversations between one user and multiple other users
2. Enter the user's name in View Message Threads
3. Open the thread list

Expected Result:

Each conversation partner appears once.

Result:

Passed.

## Evidence to Keep

The following evidence should be retained for assessment and interview purposes:

- screenshot of the successful Maven test run
- screenshot showing 11 tests with 0 failures and 0 errors
- screenshot of the Messaging main page
- screenshot of message thread list
- screenshot of conversation history
- class diagram
- sequence diagram
- ERD

## Test Status

Current Messaging testing status:

- Sanitisation: Passed
- Service behaviour: Passed
- Repository persistence: Passed
- Conversation history: Passed
- Threads: Passed
- Controller behaviour: Passed
- Timeout handling: Passed
- 100-user concurrent test: Passed
- Full automated test suite: Passed