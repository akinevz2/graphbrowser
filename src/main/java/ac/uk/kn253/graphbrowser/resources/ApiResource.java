package ac.uk.kn253.graphbrowser.resources;

import java.net.*;
import java.util.List;

import org.jboss.resteasy.reactive.RestQuery;

import ac.uk.kn253.graphbrowser.entities.resources.RemoteResource;
import ac.uk.kn253.graphbrowser.entities.resources.ViewedResource;
import ac.uk.kn253.graphbrowser.services.PageService;
import ac.uk.kn253.graphbrowser.services.URLService.SanitisationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/api")
public class ApiResource {

    @Inject
    PageService service;

    @GET
    @Path("/view")
    @Produces(MediaType.APPLICATION_JSON)
    public ViewedResource view(@RestQuery final String q) {
        try {
            if (q == null || q.isBlank())
                throw new BadRequestException("Null or empty ?q= parameter provided");
            if (new URI(q).toURL() == null)
                throw new MalformedURLException(String.format("%s is not a valid url", q));
            return service.viewResource(q);
        } catch (MalformedURLException | URISyntaxException e) {
            throw new BadRequestException(String.format("q=%s is not a valid URL", q), e);
        } catch (final SanitisationException e) {
            throw new BadRequestException(e.getMessage(), e);
        }
    }

    @GET
    @Path("/all")
    public List<RemoteResource> getAll() {
        return service.getAllPages();
    }

    @GET
    @Path("/count")
    public int getCount() {
        return service.getAllPages().size();
    }

}
