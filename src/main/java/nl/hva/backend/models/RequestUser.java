package nl.hva.backend.models;

import com.mysql.cj.PreparedQuery;
import jakarta.persistence.*;

@Entity
public class RequestUser {

    @OneToOne
    private Notification notification;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private boolean accepted;
    private boolean denied;

    public RequestUser() {}

    public RequestUser(Long id) {}

    public RequestUser(Long id, String firstName, String lastName, String email, boolean accepted, boolean denied, Notification notification) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.notification = notification;
        this.accepted = accepted;
        this.denied = denied;
    }

    public RequestUser createSample(long id) {
        RequestUser requestUser = new RequestUser(id);

        requestUser.setFirstName(this.firstName);
        requestUser.setLastName(this.lastName);
        requestUser.setEmail(this.email);
        requestUser.setAccepted(this.accepted);
        requestUser.setDenied(this.denied);

        return requestUser;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public boolean isDenied() {
        return denied;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setDenied(boolean denied) {
        this.denied = denied;
    }
    public Notification getNotification() {
        return notification;
    }
}
