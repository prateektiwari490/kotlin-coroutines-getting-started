# Echo Server and Clients

This project consists of a multiple implementations of an echo server and multiple clients that work against that
server. There are four sub-projects, the server itself (`server`), a console client (`client`), an FX client
(`FxClient`) and a Compose Desktop client (`Desktop Client`). All of the projects use asynchronous sockets in their
implementation

## The Servers

The server has three implementations, a basic implementation using Java `Future`, an implementation that uses the
callback mechanism on the async socket and an implementation that uses coroutines

### Simple Server

The `AsyncEchoServer` is the base implementation. The code creates an instance of the `AsyncEchoServer` class (part of
the project), then binds and accepts on port 9999. The `runServer` function loops for each connected client. Once the
client is connected then it reads from the channel and writes back the same data to the channel. This server only
accepts one connection

### AsyncEchoServerCallback

This server will accept more than one connection! It works with a completion handler on accept, the `completed`
function on the handler then sets up another `CompletionHandler`. The `read` method is called to start the echo server
listening and it is passed an attachment and the handler, when the `read` completes the `ReadWriteHandler`'s
`completed` function is called, the action is `read`, this gets the buffer from the attachment and writes the data down
the channel to the client. When the `write` completes the `completed` function is called again, this time with the
action set to `write` and this puts the channel back into 'read' mode

### AsyncEchoServerCallbackSuspended

This server uses the helper class `TcpSocket`, this is in the shared project, which also uses completion handlers.
The `TcpSocket` class consists of `read`, `write`, and `connect` methods and `asyncRead`, `asyncWrite` and
`asyncConnect` methods which are extensions on `AsynchronousSocketChannel`. The 'non-async' methods call the
corresponding 'async' methods passing a buffer, the async method then calls `suspendCoroutine`, then, inside that
callback, call `read` on the`AsynchronousSocketChannel` passing the continuation and a `CompletionHandler`. The
completion handler calls either`resume` or `resumeWithException` on the continuation to restart the coroutine. To
take `read` as an example, `read` needs the async socket to read from the socket, to do this it has to 
suspend the coroutine, perform the read and then, when the read has completed resume the coroutine. The `resume` is 
called in the completion handler.

## The Clients

### Console Client

A simple console client that uses the shared `TcpSocket` class to manage the async socket connection, it reads a 
line at a time from the console, sends that line to the server and displays the echo response

### FxClient

Uses JavaFX and TornadoFX as the UI. The `MainView` class implements `CoroutineScope` and so sets up a coroutine 
context, this allows us to use 'structured concurrency' to manage the coroutines. You see the different contexts in 
use, such as IO, Default and JavaFx

### Desktop Client

This uses the JetPack Compose Desktop libraries to create a UI. In Compose the UI is updated when state is changed, 
this update happens on the UI thread but the state can be changed on any thread so there is no need to switch back 
to the UI when changing state. A `scope` can be created by using the `rememberCoroutineScope` function and a `Job` 
can be stored as a remembered state and later on used to cancel any coroutines. Other than that the concepts are the 
same as for the FxClient

