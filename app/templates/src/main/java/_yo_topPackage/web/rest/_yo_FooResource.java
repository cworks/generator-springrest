package <%= app.packageName %>.web.rest;

import <%= app.packageName %>.web.rest.dto.FooDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/<%= app.apiRoot %>")
public class FooResource {

    @RequestMapping(value = "/rest/foo",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public FooDTO getFoo() {
        return new FooDTO("foo");
    }

    @RequestMapping(value = "/rest/foo",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createFoo(FooDTO fooDTO) {
        System.out.println("createFoo: " + fooDTO.toString());
    }

    @RequestMapping(value = "/rest/foo",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateFoo(FooDTO fooDTO) {
        System.out.println("updateFoo: " + fooDTO.toString());
    }

    @RequestMapping(value = "/rest/foo",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFoo(FooDTO fooDTO) {
        System.out.println("deleteFoo: " + fooDTO.toString());
    }

}
