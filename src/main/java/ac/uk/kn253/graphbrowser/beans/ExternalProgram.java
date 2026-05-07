package ac.uk.kn253.graphbrowser.beans;

import java.beans.JavaBean;
import java.io.*;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import ac.uk.kn253.graphbrowser.exceptions.NoSuchProgramException;
import jakarta.persistence.Embeddable;

@Embeddable
public class ExternalProgram implements AutoCloseable {

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    private final String programName;
    private final ProcessBuilder processBuilder;

    private Path outputRedirect = null;

    private ProgramRunResult result;

    public ExternalProgram(final String name) throws NoSuchProgramException {
        this.programName = name;
        this.processBuilder = new ProcessBuilder(name);
        ensureExists("--help");
    }

    /**
     * Ensure that the program exists on the system before running it.
     * 
     * @param usingArgs passed to the program to test if it exists. Can be empty.
     * @throws NoSuchProgramException if the program is not found on the system.
     */
    protected final void ensureExists(final String... usingArgs) throws NoSuchProgramException {
        try {
            // Check if the program exists by running it with test arguments
            final var errorCode = (int) runWithArgs(usingArgs).get().getStatusCode();
            if (errorCode != 0) {
                throw new RuntimeException(programName + " not found on the system");
            }
        } catch (final Exception e) {
            // Handle the exception, if program is not found on the system
            throw new NoSuchProgramException(e.getMessage(), e);
        }
    }

    public Future<ProgramRunResult> runWithArgs(final String... args) throws InterruptedException, ExecutionException {
        Objects.requireNonNull(args);
        this.outputRedirect = outputRedirect();
        return executorService.submit(() -> {
            this.result = new ProgramRunResult();
            final var commandString = processBuilder.command();
            // set the command string
            commandString.clear();
            commandString.add(programName);
            commandString.addAll(Arrays.asList(args));
            // run the command
            final var process = processBuilder.start();
            // wait for the process to finish
            final var errorCode = process.waitFor();
            result.setStatusCode(errorCode);
            // copy output from the process if redirect is set
            final var content = Files.readAllLines(outputRedirect);
            result.setOutput(content);
            // return the result
            return result;
        });
    }

    /**
     * Construct a redirectPipe
     *
     */
    private Path outputRedirect() {
        try {
            final var path = Files.createTempFile("graphbrowser", ".tmp");
            final var file = path.toFile();
            processBuilder.redirectOutput(Redirect.appendTo(file));
            // processBuilder.redirectError(Redirect.appendTo(file));
            return path;
        } catch (final IOException e) {
            final String message = "Could not create a temp file for redirecting output";
            throw new RuntimeException(message, e);
        }
    }

    /**
     * Destruct a redirectPipe
     * 
     * @param redirectTmp
     * @throws IOException
     */
    private void cleanupRedirectFile() throws IOException {
        try {
            if (outputRedirect != null)
                Files.deleteIfExists(outputRedirect);
        } catch (final IOException e) {
            final String message = "Could not delete temp file for redirecting output";
            throw new RuntimeException(message, e);
        }
    }

    public String getOutput() {
        return Objects.requireNonNull(result).getOutput().stream().collect(Collectors.joining("\n"));
    }

    public <T> T getOutput(final Class<T> clazz) throws IOException {
        try (final var reader = new StringReader(getOutput())) {
            final var objectMapper = new ObjectMapper();
            return objectMapper.readValue(reader, clazz);
        } catch (final IOException e) {
            throw e;
        }
    }

    @Override
    public void close() throws IOException {
        cleanupRedirectFile();
    }

    @JavaBean
    public class ProgramRunResult {
        public ProgramRunResult() {
            super();
        }

        List<String> output;
        int statusCode = -1;

        public boolean hasOutput() {
            return output != null;
        }

        public List<String> getOutput() {
            return output;
        }

        public void setOutput(final List<String> output) {
            this.output = output;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(final int statusCode) {
            this.statusCode = statusCode;
        }

    }

}
