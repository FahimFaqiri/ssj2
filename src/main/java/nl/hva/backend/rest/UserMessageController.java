package nl.hva.backend.rest;


import com.fasterxml.jackson.annotation.JsonView;
import nl.hva.backend.models.Message;
import nl.hva.backend.models.User;
import nl.hva.backend.models.views.ViewClasses;
import nl.hva.backend.repositories.MessageRepository;
import nl.hva.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("messages")
public class UserMessageController {

    private static final Logger logger = LoggerFactory.getLogger(UserMessageController.class);

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Helper method
     *
     * @param user
     * @param recentlyChattedWithUser
     */
    private void addRecentlyChattedWithUser(User user, User recentlyChattedWithUser) {
        if (!user.getRecentlyChattedWithUsers().contains(recentlyChattedWithUser)) {
            user.getRecentlyChattedWithUsers().add(recentlyChattedWithUser);
            userRepository.save(user);
        }
    }

    /**
     * To send a message to someone
     *
     * @param to      - username of whom the message will be sent to
     * @param message - the message
     */
    @MessageMapping("/send/{to}")
    public void sendMessage(@DestinationVariable String to, @Payload Message message) {
        if (!message.getReceiver().equals(to)) {
            // what???
            return;
        }

        final Optional<User> sender = userRepository.findByUsername(message.getSender());
        final Optional<User> receiver = userRepository.findByUsername(message.getReceiver());
        if (sender.isEmpty() || receiver.isEmpty()) {
            // who are you guys???
            return;
        }

        // add to recently chatted with
        addRecentlyChattedWithUser(sender.get(), receiver.get());
        addRecentlyChattedWithUser(receiver.get(), sender.get());

        // ok
        message = messageRepository.save(message);
        template.convertAndSend(String.format("/queue/messages/%s", to), message);
    }

    /**
     * @param username
     * @return
     */
    @GetMapping("/history/with/{username}")
    public ResponseEntity<?> getMessageHistoryWith(@PathVariable("username") String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            if (requester.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            List<Message> messageHistory = messageRepository.findMessageHistory(requester.get().getUsername(), username);
            return ResponseEntity.ok(messageHistory);
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * @param username
     * @return
     */
    @GetMapping("last/with/{username}")
    public ResponseEntity<?> getLastMessageWith(@PathVariable("username") String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            if (requester.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            Optional<Message> lastMessage = messageRepository.findLastMessageBetween(requester.get().getUsername(), username);
            return ResponseEntity.ok(lastMessage.isPresent() ? lastMessage : null); // NULL IS OKAY!
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * @param username
     * @return
     */
    @GetMapping("/recents/{username}/unread")
    public ResponseEntity<?> getUnreadMessagesWith(@PathVariable("username") String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            if (requester.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // I love and hate jpa at the same time (please don't check out this interface)
            List<Message> unreadMessages = messageRepository.findUnreadMessagesWhereSenderAndReceiver(username, requester.get().getUsername());
            return ResponseEntity.ok(unreadMessages);
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * @param username
     * @return
     */
    @PutMapping("/recents/{username}/read/all")
    public ResponseEntity<?> markAllMessagesAsReadFrom(@PathVariable("username") String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            if (requester.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            messageRepository.markAllMessagesReadWhereSenderAndReceiver(username, requester.get().getUsername());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * @return
     */
    @JsonView(ViewClasses.PublicSafe.class)
    @GetMapping("/recents")
    public ResponseEntity<?> getRecents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            if (requester.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            final List<User> recents = requester.get().getRecentlyChattedWithUsers();
            return ResponseEntity.ok(recents);
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * @param user
     * @return
     */
    @JsonView(ViewClasses.PublicSafe.class)
    @PostMapping("/recents")
    public ResponseEntity<?> addRecentFromRecents(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            if (requester.isEmpty()) {
                return ResponseEntity.badRequest().body("Requester doesn't exist.");
            }

            // check if new recent exists / not faulty data was supplied
            final Optional<User> newRecent = userRepository.findById(user.getId());
            if (newRecent.isEmpty()) {
                return ResponseEntity.badRequest().body("New recent user doesn't exist.");
            }

            // save data
            requester.get().getRecentlyChattedWithUsers().add(newRecent.get());
            userRepository.save(requester.get());

            // return newly added recent
            return ResponseEntity.ok(newRecent.get());
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * @param user
     * @return
     */
    @DeleteMapping("/recents")
    public ResponseEntity<?> removeRecentFromRecents(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            if (requester.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            requester.get().getRecentlyChattedWithUsers().remove(user);
            userRepository.save(requester.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }
}
