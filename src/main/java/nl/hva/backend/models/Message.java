package nl.hva.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    private String receiver;
    private String content;

    @CreationTimestamp
    private LocalDateTime sentOn;

    private Boolean wasRead = false;

    public Message() {
    }

    public Message(Long id, String sender, String receiver, String content) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentOn() {
        return sentOn;
    }

    public void setSentOn(LocalDateTime on) {
        this.sentOn = on;
    }

    public boolean isWasRead() {
        return wasRead;
    }

    public void setWasRead(Boolean read) {
        this.wasRead = read;
    }

    @Override
    public String toString() {
        return String.format("Message(%s -> %s | %s | read: %b): %s", sender, receiver, sentOn, wasRead, content);
    }
}
