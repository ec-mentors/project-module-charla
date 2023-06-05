package io.charla.users.communication.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class TopicScoreDto {
    @NotNull
    private String topic;
    @Range(min = 0, max = 4)
    private int answerOne;
    @Range(min = 0, max = 4)
    private int answerTwo;
    @Range(min = 0, max = 4)
    private int answerThree;

    public TopicScoreDto() {
    }

    public TopicScoreDto(String topic, int answerOne, int answerTwo, int answerThree) {
        this.topic = topic;
        this.answerOne = answerOne;
        this.answerTwo = answerTwo;
        this.answerThree = answerThree;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getAnswerOne() {
        return answerOne;
    }

    public void setAnswerOne(int answerOne) {
        this.answerOne = answerOne;
    }

    public int getAnswerTwo() {
        return answerTwo;
    }

    public void setAnswerTwo(int answerTwo) {
        this.answerTwo = answerTwo;
    }

    public int getAnswerThree() {
        return answerThree;
    }

    public void setAnswerThree(int answerThree) {
        this.answerThree = answerThree;
    }
}
