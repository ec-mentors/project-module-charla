package io.charla.users.persistence.domain;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
public class StandardUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @OneToOne
    private User user;

    //TODO - what is meant by mandatory here, when editing or creating?
    //@ManyToMany(fetch = FetchType.EAGER)
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

    private Country country;
    private City city;

    @ElementCollection
    @CollectionTable(name = "user_preferences", joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "topic")
    @Column(name = "preference")
    private Map<Topic, Integer> topicScoresMap = new HashMap<>();

    //Acceptance Criteria

    //User sees in the beginning his profile data
    //If any mandatory field is empty an error is thrown and saving is not possible
    //If all mandatory fields are filled out the data is stored in the DB


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
}
