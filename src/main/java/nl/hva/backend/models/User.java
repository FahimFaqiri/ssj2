package nl.hva.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import nl.hva.backend.models.views.ViewClasses;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {
    public enum Permit {
        DUTCH_NATIONALITY,
        PERMIT,
        OTHER
    }

    public enum Gender {
        MALE,
        FEMALE
    }

    public enum Availability {
        FULLTIME,
        PARTTIME,
        FLEXIBLE
    }

    // TODO: move to db
    public enum Sector {
        SOFTWARE_DEVELOPMENT("Software Development"),
        HARDWARE_MANUFACTURING("Hardware Manufacturing"),
        IT_CONSULTING("IT Consulting"),
        CLOUD_COMPUTING("Cloud Computing and Hosting"),
        CYBER_SECURITY("Cyber-Security"),
        DATA_ANALYSIS("Data Analysis and Business Intelligence"),
        NONE("None");

        public final String text;

        Sector(String text) {
            this.text = text;
        }
    }

    public enum Client {
        SMALL_BUSINESSES("Small Businesses"),
        STARTUPS("Startups"),
        ENTERPRISE("Enterprise Level Organizations"),
        ECOMMERCE("E-commerce"),
        HEALTHCARE("Healthcare"),
        FINANCE_AND_BANKING("Finance and Banking"),
        NONE("None");

        public final String text;

        Client(String text) {
            this.text = text;
        }
    }

    public enum Project {
        WEB_DEVELOPMENT("Web Development"),
        MOBILE_APP_DEVELOPMENT("Mobile App, Development"),
        UI_UX_DESIGN("UI/UX Design"),
        ECOMMERCE_INTEGRATION("E-commerce Integration"),
        MACHINE_LEARNING("Machine Learning"),
        AUGMENTED_VIRTUAL_REALITY("Augmented Reality/Virtual Reality"),
        NONE("None");

        public final String text;

        Project(String text) {
            this.text = text;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(ViewClasses.Shallow.class)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    @JsonView(ViewClasses.Shallow.class)
    private String username;

    @NotBlank
    @Size(max = 255)
    @JsonView(ViewClasses.Shallow.class)
    private String fullName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonView(ViewClasses.Shallow.class)
    private Gender gender;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonView(ViewClasses.Shallow.class)
    private LocalDate dateOfBirth;

    @NotBlank
    @Size(max = 20)
    @JsonView(ViewClasses.Shallow.class)
    private String phoneNumber;

    @NotBlank
    @Size(max = 255)
    @JsonView(ViewClasses.Shallow.class)
    private String address;

    @NotBlank
    @Size(max = 255)
    @JsonView(ViewClasses.Shallow.class)
    private String selectedSector;

    @NotBlank
    @Size(max = 255)
    @JsonView(ViewClasses.Shallow.class)
    private String selectedClient;

    @NotBlank
    @Size(max = 255)
    @JsonView(ViewClasses.Shallow.class)
    private String selectedProject;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonView(ViewClasses.Shallow.class)
    private Permit workPermit;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonView(ViewClasses.Shallow.class)
    private Availability availability;

    @NotNull
    @JsonView(ViewClasses.Shallow.class)
    private Boolean hasPli;

    @Size(max = 255)
    @JsonView(ViewClasses.Shallow.class)
    private String specialConsiderations;

    @NotBlank
    @Size(max = 255)
    @JsonView(ViewClasses.Shallow.class)
    @Email
    private String email;

    @Size(max = 255)
    @JsonView(ViewClasses.Shallow.class)
    @Email
    private String secondaryEmail;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;

    @JsonView(ViewClasses.Shallow.class)
    @Size(min = 0, max = 1024)
    @Column(length = 1024)
    private String aboutMeDescription;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonView(ViewClasses.Shallow.class)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonView(ViewClasses.Shallow.class)
    @JoinTable(
            name = "user_tag",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_recently_chatted_with_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recently_chatted_with_user_id")
    )
    private List<User> recentlyChattedWithUsers = new ArrayList<>();

    public User() {
    }

    public User(Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public User(
            Long id,
            String username,
            String fullName,
            Gender gender,
            LocalDate dateOfBirth,
            String phoneNumber,
            String address,
            String selectedSector,
            String selectedClient,
            String selectedProject,
            Permit workPermit,
            Availability availability,
            Boolean hasPli,
            String specialConsiderations,
            String email,
            String secondaryEmail,
            String password
    ) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.selectedSector = selectedSector;
        this.selectedClient = selectedClient;
        this.selectedProject = selectedProject;
        this.workPermit = workPermit;
        this.availability = availability;
        this.hasPli = hasPli;
        this.specialConsiderations = specialConsiderations;
        this.email = email;
        this.secondaryEmail = secondaryEmail;
        this.password = password;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSelectedSector() {
        return selectedSector;
    }

    public void setSelectedSector(String selectedSector) {
        this.selectedSector = selectedSector;
    }

    public String getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(String selectedClient) {
        this.selectedClient = selectedClient;
    }

    public String getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(String selectedProject) {
        this.selectedProject = selectedProject;
    }

    public Permit getWorkPermit() {
        return workPermit;
    }

    public void setWorkPermit(Permit workPermit) {
        this.workPermit = workPermit;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public Boolean getHasPli() {
        return hasPli;
    }

    public void setHasPli(Boolean hasPli) {
        this.hasPli = hasPli;
    }

    public String getSpecialConsiderations() {
        return specialConsiderations;
    }

    public void setSpecialConsiderations(String specialConsiderations) {
        this.specialConsiderations = specialConsiderations;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<User> getRecentlyChattedWithUsers() {
        return recentlyChattedWithUsers;
    }

    public void setRecentlyChattedWithUsers(List<User> recentlyChattedWithUsers) {
        this.recentlyChattedWithUsers = recentlyChattedWithUsers;
    }

    public String getAboutMeDescription() {
        return aboutMeDescription;
    }

    public void setAboutMeDescription(String aboutMeDescription) {
        this.aboutMeDescription = aboutMeDescription;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", gender=").append(gender);
        sb.append(", dateOfBirth=").append(dateOfBirth);
        sb.append(", phoneNumber='").append(phoneNumber).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", selectedSector='").append(selectedSector).append('\'');
        sb.append(", selectedClient='").append(selectedClient).append('\'');
        sb.append(", selectedProject='").append(selectedProject).append('\'');
        sb.append(", workPermit=").append(workPermit);
        sb.append(", availability=").append(availability);
        sb.append(", hasPli=").append(hasPli);
        sb.append(", specialConsiderations='").append(specialConsiderations).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", secondaryEmail='").append(secondaryEmail).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", roles=").append(roles);
        sb.append(", tags=").append(tags);
        sb.append(", recentlyChattedWithUsers=").append(recentlyChattedWithUsers);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof User user)) return false;

        if (!id.equals(user.id)) return false;
        return username.equals(user.username);
    }
}
