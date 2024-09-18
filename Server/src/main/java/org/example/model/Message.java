package org.example.model;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private LocalDateTime time;
    private String text;

    public Message() {
    }

    public Message(Long id, LocalDateTime time, String text) {
        this.id = id;
        this.time = time;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "id: " + id + " time: " + time + " text: " + text;
    }
}
