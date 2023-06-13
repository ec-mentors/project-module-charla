package io.charla.matcher.communication.dto;

import io.charla.matcher.persistance.domain.enums.Language;
import io.charla.matcher.persistance.domain.enums.LocationPreference;
import io.charla.matcher.persistance.domain.enums.Topic;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;


public class MatchPropertiesDto {
    @NotNull
    private Long standardUserId;
    @NotNull
    private LocationPreference locationPreference;
    @NotEmpty
    private Set<Topic> chosenTopics;
    @NotEmpty
    private Set<Language> chosenLanguages;

    public MatchPropertiesDto() {
    }

    public MatchPropertiesDto(Long standardUserId, LocationPreference locationPreference, Set<Topic> chosenTopics, Set<Language> chosenLanguages) {
        this.standardUserId = standardUserId;
        this.locationPreference = locationPreference;
        this.chosenTopics = chosenTopics;
        this.chosenLanguages = chosenLanguages;
    }

    public Long getStandardUserId() {
        return standardUserId;
    }

    public void setStandardUserId(Long standardUserId) {
        this.standardUserId = standardUserId;
    }

    public LocationPreference getLocationPreference() {
        return locationPreference;
    }

    public void setLocationPreference(LocationPreference locationPreference) {
        this.locationPreference = locationPreference;
    }

    public Set<Topic> getChosenTopics() {
        return chosenTopics;
    }

    public void setChosenTopics(Set<Topic> chosenTopics) {
        this.chosenTopics = chosenTopics;
    }

    public Set<Language> getChosenLanguages() {
        return chosenLanguages;
    }

    public void setChosenLanguages(Set<Language> chosenLanguages) {
        this.chosenLanguages = chosenLanguages;
    }

    //#Before a match is possible the user has to fill out his profile and also enter some preferences:
    //
    //#Location - mandatory - Single choice - “Anywhere”, “Country”, “City”
    //#Topic - mandatory - Multi choice - “Politics”, “Environment”, “Religion”, “Corona”
    //#Language - mandatory - Multi choice
    //

    //#Acceptance Criteria


    //#If the user did not fill out his profile an error is returned
    //#If the user did not fill out the mandatory fields an error is returned
    //#If the user filled out everything correct a match is searched
    //#Suggested language and topic is based on my profile
}
