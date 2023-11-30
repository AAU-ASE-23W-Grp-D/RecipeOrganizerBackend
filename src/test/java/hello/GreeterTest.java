package hello;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GreeterTest {
    private final Greeter greeter = new Greeter();

    @Test
    public void greeterSaysHello() {
        assertEquals(greeter.sayHello(), "Hello world!");
    }
}
