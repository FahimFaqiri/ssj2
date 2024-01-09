package nl.hva.backend.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class StoredFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    @CreationTimestamp
    private LocalDateTime uploadedOn;

    @Lob
    @Column(name = "data", length = 4 * 1024 * 1024)
    private byte[] data;

    private boolean isCompressed;

    public StoredFile() {
    }

    public StoredFile(Long id, String name, String type, byte[] data) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(LocalDateTime uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isCompressed() {
        return isCompressed;
    }

    public void setCompressed(boolean compressed) {
        isCompressed = compressed;
    }
}
