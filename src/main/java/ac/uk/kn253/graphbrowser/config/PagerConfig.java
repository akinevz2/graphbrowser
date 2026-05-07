package ac.uk.kn253.graphbrowser.config;

import ac.uk.kn253.graphbrowser.beans.PagerTS;
import ac.uk.kn253.graphbrowser.exceptions.NoSuchProgramException;
import jakarta.enterprise.context.*;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class PagerConfig {
    
    @Produces
    public PagerTS getPager() throws NoSuchProgramException {
        return new PagerTS();
    }
    
}
