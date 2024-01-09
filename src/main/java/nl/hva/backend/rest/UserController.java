package nl.hva.backend.rest;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import nl.hva.backend.enums.UserRole;
import nl.hva.backend.exceptions.ResourceNotFound;
import nl.hva.backend.models.*;
import nl.hva.backend.models.views.ViewClasses;
import nl.hva.backend.repositories.*;
import nl.hva.backend.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ExpertProjectRatingRepository expertProjectRatingRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private StorageService storageService;

    /**
     * Gets all users
     *
     * @return
     */
    @GetMapping(path = "")
    public List<User> getAll() {
        return userRepository.findAllUsersWithTags();
    }

    /**
     * Gets all experts
     *
     * @return
     */
    @JsonView(ViewClasses.PublicSafe.class)
    @GetMapping(path = "/experts")
    public List<User> getAllExperts(
            @RequestParam("size") @DefaultValue("0") Optional<Integer> pageSize,
            @RequestParam("page") @DefaultValue("0") Optional<Integer> pageNumber,
            @RequestParam("sector") @DefaultValue("ALL") Optional<String> sector,
            @RequestParam("client") @DefaultValue("ALL") Optional<String> client,
            @RequestParam("project") @DefaultValue("ALL") Optional<String> project
    ) {
        // get expert role to sort by role
        Role expertRole = roleRepository
                .findByName(UserRole.ROLE_EXPERT)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // request size
        Pageable page = Pageable.unpaged();
        if (pageNumber.isPresent() && pageSize.isPresent()) {
            page = PageRequest.of(pageNumber.get(), pageSize.get());
        }

        // do request and return values
        return userRepository.findUsersByRolesContainingAndSelectedSectorAndSelectedClientAndSelectedProject(
                expertRole,
                sector.orElse("ALL"),
                client.orElse("ALL"),
                project.orElse("ALL"),
                page
        );
    }

    /**
     * Gets all brokers
     *
     * @return
     */
    @JsonView(ViewClasses.PublicSafe.class)
    @GetMapping(path = "/brokers")
    public List<User> getAllBrokers() {
        // get broker role from repo
        Role brokerRole = roleRepository
                .findByName(UserRole.ROLE_BROKER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // authenticate user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final String username = authentication.getName();
            final Optional<User> requester = userRepository.findByUsername(username);

            // check if user exists and if role is broker
            if (requester.isPresent() && requester.get().getRoles().contains(brokerRole)) {
                return userRepository.findUsersByRolesContaining(brokerRole);
            }
        }

        // either not authenticated or something funny happened
        return List.of();
    }

    /**
     * Only allowed to search for experts
     *
     * @param partialUsername
     * @param pageSize
     * @return
     */
    @JsonView(ViewClasses.PublicSafe.class)
    @GetMapping(path = "/search/{partialUsername}")
    public ResponseEntity<?> searchForUserByUsername(
            @PathVariable("partialUsername") String partialUsername,
            @RequestParam("size") @DefaultValue("0") Integer pageSize
    ) {
        final Role expertRole = roleRepository.findByName(UserRole.ROLE_EXPERT).orElseThrow(() -> new RuntimeException("Role not found"));
        final Pageable page = PageRequest.of(0, pageSize);
        final List<User> users = userRepository.findUsersByRolesContainingAndUsernameContainingIgnoreCase(expertRole, partialUsername, page);
        return ResponseEntity.ok(users);
    }

    /**
     * Get data from a profile, meant for visiting someone else's profile.
     * NOTE: we should probably filter out some more data
     *
     * @param username
     * @return
     */
    @JsonView(ViewClasses.PublicSafe.class)
    @GetMapping(path = "/p/{username}")
    public ResponseEntity<?> getUserById(@PathVariable("username") String username) {
        return ResponseEntity.ok(userRepository.findByUsername(username));
    }

    /**
     * Get user by data through authentication
     *
     * @param user
     * @return
     */
    @JsonView(ViewClasses.PublicSafe.class)
    @GetMapping(path = "/profile")
    public ResponseEntity<?> getUser(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final String username = authentication.getName();
            return ResponseEntity.ok(userRepository.findByUsername(username));
        }

        return (ResponseEntity<?>) ResponseEntity.badRequest();
    }

    /**
     * Get user by id
     *
     * @param id
     * @return
     */
    @JsonView(ViewClasses.PublicSafe.class)
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        final Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.badRequest().build();
    }


    /**
     * Update user by id
     *
     * @param id
     * @param user
     * @return
     */
    @JsonView(ViewClasses.PublicSafe.class)
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<User> userFromDB = userRepository.findById(id);
            if (userFromDB.isEmpty()) {
                throw new ResourceNotFound(String.format("User with id=%d doesn't exist.", id));
            }

            // TODO: make copy constructor method
            user.setId(userFromDB.get().getId());
            user.setPassword(userFromDB.get().getPassword());
            user.setRoles(userFromDB.get().getRoles());

            return ResponseEntity.ok(userRepository.save(user));
        }

        return ResponseEntity.badRequest().body(user);
    }


    /**
     * Delete user by id
     *
     * @param id
     * @return
     */
    @JsonView(ViewClasses.PublicSafe.class)
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<User> userFromDB = userRepository.findById(id);
            if (userFromDB.isEmpty()) {
                throw new ResourceNotFound(String.format("User with id=%d doesn't exist.", id));
            }

            userRepository.deleteById(id);
            return ResponseEntity.ok("Deleted user with id=" + id);
        }

        return ResponseEntity.badRequest().body("???");
    }

    /**
     * Where users can download the profile photo from
     *
     * @param userId
     * @return
     */
    @GetMapping("/profile/{id}/photo")
    public ResponseEntity<?> getUserProfilePhoto(@PathVariable("id") Long userId) {
        byte[] image = storageService.download(String.format("pfp_%d", userId)); // auto-detects if file was compressed, don't worry!
        if (image == null) {
            return ResponseEntity.badRequest().body("Couldn't download file.");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }


    /**
     * Where users can upload their profile photo to
     *
     * @param userId
     * @param image
     * @return
     * @throws IOException
     */
    @PostMapping("/profile/{id}/photo")
    public ResponseEntity<?> setUserProfilePhoto(@PathVariable("id") Long userId, @RequestParam("image") MultipartFile image) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            final Optional<User> userFromUrl = userRepository.findById(userId);

            // make sure the that person who wants to request is the same person who's pfp is going to change
            if (requester.isEmpty() || userFromUrl.isEmpty() || !requester.get().equals(userFromUrl.get())) {
                return ResponseEntity.badRequest().body("Couldn't authenticate user.");
            }
        }

        // everything is okay
        final boolean useFileCompression = true;
        String uploadImage = storageService.upload(image, String.format("pfp_%d", userId), useFileCompression);
        if (uploadImage == null) {
            return ResponseEntity.badRequest().body("Couldn't upload file.");
        }

        return ResponseEntity.ok().body(uploadImage);
    }


    /**
     * Retrieve a single comment from an expert's page.
     * NOTE that the expert's id and the comment's id are not linked.
     * A comments id does NOT start with 1 even if it's the first comment on an expert's profile.
     *
     * @param id
     * @return
     */
    @GetMapping("/profile/{id}/comments/{commentId}")
    public ResponseEntity<?> getCommentOnProfileByIds(@PathVariable("id") Long id, @PathVariable("commentId") Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            final Optional<User> user = userRepository.findById(id);

            if (requester.isPresent() && user.isPresent()) {
                final Optional<Comment> comment = commentRepository.findById(commentId);
                if (comment.isPresent()) {
                    return ResponseEntity.ok(comment.get());
                } else {
                    return ResponseEntity.notFound().build();
                }
            }
        }
        return ResponseEntity.badRequest().build();
    }


    @DeleteMapping("/profile/{id}/comments/{commentId}")
    public ResponseEntity<?> deleteCommentOnProfileByIds(@PathVariable("id") Long id, @PathVariable("commentId") Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            final Optional<User> user = userRepository.findById(id);

            if (requester.isPresent() && user.isPresent() && commentRepository.existsById(commentId)) {
                commentRepository.deleteById(commentId);
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();
    }


    /**
     * Retrieve comments on an expert's page
     *
     * @param id
     * @return
     */
    @GetMapping("/profile/{id}/comments")
    public ResponseEntity<?> getCommentsOnProfile(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            final Optional<User> user = userRepository.findById(id);
            if (requester.isEmpty() || user.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            try {
                final List<Comment> comments = commentRepository.findCommentsByUser(user.get());
                return ResponseEntity.ok(comments);
            } catch (Exception exception) {
                if (exception.getClass().getSimpleName().equals(InvalidDefinitionException.class.getSimpleName())) {
                    // gets thrown when the user in the comment is null (
                    return ResponseEntity.ok(List.of());
                } else {
                    return ResponseEntity.internalServerError().body(exception.getMessage());
                }
            }
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * Users can place a comment on someones profile
     *
     * @param id
     * @param comment
     * @return
     */
    @PostMapping("/profile/{id}/comments")
    public ResponseEntity<?> placeCommentOnProfile(@PathVariable("id") Long id, @RequestBody Comment comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user, commenter;
            Optional<User> _user, _commenter;

            _user = userRepository.findById(id);
            _commenter = userRepository.findByUsername(authentication.getName());

            if (_user.isPresent() && _commenter.isPresent()) {
                user = _user.get();
                commenter = _commenter.get();

                // set data & save
                comment.setUser(user);
                comment.setCommenter(commenter);
                commentRepository.save(comment);

                userRepository.save(user);
                return ResponseEntity.ok(comment);
            }
        }
        return ResponseEntity.badRequest().build();
    }


    /**
     * Retrieve project ratings on an expert's page
     *
     * @param id
     * @return
     */
    @GetMapping("/profile/{id}/projects/ratings")
    public ResponseEntity<?> getProjectRatingsOnProfile(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            final Optional<User> user = userRepository.findById(id);
            if (requester.isEmpty() || user.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            final List<ExpertProjectRating> ratings = expertProjectRatingRepository.findExpertProjectRatingByUser(user.get());
            return ResponseEntity.ok(ratings);
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * Retrieve project ratings on an expert's page
     *
     * @param id
     * @return
     */
    @PostMapping("/profile/{id}/projects/ratings")
    public ResponseEntity<?> getProjectRatingsOnProfile(@PathVariable("id") Long id, @RequestBody ExpertProjectRating expertProjectRating) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> user = userRepository.findById(id);

            if (user.isPresent()) {
                expertProjectRating.setUser(user.get());
                expertProjectRating = expertProjectRatingRepository.save(expertProjectRating);
                return ResponseEntity.ok(expertProjectRating);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Edit project rating
     *
     * @param id
     * @return
     */
    @PutMapping("/profile/{id}/projects/ratings")
    public ResponseEntity<?> updateProjectRatingsOnProfile(@PathVariable("id") Long id, @RequestBody ExpertProjectRating expertProjectRating) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> user = userRepository.findById(id);
            final boolean projectRatingExists = expertProjectRatingRepository.existsById(expertProjectRating.getId());
            if (!projectRatingExists) {
                throw new ResourceNotFound(expertProjectRating.getId());
            }

            if (user.isPresent()) {
                System.out.println(expertProjectRating);
                expertProjectRating.setUser(user.get());
                expertProjectRating = expertProjectRatingRepository.save(expertProjectRating);
                return ResponseEntity.ok(expertProjectRating);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Upload resume
     *
     * @param userId
     * @param resume
     * @return
     * @throws IOException
     */
    @PostMapping(path = "/profile/{id}/resume")
    public ResponseEntity<?> uploadResume(@PathVariable("id") Long userId, @RequestParam("resume") MultipartFile resume) throws IOException {
        final Role brokerRole = roleRepository.findByName(UserRole.ROLE_BROKER).orElseThrow(() -> new RuntimeException("Role not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            final Optional<User> userFromUrl = userRepository.findById(userId);

            if (requester.isPresent() && userFromUrl.isPresent() &&
                    (requester.get().getRoles().contains(brokerRole) || requester.get().equals(userFromUrl.get()))) {

                String uploadResume = storageService.uploadResume(resume, String.format("resume_%d", userId));
                if (uploadResume != null) {
                    return ResponseEntity.ok(uploadResume);
                }
            }
        }
        return ResponseEntity.badRequest().body("Something went wrong with uploading your resume");
    }

    /**
     * Download resume
     *
     * @param id
     * @return
     */
    @GetMapping("/profile/{id}/resume")
    public ResponseEntity<?> downloadResume(@PathVariable("id") Long id) {
        byte[] resumeData = storageService.download(String.format("resume_%d", id));

        if (resumeData == null) {
            return ResponseEntity.badRequest().body("Resume not found.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "resume.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resumeData);
    }

    /**
     * Delete a resume
     *
     * @param id
     * @return
     */
    @DeleteMapping("/profile/{id}/resume")
    public ResponseEntity<?> deleteResume(@PathVariable("id") Long id) {
        final Role brokerRole = roleRepository.findByName(UserRole.ROLE_BROKER).orElseThrow(() -> new RuntimeException("Role not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            final Optional<User> userFromUrl = userRepository.findById(id);

            if (requester.isPresent() && userFromUrl.isPresent() &&
                    (requester.get().getRoles().contains(brokerRole) || requester.get().equals(userFromUrl.get()))) {

                final String filename = String.format("resume_%d", id);
                final boolean success = storageService.delete(filename);
                if (success) {
                    return ResponseEntity.ok().build();
                }
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/profile/{id}/tags")
    public ResponseEntity<?> addTagToUser(@PathVariable("id") Long id, @RequestBody Tag tag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            final Optional<User> userFromUrl = userRepository.findById(id);
            tag = tagRepository.findById(tag.getId());

            if (requester.isEmpty() || userFromUrl.isEmpty() ||
                    !requester.equals(userFromUrl) ||
                    tag == null || requester.get().getTags().contains(tag)) {
                return ResponseEntity.badRequest().build();
            }

            userFromUrl.get().getTags().add(tag);
            userRepository.save(userFromUrl.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/profile/{id}/tags/{tagId}")
    public ResponseEntity<?> removeTagToUser(@PathVariable("id") Long id, @PathVariable("tagId") Long tagId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            final Optional<User> requester = userRepository.findByUsername(authentication.getName());
            final Optional<User> userFromUrl = userRepository.findById(id);

            if (requester.isEmpty() || userFromUrl.isEmpty() || !requester.equals(userFromUrl)) {
                return ResponseEntity.badRequest().body("User data invalid.");
            }

            final Tag tag = tagRepository.findById(tagId);
            if (tag == null) {
                return ResponseEntity.badRequest().body("Couldn't find tag.");
            }

            userFromUrl.get().getTags().remove(tag);
            userRepository.save(userFromUrl.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }
}
