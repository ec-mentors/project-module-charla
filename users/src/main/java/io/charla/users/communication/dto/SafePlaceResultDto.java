package io.charla.users.communication.dto;

import io.charla.users.persistence.domain.HostUser;
import io.charla.users.persistence.domain.SafePlace;
import io.charla.users.persistence.repository.SafePlaceRepository;

public class SafePlaceResultDto {

    private String hostEmail;

    private SafePlace safePlace;

    public SafePlaceResultDto() {
    }

    public SafePlaceResultDto(String hostEmail, SafePlace safePlace) {
        this.hostEmail = hostEmail;
        this.safePlace = safePlace;
    }

    public String getHostEmail() {
        return hostEmail;
    }

    public void setHostEmail(String hostEmail) {
        this.hostEmail = hostEmail;
    }

    public SafePlace getSafePlace() {
        return safePlace;
    }

    public void setSafePlace(SafePlace safePlace) {
        this.safePlace = safePlace;
    }

    public static SafePlaceResultDto translateToResult(SafePlace safePlace, HostUser hostUser) {
        return new SafePlaceResultDto(hostUser.getUser().getEmail(), safePlace);
    }
}
