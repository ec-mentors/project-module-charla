package io.charla.users.communication.dto;

import io.charla.users.persistence.domain.enums.Language;
import io.charla.users.persistence.domain.enums.LocationPreference;
import io.charla.users.persistence.domain.enums.Topic;

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
}
