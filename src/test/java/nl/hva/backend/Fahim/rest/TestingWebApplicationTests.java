package nl.hva.backend.Fahim.rest;

import nl.hva.backend.models.Client;
import nl.hva.backend.models.Project;
import nl.hva.backend.repositories.ClientRepository;
import nl.hva.backend.repositories.ProjectRepository;
import nl.hva.backend.rest.ClientController;
import nl.hva.backend.rest.ProjectController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest
public class TestingWebApplicationTests {
    @Autowired
    private ProjectController projectController;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientController clientController;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void contextLoads() {
        assertThat(projectController).isNotNull();
        assertThat(projectRepository).isNotNull();
        assertThat(clientController).isNotNull();
        assertThat(clientRepository).isNotNull();
    }

    @Test
    void testGetProjectById() {
        Long existingProjectId = 1L;
        ResponseEntity<Project> responseEntity = projectController.getProjectById(existingProjectId);
        assertThat(responseEntity).isNotNull();
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    void testCreateProject() {
        Client client = new Client();
        clientRepository.save(client);

        Project project = new Project(
                null,
                "Amsterdam",
                "Project One",
                "2023-11-22",
                "This is the first dummy project",
                "Web Development",
                "project1.jpg",
                "Content for Project One",
                client,
                null
        );

        ResponseEntity<?> responseEntity = projectController.createProject(project);
        assertThat(responseEntity).isNotNull();
        assertEquals(HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    void testFindAllClients() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).isNotNull();
    }

    @Test
    void testFindProjectById() {
        Long existingProjectId = 1L;
        Project project = projectRepository.findById(existingProjectId);
        assertThat(project).isNotNull();
    }
}