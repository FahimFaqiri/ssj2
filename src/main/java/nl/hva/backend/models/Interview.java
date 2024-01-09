package nl.hva.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private Long belongsToUser;

    @NotBlank
    private Long interviewedBy;

    @NotBlank
    @Size(min = 4, max = 50)
    private String title;

    @NotBlank
    @Column(length = 8192)
    private String text;

    @NotBlank
    private LocalDate interviewedOnDate;

    public Interview(Long id, Long belongsToUser, Long interviewedBy, String title, String text, LocalDate interviewedOnDate) {
        this.id = id;
        this.belongsToUser = belongsToUser;
        this.interviewedBy = interviewedBy;
        this.title = title;
        this.text = text;
        this.interviewedOnDate = interviewedOnDate;
    }

    public Interview() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBelongsToUser() {
        return belongsToUser;
    }

    public void setBelongsToUser(Long belongsToUser) {
        this.belongsToUser = belongsToUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getInterviewedBy() {
        return interviewedBy;
    }

    public void setInterviewedBy(Long interviewedByPerson) {
        this.interviewedBy = interviewedByPerson;
    }

    public LocalDate getInterviewedOnDate() {
        return interviewedOnDate;
    }

    public void setInterviewedOnDate(LocalDate interviewedOnDate) {
        this.interviewedOnDate = interviewedOnDate;
    }

    @Override
    public String toString() {
        return "Interview{" +
                "id=" + id +
                ", belongsToUser=" + belongsToUser +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", interviewedByPerson='" + interviewedBy + '\'' +
                ", interviewedOnDate=" + interviewedOnDate +
                '}';
    }
}
