package ac.uk.kn253.graphbrowser.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagertsResourceDTO {
    PagertsTextDTO text;
    PagertsLinkDTO link;

    public PagertsTextDTO getText() {
        return text;
    }

    public void setText(final PagertsTextDTO text) {
        this.text = text;
    }

    public PagertsLinkDTO getLink() {
        return link;
    }

    public void setLink(final PagertsLinkDTO link) {
        this.link = link;
    }

}
