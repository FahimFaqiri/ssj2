package nl.hva.backend.rest;

import nl.hva.backend.exceptions.PreConditionFailedException;
import nl.hva.backend.exceptions.ResourceNotFound;
import nl.hva.backend.models.Notification;
import nl.hva.backend.models.RequestUser;
import nl.hva.backend.models.User;
import nl.hva.backend.repositories.NotificationRepository;
import nl.hva.backend.repositories.RequestUserRepository;
import nl.hva.backend.repositories.UserRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("notifications")
public class NotificationController {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepo;




    @GetMapping(path = "")
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @GetMapping("expert")
    public List<Notification> getExpertNotifications() {
        return notificationRepository.findExpertNotifications();
    }

    @GetMapping("{username}/user")
    public List<Notification> getUserNotificationByUsername(@PathVariable String username) {

        Optional<User> userOptional = userRepo.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();


             return notificationRepository.findByUserId(user.getId());
        }

        throw new ResourceNotFound("User with username " + username + " not found");

    }

    @PostMapping(path = "")
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        return ResponseEntity.ok(notificationRepository.save(notification));
    }

//    @PostMapping("/{username}/user")
//    public ResponseEntity<Notification> sendEditNotification(@PathVariable String username) {
//        Optional<User> userOptional = userRepo.findByUsername(username);
//
//        List<Notification> notifications = notificationRepository.findAll();
//
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            for (Notification notification : notifications) {
//                if (notification.getUser() == user) {
//                    throw new PreConditionFailedException(
//                            String.format("User already send an Edit request and is still pending")
//                    );
//                }
//            }
//
//            Notification notification = new Notification();
//            notification.setUser(user);
//            notification.setMessage(user.getFullName() + " wants to make changes to the profile");
//            notification.setType("Edit Request");
//            notification.setHasPermission(false);
//
//            notificationRepository.save(notification);
//
//            return ResponseEntity.ok().body(notification);
//
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//
//    }



//    @PutMapping("{id}/id/{username}/user")
//    public ResponseEntity<Notification> updatePermission(@PathVariable String username, @PathVariable long id ,@RequestBody Notification notification) {
//
//        Optional<User> userOptional = userRepo.findByUsername(username);
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            if (!Objects.equals(username, user.getUsername())) {
//                throw new PreConditionFailedException("Username in path doesn't match the username in the request body");
//            }
//
//            Notification updateNotification = notificationRepository.findById(id);
//
//            updateNotification.setHasPermission(notification.isHasPermission());
//
//            notificationRepository.save(updateNotification);
//
//            return ResponseEntity.ok().body(updateNotification);
//
//        }
//
//        return ResponseEntity.notFound().build();
//
//    }

    @DeleteMapping("{id}")
    public ResponseEntity<Notification> deleteById(@PathVariable long id) {

        Notification notification = notificationRepository.deleteById(id);

        if (notification == null) {
            throw new ResourceNotFound("Offer with ID " + id + " not found");
        }

        return ResponseEntity.ok().body(notification);
    }



}

