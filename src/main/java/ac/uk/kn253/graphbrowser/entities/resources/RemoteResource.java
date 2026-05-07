package ac.uk.kn253.graphbrowser.entities.resources;

import java.net.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties(value = "id")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "Cached")
@DiscriminatorValue("remote")
public class RemoteResource extends PanacheEntity {

    @Column(length = 5196)
    private URL url;

    @Column(nullable = false)
    private String title;

    @Override
    public String toString() {
        return String.format("RemoteResource{title='%s', url='%s', persistent='%s'}", title, url,
                isPersistent() ? "yes" : "no");
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(final URL url) {
        this.url = url;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        final var substring = (title.length() > 252) ? String.format("%s...", title.substring(0, 252)) : title;
        final var trimmed = substring.trim();
        final var firstLine = (trimmed.contains("\n")) ? trimmed.substring(0, trimmed.indexOf("\n")) : trimmed;
        this.title = firstLine;
    }

}
