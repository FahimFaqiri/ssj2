package nl.hva.backend.models;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import nl.hva.backend.enums.UserRole;
import nl.hva.backend.models.views.ViewClasses;

@Entity
@Table(name = "roles")
@JsonView(ViewClasses.Shallow.class)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserRole name;

    public Role() {
    }

    public Role(UserRole name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRole getName() {
        return name;
    }

    public void setName(UserRole name) {
        this.name = name;
    }
}
