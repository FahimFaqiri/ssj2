package nl.hva.backend.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import nl.hva.backend.enums.UserRole;
import nl.hva.backend.models.Role;
import nl.hva.backend.models.User;
import nl.hva.backend.payload.request.LoginRequest;
import nl.hva.backend.payload.request.SignupRequest;
import nl.hva.backend.payload.response.JwtResponse;
import nl.hva.backend.payload.response.MessageResponse;
import nl.hva.backend.repositories.RoleRepository;
import nl.hva.backend.repositories.UserRepository;
import nl.hva.backend.security.jwt.JwtUtils;
import nl.hva.backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(
                null,
                signUpRequest.getUsername(),
                signUpRequest.getFullName(),
                signUpRequest.getGender(),
                signUpRequest.getDateOfBirth(),
                signUpRequest.getPhoneNumber(),
                signUpRequest.getAddress(),
                signUpRequest.getSelectedSector(),
                signUpRequest.getSelectedClient(),
                signUpRequest.getSelectedProject(),
                signUpRequest.getWorkPermit(),
                signUpRequest.getAvailability(),
                signUpRequest.getHasPli(),
                signUpRequest.getSpecialConsiderations(),
                signUpRequest.getEmail(),
                "",
                encoder.encode(signUpRequest.getPassword())
        );

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(UserRole.ROLE_EXPERT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("broker".equals(role)) {
                    Role brokerRole = roleRepository.findByName(UserRole.ROLE_BROKER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(brokerRole);
                } else {
                    Role expertRole = roleRepository.findByName(UserRole.ROLE_EXPERT)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(expertRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/user")
    public ResponseEntity<User> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                User user = userRepository.findByUsername(username).orElse(null);

                if (user != null) {
                    return ResponseEntity.ok(user);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
