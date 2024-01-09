package nl.hva.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String subject;
    private String peekEmail;
    private String toEmail;
    private String expertMessage;
    private String brokerAnswer;

    public Support() {
    }

    public Support(Long id, String username, String subject, String peekEmail, String toEmail, String expertMessage, String brokerAnswer) {
        this.id = id;
        this.username = username;
        this.subject = subject;
        this.peekEmail = peekEmail;
        this.toEmail = toEmail;
        this.expertMessage = expertMessage;
        this.brokerAnswer = brokerAnswer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPeekEmail() {
        return peekEmail;
    }

    public void setPeekEmail(String fromEmail) {
        this.peekEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getExpertMessage() {
        return expertMessage;
    }

    public void setExpertMessage(String expertMessage) {
        this.expertMessage = expertMessage;
    }

    public String getBrokerAnswer() {
        return brokerAnswer;
    }

    public void setBrokerAnswer(String brokerAnswer) {
        this.brokerAnswer = brokerAnswer;
    }
}

