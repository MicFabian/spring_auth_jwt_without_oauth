package de.esteemo.estimo.modules.users.application;

import de.esteemo.estimo.modules.users.application.resources.JwtResponse;
import de.esteemo.estimo.modules.users.application.resources.LoginRequest;
import de.esteemo.estimo.modules.users.application.resources.RegisterRequest;
import de.esteemo.estimo.modules.users.domain.Role;
import de.esteemo.estimo.modules.users.domain.usecases.RegisterUser;
import de.esteemo.estimo.modules.users.infrastructure.JwtUtil;
import de.esteemo.estimo.modules.users.infrastructure.RoleRepository;
import de.esteemo.estimo.modules.users.infrastructure.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RegisterUser registerUser;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.findFirstByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        Optional<Role> optionalRole = roleRepository.findByName("USER");
        if (optionalRole.isEmpty()) {
            return ResponseEntity.badRequest().body("Default role USER does not exist");
        }

        registerUser.registerUser(registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getEmail(),
                Set.of(optionalRole.get()));

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            String jwtToken = jwtUtil.generateToken(userDetails.getUsername(), roles);

            return ResponseEntity.ok(new JwtResponse(jwtToken));
        } catch (Exception ex) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

}
