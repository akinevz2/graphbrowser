package ac.uk.kn253.graphbrowser.services;

import java.net.*;
import java.nio.charset.Charset;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class URLService {

    public URL clean(final String url) throws SanitisationException {
        final var pathSlashIndex = 1 + url.lastIndexOf("/");
        final var base = url.substring(0, pathSlashIndex);
        final var path = url.substring(pathSlashIndex);
        final var encoded = URLEncoder.encode(path, Charset.defaultCharset());
        final var sanitised = base + encoded;
        try {
            return new URI(sanitised).toURL();
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
            final var message = String.format("failed to sanitise URL: %s", url);
            throw new SanitisationException(message, e);
        }
    }

    public static class SanitisationException extends Exception {
        public SanitisationException(final String message, final Exception cause) {
            super(message, cause);
        }
    }

}
