package nl.hva.backend.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import nl.hva.backend.models.views.ViewClasses;
import nl.hva.backend.seeders.DataSeeder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(ViewClasses.Shallow.class)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonView(ViewClasses.Shallow.class)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commenter_id")
    private User commenter;

    @JsonView(ViewClasses.Shallow.class)
    @CreationTimestamp
    private LocalDateTime commentedOn;

    @JsonView(ViewClasses.Shallow.class)
    private String content;

    public Comment() {
    }

    public Comment(Long id, User user, User commenter, String content) {
        this.id = id;
        this.user = user;
        this.commenter = commenter;
        this.content = content;
    }

    public static Comment createSampleCommentForBy(User user, User commenter) {
        return new Comment(
                null,
                user,
                commenter,
                DataSeeder.generateRandomString(64)
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(LocalDateTime commentedOn) {
        this.commentedOn = commentedOn;
    }
}
