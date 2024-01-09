package nl.hva.backend.rest;

import nl.hva.backend.models.Project;
import nl.hva.backend.models.Tag;
import nl.hva.backend.models.User;
import nl.hva.backend.repositories.ProjectRepository;
import nl.hva.backend.repositories.TagRepository;
import nl.hva.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "tags")
public class TagController {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "")
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @GetMapping(path = "{id}")
    public Tag getTagById(@PathVariable Long id) {
        return tagRepository.findById(id);
    }

    @PostMapping(path = "")
    public Tag createTag(@RequestBody Tag tag) {
        return tagRepository.save(tag);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateTag(@PathVariable("id") Long id, @RequestBody Tag updatedTag) {
        try {
            Tag existingTag = tagRepository.findById(id);
            if (existingTag == null) {
                return ResponseEntity.badRequest().body("Tag with ID " + id + " not found.");
            }

            existingTag.setTitle(updatedTag.getTitle());
            existingTag.setColor(updatedTag.getColor());

            Tag savedTag = tagRepository.save(existingTag);
            return ResponseEntity.ok(savedTag);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating tag: " + e.getMessage());
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<String> deleteTag(@PathVariable Long id) {
        try {
            Tag tag = tagRepository.findById(id);

            if (tag == null) {
                return ResponseEntity.badRequest().body("Tag with ID " + id + " not found.");
            }

            List<Project> projects = tag.getProjects();
            List<User> users = tag.getUsers();

            for (Project project : projects) {
                project.getTags().remove(tag);
                projectRepository.save(project);
            }

            for (User user : users) {
                user.getTags().remove(tag);
                userRepository.save(user);
            }

            tag.getProjects().clear();
            tag.getUsers().clear();

            tagRepository.save(tag);
            tagRepository.deleteById(tag.getId());

            return ResponseEntity.ok("Tag with ID " + id + " deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting tag: " + e.getMessage());
        }
    }
}
