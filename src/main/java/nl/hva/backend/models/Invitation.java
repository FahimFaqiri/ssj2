package nl.hva.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String uniqueCode;
    @Column(nullable = false)
    @Size(max = 255)
    @Email
    private String recipientEmail;
    @Future
    LocalDate expirationDate;

    public Invitation() {
        //
    }

    public Invitation(Long id, String uniqueCode) {
        this.id = id;
        this.uniqueCode = uniqueCode;
    }

    public Invitation(Long id, String uniqueCode, String recipientEmail, LocalDate expirationDate) {
        this.id = id;
        this.uniqueCode = uniqueCode;
        this.recipientEmail = recipientEmail;
        this.expirationDate = expirationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}
