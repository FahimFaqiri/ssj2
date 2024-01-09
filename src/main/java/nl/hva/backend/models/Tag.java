package nl.hva.backend.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import nl.hva.backend.models.views.ViewClasses;

import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties("projects")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(ViewClasses.Shallow.class)
    private Long id;

    @JsonView(ViewClasses.Shallow.class)
    private String title;

    @JsonView(ViewClasses.Shallow.class)
    private String color;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<Project> projects = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private List<User> users = new ArrayList<>();

    public Tag() {
    }

    public Tag(Long id, String title, String color) {
        this.id = id;
        this.title = title;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
