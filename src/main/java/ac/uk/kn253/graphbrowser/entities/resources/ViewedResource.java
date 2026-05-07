package ac.uk.kn253.graphbrowser.entities.resources;

import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("viewed")
public class ViewedResource extends RemoteResource {

    @Column(nullable = false)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="REMOTE_RESOURCE_LINK")
    private List<RemoteResource> resources;

    public ViewedResource() {
        super();
    }

    public ViewedResource(final RemoteResource existing, final String title, final List<RemoteResource> resources) {
        super();
        final var url = existing.getUrl();
        setUrl(url);
        setTitle(title);
        setResources(resources);
    }

    @Override
    public String toString() {
        return String.format("ViewedResource{title='%s', url='%s'}", getTitle(), getUrl());
    }

    public List<RemoteResource> getResources() {
        return resources;
    }

    public void setResources(final List<RemoteResource> resources) {
        this.resources = resources;
    }

}
