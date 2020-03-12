package org.shariz.model;

import com.google.gson.Gson;
import org.junit.Test;
import org.shariz.utils.HelperMethods;
import static org.junit.Assert.*;

public class SignupRequestTest {

    /**
     * test serialization/deserialization methods to make sure the Producer and Consumer can work on a single message type.
     */
    @Test
    public void testSerialize() {

        // create a SignupRequest object, serialize it to JSON and then convert it back to the object
        SignupRequest original = new SignupRequest();
        original.setFirstName("John");
        original.setLastName("Appleseed");
        original.setEmail("jappleseed@apple.com");
        original.setCompany("Apple");
        String serialized = HelperMethods.serialize(original);

        Object deserialized = HelperMethods.deserialize(serialized, SignupRequest.class);

        assertEquals("Original differs from unserialized!",original,deserialized);

    }
}