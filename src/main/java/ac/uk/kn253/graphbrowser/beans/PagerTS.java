package ac.uk.kn253.graphbrowser.beans;

import java.beans.JavaBean;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import ac.uk.kn253.graphbrowser.entities.dto.PagertsDTO;
import ac.uk.kn253.graphbrowser.exceptions.NoSuchProgramException;
import ac.uk.kn253.graphbrowser.exceptions.PagerRunException;
import jakarta.enterprise.inject.Default;

@JavaBean
@Default
public class PagerTS implements AutoCloseable {

    private final ExternalProgram pagerts;

    public PagerTS() throws NoSuchProgramException {
        this.pagerts = new ExternalProgram("pagerts");
    }

    @Override
    public void close() throws IOException {
        pagerts.close();
    }

    public PagertsDTO intoDto(final String url) {
        try {
            final var result = pagerts.runWithArgs(url).get();
            final var statusCode = result.getStatusCode();
            if (statusCode != 0)
                throw new PagerRunException("Program exited abnormally: " + statusCode);
            final var cache = pagerts.getOutput(PagertsDTO[].class)[0];
            if(cache.getError() != null)
                throw new IOException(cache.getError());
            return cache;
        } catch (final IOException e) {
            throw new PagerRunException("Could not parse pagerts output: "+e.getMessage(), e);
        } catch (InterruptedException | ExecutionException e) {
            throw new PagerRunException("Could not run pagerts", e);
        }
    }

}
