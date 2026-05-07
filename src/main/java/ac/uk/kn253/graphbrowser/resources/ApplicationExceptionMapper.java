package ac.uk.kn253.graphbrowser.resources;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@ApplicationScoped
@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(final IllegalArgumentException exception) {
        return Response.status(Status.BAD_REQUEST).entity(exception)
                .encoding(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).build();
    }

}
