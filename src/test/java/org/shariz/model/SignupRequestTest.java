package org.shariz.model;

import com.google.gson.Gson;
import org.junit.Test;
import org.shariz.utils.HelperMethods;

public class SignupRequestTest {

    @Test
    public void createJson() {
        SignupRequest request = new SignupRequest();
        request.setFirstName("John");
        request.setLastName("Appleseed");
        request.setEmail("jappleseed@apple.com");
        request.setCompany("Apple");
        String json = new Gson().toJson(request);
        System.out.println(json);
        System.out.println(HelperMethods.serialize(request));
    }
}