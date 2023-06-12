package io.charla.matcher.persistance.domain;


import io.charla.matcher.persistance.domain.enums.City;
import io.charla.matcher.persistance.domain.enums.Country;
import io.charla.matcher.persistance.domain.enums.Language;
import io.charla.matcher.persistance.domain.enums.Topic;

import javax.persistence.*;
import java.util.*;

@Entity
public class StandardUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @OneToOne
    private User user;
//todo customiz enum deserlization error msg
    @ElementCollection
    @CollectionTable(name = "languages", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "languages")
    @Enumerated(EnumType.STRING)
    //@JoinColumn(name = "parent", referencedColumnName = "id")
    private Set<Language> languages = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_topics", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "topic")
    @Enumerated(EnumType.STRING)
    //@ManyToMany(fetch = FetchType.EAGER)
    private Set<Topic> preferredTopics = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Country country;
    @Enumerated(EnumType.STRING)
    private City city;

    @ElementCollection
    @CollectionTable(name = "user_preferences", joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "topic")
    @Column(name = "preference")
    private Map<Topic, Integer> topicScoresMap = new HashMap<>();

    public StandardUser() {
    }

    public StandardUser(User user, Set<Language> languages, Set<Topic> preferredTopics, Country country, City city, Map<Topic, Integer> topicScoresMap) {
        this.user = user;
        this.languages = languages;
        this.preferredTopics = preferredTopics;
        this.country = country;
        this.city = city;
        this.topicScoresMap = topicScoresMap;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Language> getLanguages() {
        return languages;
    }
    public void addLanguage(Language language) {
        languages.add(language);
    }

    public void addPreferredTopic(Topic topic) {
        preferredTopics.add(topic);
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public Set<Topic> getPreferredTopics() {
        return preferredTopics;
    }

    public void setPreferredTopics(Set<Topic> preferredTopics) {
        this.preferredTopics = preferredTopics;
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

    public Map<Topic, Integer> getTopicScoresMap() {
        return topicScoresMap;
    }

    public void addTopicScore(Topic topic, int score) {
        topicScoresMap.put(topic, score);
    }

    public void setTopicScoresMap(Map<Topic, Integer> topicScoresMap) {
        this.topicScoresMap = topicScoresMap;
    }

    @Override
    public String toString() {
        return "StandardUser{" +
                "Id=" + Id +
                ", user=" + user +
                ", languages=" + languages +
                ", preferredTopics=" + preferredTopics +
                ", country=" + country +
                ", city=" + city +
                ", topicScoresMap=" + topicScoresMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandardUser that = (StandardUser) o;
        return Id == that.Id && Objects.equals(user, that.user) && Objects.equals(languages, that.languages) && Objects.equals(preferredTopics, that.preferredTopics) && country == that.country && city == that.city && Objects.equals(topicScoresMap, that.topicScoresMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, user, languages, preferredTopics, country, city, topicScoresMap);
    }
}
