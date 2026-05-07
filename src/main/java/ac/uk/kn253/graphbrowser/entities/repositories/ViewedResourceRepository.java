package ac.uk.kn253.graphbrowser.entities.repositories;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jboss.logging.Logger;

import ac.uk.kn253.graphbrowser.entities.dto.PagertsDTO;
import ac.uk.kn253.graphbrowser.entities.resources.RemoteResource;
import ac.uk.kn253.graphbrowser.entities.resources.ViewedResource;
import ac.uk.kn253.graphbrowser.services.URLService;
import ac.uk.kn253.graphbrowser.services.URLService.SanitisationException;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ViewedResourceRepository implements PanacheRepository<ViewedResource> {

    private final Logger LOGGER = Logger.getLogger(getClass());

    @Inject
    RemoteResourceRepository repository;

    @Inject
    URLService urlService;

    public Optional<ViewedResource> findByUrlOptional(final URL url) {
        return find("url", url).firstResultOptional();
    }

    public ViewedResource locate(final PagertsDTO pagertsDTO) {
        final var rawUrl = pagertsDTO.getUrl();
        final var url = clean(rawUrl);
        LOGGER.info("locating for URL in viewed");
        final var title = pagertsDTO.getTitle();

        final var viewed = findByUrlOptional(url);
        LOGGER.info("result from viewedRepo " + viewed.toString());
        if (viewed.isPresent())
            return viewed.get();

        final var resources = pagertsDTO.getResources().stream()
                .filter(r -> r.getLink() != null && r.getLink().getValue() != null)
                .map(repository::locate).toList();
        LOGGER.info("constructing new resource: "
                + Stream.of(resources.toArray()).map(Object::toString).collect(Collectors.joining(",\n")));

        final var remote = repository.findByUrlOptional(url);
        if (remote.isPresent()) {
            LOGGER.info("result from remoteRepo" + remote.toString());
            final var existing = remote.get();
            return view(existing, title, resources);
        }

        final var resource = new ViewedResource();
        resource.setUrl(url);
        resource.setTitle(title);
        resource.setResources(resources);
        LOGGER.info("persisting viewed " + resource);
        persist(resource);
        return resource;
    }

    private URL clean(final String url) {
        try {
            return urlService.clean(url);
        } catch (final SanitisationException e) {
            return null;
        }
    }

    private ViewedResource view(final RemoteResource existing, final String title,
            final List<RemoteResource> resources) {
        LOGGER.info("viewing remote");
        final var viewed = new ViewedResource(existing, title, resources);
        LOGGER.info("persisting viewed");
        // repository.delete(existing);
        persist(viewed);
        return viewed;
    }

}
