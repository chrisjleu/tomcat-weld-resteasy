package resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("resource")
public class Api {

    /**
     * curl http://localhost:8080/api/resource/platform/123456
     * 
     * @param id
     * @return
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/platform/{id}")
    public String getPlatform(@PathParam("id") String id) {
        return "Platform " + id;
    }

}
