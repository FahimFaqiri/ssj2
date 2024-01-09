package nl.hva.backend.rest;


import nl.hva.backend.exceptions.ResourceNotFound;
import nl.hva.backend.models.Client;
import nl.hva.backend.repositories.BaseRepository;
import nl.hva.backend.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "clients")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping(path = "")
    public List<Client> getAllClients() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return clientRepository.findAll();
        }

        return List.of();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getClientById(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<Client> client = clientRepository.findById(id);
            if (client.isPresent()) {
                return ResponseEntity.ok(client);
            }

            throw new ResourceNotFound(String.format("Client with id=%d doesn't exist.", id));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createClient(@RequestBody Client client) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(clientRepository.save(client));
        }

        return ResponseEntity.badRequest().body(client);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateClientById(@PathVariable("id") Long id, @RequestBody Client client) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<Client> clientFromDB = clientRepository.findById(id);
            if (clientFromDB.isEmpty()) {
                throw new ResourceNotFound(String.format("Client with id=%d doesn't exist.", id));
            }

            return ResponseEntity.ok(clientRepository.save(client));
        }

        return ResponseEntity.badRequest().body(client);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteClientById(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<Client> clientFromDB = clientRepository.findById(id);
            if (clientFromDB.isEmpty()) {
                throw new ResourceNotFound(String.format("Client with id=%d doesn't exist.", id));
            }

            clientRepository.deleteById(id);
            return ResponseEntity.ok("Deleted client with id=" + id);
        }

        return ResponseEntity.badRequest().body(false);
    }
}
