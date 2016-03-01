package sap.bsr.lyma.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Testing is great in jersey:: using grizzly container
 *
 * http://localhost:9099/bsr/rest/hello
 * bsr/:: context-path from ws-pom.xml under web-container
 * rest/:: servlet-mapping/url-pattern from web.xml
 * hello/:: the exposed endpoint
 *
 * Created by cclaudiu on 3/1/16.
 */
@Path("/hello")
public class DemoEndpoint {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String demoEndpoint() {
        return "hello-dudes!";
    }
}
