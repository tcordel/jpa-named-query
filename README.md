
Here is a sample project presenting an unexpected behaviour using jpa named queries and dealing with transaction propagations.

Downgrading hibernate version to 6.5.3.Final fixes it.

See tests for more details [DemoApplicationTests.java](src/test/java/com/example/demo/DemoApplicationTests.java)
