package io.charla.users.persistence.domain;

import io.charla.users.persistence.domain.enums.City;
import io.charla.users.persistence.domain.enums.Country;
import io.charla.users.persistence.domain.enums.SafePlaceKeywords;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
public class SafePlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "{safePlace.name}")
    @NotEmpty(message = "{safePlace.name}")
    private String name;
    @NotNull(message = "{safePlace.country}")
    @Enumerated(EnumType.STRING)
    private Country country;
    @NotNull(message = "{safePlace.city}")
    @Enumerated(EnumType.STRING)
    private City city;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "keywords", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "keywords")
    @Enumerated(EnumType.STRING)
    private List<SafePlaceKeywords> keywords;


    public SafePlace() {
    }


    public SafePlace(String name, Country country, City city, List<SafePlaceKeywords> keywords) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SafePlace)) return false;
        SafePlace safePlace = (SafePlace) o;
        return id == safePlace.id && Objects.equals(name, safePlace.name) && country == safePlace.country && city == safePlace.city && Objects.equals(keywords, safePlace.keywords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, country, city, keywords);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<SafePlaceKeywords> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<SafePlaceKeywords> keywords) {
        this.keywords = keywords;
    }
}
