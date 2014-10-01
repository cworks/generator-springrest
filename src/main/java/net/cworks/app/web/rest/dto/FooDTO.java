/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: springrest
 * Package: net.cworks.app.web.rest.dto
 * Class: FooDTO
 * Created: 9/30/14 2:01 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.app.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class FooDTO {

    private String something;

    @JsonCreator
    public FooDTO() {

    }

    public FooDTO(String something) {
        this.something = something;
    }

    public String getFoo() {
        return this.something;
    }

    public void setFoo(String something) {
        this.something = something;
    }

    @Override
    public String toString() {
        return "FooDTO{ something='" + something + '\'' + "}";
    }
}
