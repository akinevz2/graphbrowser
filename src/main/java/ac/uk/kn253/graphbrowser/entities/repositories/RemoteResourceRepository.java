package ac.uk.kn253.graphbrowser.entities.repositories;

import java.net.*;
import java.util.Optional;

import org.jboss.logging.Logger;

import ac.uk.kn253.graphbrowser.entities.dto.PagertsResourceDTO;
import ac.uk.kn253.graphbrowser.entities.resources.RemoteResource;
import ac.uk.kn253.graphbrowser.services.URLService;
import ac.uk.kn253.graphbrowser.services.URLService.SanitisationException;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RemoteResourceRepository implements PanacheRepository<RemoteResource> {

    private final Logger LOGGER = Logger.getLogger(getClass());

    @Inject
    URLService urlService;

    public Optional<RemoteResource> findByUrlOptional(final URL url) {
        return find("url", url).firstResultOptional();
    }

    public RemoteResource locate(final PagertsResourceDTO pagertsResourceDTO) {
        final var rawUrl = pagertsResourceDTO.getLink().getValue();
        final var url = clean(rawUrl);
        final var text = pagertsResourceDTO.getText().getText();
        final var remote = findByUrlOptional(url);
        LOGGER.info("checking if " + url + " is in " + remote);
        if (remote.isPresent())
            return remote.get();

        LOGGER.info("constructing new remote for " + url);
        final var resource = new RemoteResource();
        resource.setUrl(url);
        resource.setTitle(text);

        LOGGER.info("persisting remote " + resource);
        persist(resource);
        LOGGER.info("returning remote " + resource);
        return resource;
    }

    private URL clean(final String url) {
        try {
            return urlService.clean(url);
        } catch (final SanitisationException e) {
            return null;
        }
    }

}
