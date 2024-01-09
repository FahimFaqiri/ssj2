package nl.hva.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import nl.hva.backend.models.views.ViewClasses;
import nl.hva.backend.seeders.DataSeeder;

import java.util.Random;

@Entity
public class ExpertProjectRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(ViewClasses.Shallow.class)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @JsonView(ViewClasses.Shallow.class)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    @NotNull
    private Project project;

    @JsonView(ViewClasses.Shallow.class)
    private Integer personality = 50;

    @JsonView(ViewClasses.Shallow.class)
    private Integer efficiency = 50;

    @JsonView(ViewClasses.Shallow.class)
    private Integer performance = 50;

    public ExpertProjectRating() {
    }

    public ExpertProjectRating(Long id, User user, Project project, Integer personality, Integer efficiency, Integer performance) {
        this.id = id;
        this.user = user;
        this.project = project;
        this.personality = personality;
        this.efficiency = efficiency;
        this.performance = performance;
    }

    public static ExpertProjectRating createSampleRatingForUserWithProject(User user, Project project) {
        Random random = new Random();
        return new ExpertProjectRating(
                null,
                user,
                project,
                random.nextInt(100),
                random.nextInt(100),
                random.nextInt(100)
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Integer getPersonality() {
        return personality;
    }

    public void setPersonality(Integer personality) {
        this.personality = personality;
    }

    public Integer getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Integer efficiency) {
        this.efficiency = efficiency;
    }

    public Integer getPerformance() {
        return performance;
    }

    public void setPerformance(Integer performance) {
        this.performance = performance;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExpertProjectRating{");
        sb.append("id=").append(id);
        sb.append(", user=").append(user);
        sb.append(", project=").append(project);
        sb.append(", personality=").append(personality);
        sb.append(", efficiency=").append(efficiency);
        sb.append(", performance=").append(performance);
        sb.append('}');
        return sb.toString();
    }
}
