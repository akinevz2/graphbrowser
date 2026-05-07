package ac.uk.kn253.graphbrowser.exceptions;

// import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class ExceptionMappers {

    public Response handleExceptions(final WebApplicationException exception) {
        return exception.getResponse();
    }
}
