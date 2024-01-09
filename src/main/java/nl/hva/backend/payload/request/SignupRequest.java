package nl.hva.backend.payload.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import nl.hva.backend.models.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

public class SignupRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(max = 255)
    private String fullName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private User.Gender gender;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    @NotBlank
    @Size(max = 20)
    private String phoneNumber;


    @NotBlank
    @Size(max = 255)
    private String address;

    @NotBlank
    @Size(max = 255)
    private String selectedSector;

    @NotBlank
    @Size(max = 255)
    private String selectedClient;

    @NotBlank
    @Size(max = 255)
    private String selectedProject;

    @NotNull
    @Enumerated(EnumType.STRING)
    private User.Permit workPermit;

    @NotNull
    @Enumerated(EnumType.STRING)
    private User.Availability availability;

    @NotNull
    private Boolean hasPli;

    @Size(max = 255)
    private String specialConsiderations;

    @NotBlank
    @Size(max = 255)
    @Email
    private String email;

    @Size(max = 255)
    @Email
    private String secondaryEmail;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;

    private Set<String> role;

    private Set<String> tags;

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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public User.Gender getGender() {
        return gender;
    }

    public void setGender(User.Gender gender) {
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

    public User.Permit getWorkPermit() {
        return workPermit;
    }

    public void setWorkPermit(User.Permit workPermit) {
        this.workPermit = workPermit;
    }

    public User.Availability getAvailability() {
        return availability;
    }

    public void setAvailability(User.Availability availability) {
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

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
