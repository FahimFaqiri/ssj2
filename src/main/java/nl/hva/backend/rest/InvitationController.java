package nl.hva.backend.rest;

import nl.hva.backend.models.Invitation;
import nl.hva.backend.models.Notification;
import nl.hva.backend.models.RequestUser;
import nl.hva.backend.repositories.BaseRepository;
import nl.hva.backend.repositories.InvitationRepository;
import nl.hva.backend.repositories.NotificationRepository;
import nl.hva.backend.repositories.RequestUserRepository;
import nl.hva.backend.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "invitations")
public class InvitationController {
    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RequestUserRepository requestUserRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping(path = "")
    public List<Invitation> getAllInvitations() {
        return invitationRepository.findAll();
    }

//    @GetMapping(path = "{id}")
//    public Invitation getInvitationById(@PathVariable Long id) {
//        return invitationRepository.findById(id);
//    }

    @GetMapping(path = "{uniqueCode}")
    public Invitation getInvitationByUniqueCode(@PathVariable String uniqueCode) {
        return invitationRepository.findByUniqueCode(uniqueCode);
    }

    @PostMapping(path = "")
    public Invitation createInvitation(@RequestBody Invitation invitation) {
        String from = "peekprospects@gmail.com";
        String to = invitation.getRecipientEmail();
        String subject = "Invitation from Peek to register on the website";
        String text = "<p>Dear recipient,</p><br>" +
                "<p>You have been invited by Peek to register on our website. Kindly follow the link below to access the registration page:</p><br>" +
                "<p><a href='http://localhost:8080/#/register/" + invitation.getUniqueCode() + "'>Registration Link</a></p><br>" +
                "<p>Best regards,<br>PeekProspects</p>";
        emailService.sendSimpleMessage(from, to, subject, text);

        return invitationRepository.save(invitation);
    }

    @PostMapping("{id}/requestUser")
    public Invitation createInvitationForRequestUser(@PathVariable Long id, @RequestBody Invitation invitation) {
        RequestUser requestUser = requestUserRepository.findById(id);
        Notification notification = Notification.createAcceptedNotification();

        requestUser.setNotification(notification);

        notificationRepository.save(notification);

        requestUser.setAccepted(true);
        requestUser.setDenied(false);
        requestUserRepository.save(requestUser);

        invitation.setRecipientEmail(requestUser.getEmail());


        String fullname = requestUser.getFirstName() + " " + requestUser.getLastName();

        String from = "peekprospects@gmail.com";
        String to = requestUser.getEmail();
        String subject = "You've Been Accepted as an Expert";
        String text = "<h2>Congratulations! You've Been Accepted as an Expert</h2>" +
                "<p>Dear " + fullname + "," +
                "<p>We are delighted to inform you that you have officially been accepted as an Expert on our platform. Your knowledge and skills will be a valuable addition to our growing network of experts, and we are excited to collaborate with you.<p>" +
                "<p>As an Expert, you now have the opportunity to complete your registration and gain access to our exclusive platform. Click the link below to set up your account and start sharing your expertise:<p>" +
                "<p><a href='http://localhost:8080/#/register/" + invitation.getUniqueCode() + "'>Registration Link</a></p><br>" +
                "<p>Best regards,<br>PeekProspects</p>";

        emailService.sendSimpleMessage(from, to, subject, text);

        return invitationRepository.save(invitation);
    }

    @DeleteMapping(path = "{id}")
    public Invitation deleteInvitation(@PathVariable Long id) {
        return invitationRepository.deleteById(id);
    }
}
