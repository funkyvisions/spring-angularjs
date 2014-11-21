Application demonstrating an AngularJS client talking to a Spring REST-powered backend.

## Building

To build the WAR file simply run `mvn package`. This puts the WAR
file in `target/spring-angularjs.war`.

You can deploy this WAR in any servlet container.

## Running

You can run the app in Jetty using `mvn jetty:run`.

Once started, the app is available at `http://localhost:8080`.

## Testing

The standard Java tests can be run with `mvn test`.

### JavaScript tests

The JavaScript tests use [Karma Runner][karma]. Rather than rely on a
global install of Karma, this project uses a local install.

Run `npm install` to install all the local JS dependencies and then
run:

  node_module/karma/bin/karma start src/test/js/config/karma.unit.js

This will start the live Karma test runner.

For convenience use the supplied Rake task: `rake test:karma:start`.


[karma]: http://karma-runner.github.com/
