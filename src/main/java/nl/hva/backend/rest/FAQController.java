package nl.hva.backend.rest;

import nl.hva.backend.exceptions.ResourceNotFound;
import nl.hva.backend.models.FAQ;
import nl.hva.backend.repositories.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("public/faq")
public class FAQController {
    @Autowired
    private BaseRepository<FAQ> faqRepository;

        @GetMapping(path = "")
        public List<FAQ> getAllFAQ() {
            return faqRepository.findAll();
        }

        // Get the id of FAQ
        @GetMapping(path = "/{id}")
        public FAQ getFaqById(@PathVariable long id) {
            FAQ faq = faqRepository.findById(id);
            if (faq == null) {
                throw new ResourceNotFound("The Id: " + id + "that you are looking for is not found");
            }
            return faq;
        }


        // Add FAQ
        @PostMapping
        public ResponseEntity<Object> addFAQ(@RequestBody FAQ faq) {
            if (faqRepository.findById(faq.getId()) != null) {
                return ResponseEntity.badRequest().build();
            } else {
                FAQ addedFAQ = faqRepository.save(faq);

                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(faq.getId())
                        .toUri();

                return ResponseEntity.created(location).body(addedFAQ);
            }
        }

        // Update FAQ
        @PutMapping(path = "/{id}")
        public FAQ updateFAQ(@PathVariable long id, @RequestBody FAQ faq) {
            if (id != faq.getId()) {
                throw new ResourceNotFound(" Path id does not match ");
            }
            return faqRepository.save(faq);
        }

        // Delete FAQ
        @DeleteMapping(path = "/{id}")
        public FAQ deleteFAQ(@PathVariable long id) {
            if (faqRepository.findById(id) == null) {
                throw new ResourceNotFound("id: " + id + " not found");
            }
            return faqRepository.deleteById(id);
        }


}
