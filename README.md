# Commander

Commander is a simple Java application framework based on the [Command design pattern](http://en.wikipedia.org/wiki/Command_pattern).

### How it Works

You structure an application by defining a series of Commands, and what they do, then the framework provides the means
to allow users to execute them.  Put differently, this is a flexible way to define applications that
lend themselves to a command-line interface.

Commands can be executed via several interfaces, including:

- an interactive command-line shell
- a web service wrapper
- a well-defined extension point for others

The interactive shell includes built-in help and related features.  This framework might be right for you if you want to
create an application with a built-in shell by default, with extensibility to many other interfaces (IM, web services,
etc.)

## Requirements & Setup

Commander doesn't have any special requirements, just start using the library.

## Getting Started

Take a look at `com.willmeyer.commander.SampleApp` for the basic idea.




