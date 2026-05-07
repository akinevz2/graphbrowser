package ac.uk.kn253.graphbrowser.entities.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class PagertsDTO {
    private String url;

    private String title;

    private String error;

    private List<PagertsResourceDTO> resources;

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public List<PagertsResourceDTO> getResources() {
        return resources;
    }

    public void setResources(final List<PagertsResourceDTO> resources) {
        this.resources = resources;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

}
