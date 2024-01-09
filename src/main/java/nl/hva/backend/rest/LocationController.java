package nl.hva.backend.rest;


import nl.hva.backend.models.Client;
import nl.hva.backend.models.Location;
import nl.hva.backend.repositories.BaseRepository;
import nl.hva.backend.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "locations")
public class LocationController {
    @Autowired
    private LocationRepository locationRepository;

    @GetMapping(path = "")
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}
