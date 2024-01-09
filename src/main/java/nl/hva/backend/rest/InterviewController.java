package nl.hva.backend.rest;


import nl.hva.backend.exceptions.ResourceNotFound;
import nl.hva.backend.models.Interview;
import nl.hva.backend.repositories.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "interviews")
public class InterviewController {
    @Autowired
    private InterviewRepository interviewRepository;

    @GetMapping(path = "")
    public List<Interview> getAllInterviews() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return interviewRepository.findAll();
        }

        return List.of();
    }

    @GetMapping(path = "/expert/{id}")
    public List<Interview> getAllInterviewsFromExpert(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return interviewRepository.findInterviewsByBelongsToUser(id);
        }

        return List.of();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getInterviewById(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<Interview> interview = interviewRepository.findById(id);
            if (interview.isPresent()) {
                return ResponseEntity.ok(interview);
            }

            throw new ResourceNotFound(String.format("Interview with id=%d doesn't exist.", id));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping(path = "")
    public ResponseEntity<?> createInterview(@RequestBody Interview interview) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(interviewRepository.save(interview));
        }

        return ResponseEntity.badRequest().body(interview);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateInterviewById(@PathVariable("id") Long id, @RequestBody Interview interview) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<Interview> interviewFromDB = interviewRepository.findById(id);
            if (interviewFromDB.isEmpty()) {
                throw new ResourceNotFound(String.format("Interview with id=%d doesn't exist.", id));
            }

            return ResponseEntity.ok(interviewRepository.save(interview));
        }

        return ResponseEntity.badRequest().body(interview);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteInterviewById(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<Interview> interviewFromDB = interviewRepository.findById(id);
            if (interviewFromDB.isEmpty()) {
                throw new ResourceNotFound(String.format("Interview with id=%d doesn't exist.", id));
            }

            interviewRepository.deleteById(id);
            return ResponseEntity.ok("Deleted interview with id=" + id);
        }

        return ResponseEntity.badRequest().body(false);
    }
}
