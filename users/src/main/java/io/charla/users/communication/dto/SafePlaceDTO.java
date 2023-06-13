package io.charla.users.communication.dto;

import java.util.List;

public class SafePlaceDTO {
    private String name;
    private String country;
    private String city;
    private List<String> keywords;

    public SafePlaceDTO() {
    }

    public SafePlaceDTO(String name, String country, String city, List<String> keywords) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
