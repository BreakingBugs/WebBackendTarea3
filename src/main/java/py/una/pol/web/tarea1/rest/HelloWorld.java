package py.una.pol.web.tarea1.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/helloworld")
public class HelloWorld {

    @GET
    public String helloWorld() {
        return "hello world.";
    }

}