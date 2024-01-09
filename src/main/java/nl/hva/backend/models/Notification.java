package nl.hva.backend.models;

import jakarta.persistence.*;

import java.util.Optional;

@Entity
public class Notification {




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private String type;

    public Notification() {
    }

    public Notification(Long id) {
    }

    public static Notification createRequestNotification(Long id) {
        Notification notification = new Notification(id);

        notification.setMessage("send you a request to join as an expert!");
        notification.setType("Request");

        return notification;

    }

    public static Notification createDeniedNotification() {
        Notification notification = new Notification();

        notification.setMessage("Access has not been granted");
        notification.setType("Denied");

        return notification;

    }

    public static Notification createAcceptedNotification() {
        Notification notification = new Notification();

        notification.setMessage("Has been accepted");
        notification.setType("Accepted");

        return notification;

    }



    
    public Notification(Long id, String message, String type) {
        this.id = id;
        this.message = message;
        this.type = type;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
