package nl.hva.backend.rest;

import nl.hva.backend.models.Client;
import nl.hva.backend.models.Project;
import nl.hva.backend.repositories.BaseRepository;
import nl.hva.backend.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "projects")
public class ProjectController {
    private final BaseRepository<Project> projectRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ProjectController(BaseRepository<Project> projectRepository, ClientRepository clientRepository) {
        this.projectRepository = projectRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping(path = "")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return ResponseEntity.ok(projects);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectRepository.findById(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        try {
            // Save project
            Project savedProject = projectRepository.save(project);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating project: " + e.getMessage());
        }
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateProject(@PathVariable("id") Long id, @RequestBody Project project) {
        try {
            // Check if the client exists
            Optional<Client> optionalClient = clientRepository.findById(project.getClient().getId());
            if (optionalClient.isEmpty()) {
                return ResponseEntity.badRequest().body("Client with ID " + project.getClient().getId() + " not found.");
            }

            // Save project
            project.setId(id);
            Project savedProject = projectRepository.save(project);
            return ResponseEntity.ok(savedProject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating project: " + e.getMessage());
        }
    }
}
