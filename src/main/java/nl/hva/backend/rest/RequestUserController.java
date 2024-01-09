package nl.hva.backend.rest;

import nl.hva.backend.models.Invitation;
import nl.hva.backend.models.Notification;
import nl.hva.backend.models.RequestUser;
import nl.hva.backend.repositories.InvitationRepository;
import nl.hva.backend.repositories.NotificationRepository;
import nl.hva.backend.repositories.RequestUserRepository;
import nl.hva.backend.services.EmailService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("requestUser")
public class RequestUserController {

    @Autowired
    private RequestUserRepository requestUserRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private EmailService emailService;


    @GetMapping("all")
    public List<RequestUser> getAllRequestUsers() {
        return requestUserRepository.findAll();
    }

    @GetMapping("accepted")
    public List<RequestUser> getALlAcceptedRequestUsers() {
       return requestUserRepository.findAcceptedRequestUsers();
    }

    @GetMapping("denied")
    public List<RequestUser> getAllDeniedRequestUsers() {
        return requestUserRepository.findDeniedRequestUsers();
    }

    @GetMapping("")
    public List<RequestUser> getRequestUsers() {
        return requestUserRepository.findRequestUsers();
    }

    @PostMapping("")
    public ResponseEntity<RequestUser> createRequestUser(@RequestBody RequestUser requestUser) {

        Notification notification = Notification.createRequestNotification(null);

        notificationRepository.save(notification);

        requestUser.setAccepted(false);
        requestUser.setDenied(false);
        requestUser.setNotification(notification);

        return ResponseEntity.ok(requestUserRepository.save(requestUser));
    }

    @PostMapping("{id}/mail")
    public ResponseEntity<RequestUser> denyRequestUser(@PathVariable long id) {
        RequestUser requestUser = requestUserRepository.findById(id);
        Notification notification = Notification.createDeniedNotification();

        requestUser.setNotification(notification);

        notificationRepository.save(notification);


        requestUser.setDenied(true);
        requestUser.setAccepted(false);

        requestUserRepository.save(requestUser);

        String fullname = requestUser.getFirstName() + " " + requestUser.getLastName();

        String from = "peekprospects@gmail.com";
        String to = requestUser.getEmail();
        String subject = "Application Status: Expert Registration";
        String text = "<h2>Expert Registration Application Status</h2>" +
                "<p>Dear " + fullname + "," +
                "<p>We appreciate your interest in becoming an Expert on our platform. After careful consideration, we regret to inform you that your application has not been accepted at this time.</p>" +
                "<p>We received many applications, and while we recognize your qualifications, we have selected candidates whose expertise align more closely with our current needs. We encourage you to continue developing your skills and expertise, and we welcome you to apply again in the future.</p>" +
                "<p>Thank you for your understanding, and we wish you the best.</p>" +
                "<p>Best regards,<br>PeekProspects</p>";

        emailService.sendSimpleMessage(from, to, subject, text);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public RequestUser deleteUser(@PathVariable long id) {
       return requestUserRepository.deleteById(id);
    }


}
