package nl.hva.backend.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String name;

    private String date;

    private String description;

    @Column(nullable = false)
    private String coreFunctionality;

    private String image;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ExpertProjectRating> expertProjectRatings = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "project_tag",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    public Project() {
    }

    public Project(
            Long id,
            String location,
            String name,
            String date,
            String description,
            String coreFunctionality,
            String image,
            String content,
            Client client,
            List<Tag> tags
    ) {
        this.id = id;
        this.location = location;
        this.name = name;
        this.date = date;
        this.description = description;
        this.coreFunctionality = coreFunctionality;
        this.image = image;
        this.content = content;
        this.client = client;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoreFunctionality() {
        return coreFunctionality;
    }

    public void setCoreFunctionality(String coreFunctionality) {
        this.coreFunctionality = coreFunctionality;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<ExpertProjectRating> getExpertProjectRatings() {
        return expertProjectRatings;
    }

    public void setExpertProjectRatings(List<ExpertProjectRating> expertProjectRatings) {
        this.expertProjectRatings = expertProjectRatings;
    }
}
