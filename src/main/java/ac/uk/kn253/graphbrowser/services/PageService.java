package ac.uk.kn253.graphbrowser.services;

import java.util.List;

import ac.uk.kn253.graphbrowser.beans.PagerTS;
import ac.uk.kn253.graphbrowser.entities.repositories.RemoteResourceRepository;
import ac.uk.kn253.graphbrowser.entities.repositories.ViewedResourceRepository;
import ac.uk.kn253.graphbrowser.entities.resources.RemoteResource;
import ac.uk.kn253.graphbrowser.entities.resources.ViewedResource;
import ac.uk.kn253.graphbrowser.services.URLService.SanitisationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PageService {

    @Inject
    PagerTS pager;

    @Inject
    ViewedResourceRepository viewed;

    @Inject
    RemoteResourceRepository remote;

    @Inject
    URLService urlService;

    @Transactional
    public ViewedResource viewResource(final String resource) throws SanitisationException {
        final var url = urlService.clean(resource);
        final var found = viewed.findByUrlOptional(url);
        if (found.isPresent())
            return found.get();

        final var dto = pager.intoDto(resource);
        if (dto.getUrl() == null)
            dto.setUrl(resource);
        return viewed.locate(dto);
    }

    @Transactional
    public List<RemoteResource> getAllPages() {
        return remote.findAll().list();
    }

}
