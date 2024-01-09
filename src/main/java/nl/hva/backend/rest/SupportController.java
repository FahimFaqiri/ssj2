package nl.hva.backend.rest;

import nl.hva.backend.exceptions.PreConditionFailedException;
import nl.hva.backend.exceptions.ResourceNotFound;
import nl.hva.backend.models.Support;
import nl.hva.backend.repositories.SupportRepository;
import nl.hva.backend.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("support")
public class SupportController {

    @Autowired
    private SupportRepository supportRepository;
    @Autowired
    private EmailService emailService;

    //GET
    @GetMapping(path = "/{id}")
    public Support getSupportById(@PathVariable long id) {
        Support support = supportRepository.findById(id);
        if (support == null) {
            throw new ResourceNotFound("id: " + id + " not found");
        }
        return support;
    }

    //FIND ALL SUPPORT
    @GetMapping(path = "")
    public List<Support> getAllSupports() {
        return this.supportRepository.findAll();
    }

    //POST
    @PostMapping(path = "")
    public ResponseEntity<Object> addSupport(@RequestBody Support support) {
        Support addedSupport = supportRepository.save(support);

        return ResponseEntity.ok(addedSupport);
    }

    //PUT
    @PutMapping(path = "/{id}")
    public Support updateSupport(@PathVariable long id, @RequestBody Support support) {
        if (id != support.getId()) {
            throw new PreConditionFailedException(" Path id does not match ");
        }
        Support updatedSupport = supportRepository.save(support);
        return updatedSupport;
    }


    //DELETE
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteSupport(@PathVariable long id) {
        if (supportRepository.findById(id) == null) {
            throw new ResourceNotFound("id: " + id + " not found");
        }
        supportRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    //Fetch all the support messages for the broker's page
    @GetMapping("/supportMessages")
    public List<Support> recieveAllSupport() {
        return this.supportRepository.findAll();
    }

    @GetMapping("/fetchSupportMessages")
    public ResponseEntity<List<Support>> fetchSupportMessages() {
        try {
            List<Support> messages = this.supportRepository.findAll();
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/send")
    public ResponseEntity<Object> sendEmail(@RequestBody Support message) {
        try {
            String from = "peekprospects@gmail.com";
            String to = message.getToEmail();
            String subject = message.getSubject();

            if (message.getExpertMessage() != null && !message.getExpertMessage().trim().isEmpty()) {
                String text = "Hello " + message.getUsername() + ",<br><br>" +
                         message.getBrokerAnswer() + "<br><br>" +
                        "Best regards, <br><br> The Peek team";

                if (message.getBrokerAnswer() != null) {
                    emailService.sendSimpleMessage(from, to, subject, text);

                    // Delete answered support message by ID
                    if (message.getId() != null) {
                        supportRepository.deleteById(message.getId());
                        return ResponseEntity.ok("Email sent successfully, and support message deleted");
                    } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Message id is null");
                    }
                } else {
                    return ResponseEntity.badRequest().body("Sending email not successful");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request");
        }
        return ResponseEntity.badRequest().body("Invalid expert message");
    }
}
