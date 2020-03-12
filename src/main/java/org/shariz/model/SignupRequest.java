package org.shariz.model;

import lombok.Data;

import java.util.Objects;

/**
 * model to store a signup request. Uses Lombok to reduce getter/setter boilerplate.
 */
@Data
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String company;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignupRequest that = (SignupRequest) o;
        return firstName.equals(that.firstName) &&
                lastName.equals(that.lastName) &&
                email.equals(that.email) &&
                company.equals(that.company);
    }

    
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, company);
    }
}
