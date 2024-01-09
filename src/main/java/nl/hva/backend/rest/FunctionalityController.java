package nl.hva.backend.rest;


import nl.hva.backend.models.Functionality;
import nl.hva.backend.repositories.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "functionalities")
public class FunctionalityController {
    @Autowired
    private BaseRepository<Functionality> functionalityRepository;

    @GetMapping(path = "")
    public List<Functionality> getAllFunctionalities() {
        return functionalityRepository.findAll();
    }
}
