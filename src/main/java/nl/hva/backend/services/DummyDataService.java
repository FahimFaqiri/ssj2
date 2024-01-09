package nl.hva.backend.services;

import jakarta.annotation.PostConstruct;
import nl.hva.backend.enums.TagColor;
import nl.hva.backend.models.Project;
import nl.hva.backend.models.Tag;
import nl.hva.backend.repositories.ClientRepository;
import nl.hva.backend.repositories.ProjectRepository;
import nl.hva.backend.repositories.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class DummyDataService {

    private final ProjectRepository projectRepository;
    private final ClientRepository expertRepository;
    private final TagRepository tagRepository;

    public DummyDataService(
            ProjectRepository projectRepository,
            ClientRepository expertRepository,
            TagRepository tagRepository
    ) {
        this.projectRepository = projectRepository;
        this.expertRepository = expertRepository;
        this.tagRepository = tagRepository;
    }

    public void addDummyData() {
        // Creating dummy tags
        Tag tag1 = new Tag(null, "Tag1", TagColor.PRIMARY.getColorClass());
        Tag tag2 = new Tag(null, "Tag2", TagColor.SUCCESS.getColorClass());

        Tag tag3 = new Tag(null, "Tag3", TagColor.WARNING.getColorClass());
        Tag tag4 = new Tag(null, "Tag4", TagColor.INFO.getColorClass());
        // ... create more tags as needed

        // Saving tags to the database
        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);
        tagRepository.save(tag4);
        // ...

        // Creating a dummy project
        Project project = new Project();

        // Associating tags with the project
        project.getTags().add(tag1);
        project.getTags().add(tag2);
        project.getTags().add(tag3);
        project.getTags().add(tag4);


        // Saving the project to the database
        projectRepository.save(project);
    }

    @PostConstruct
    public void addDummyDataOnStartup() {
        // addDummyData();
    }
}
