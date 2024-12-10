package de.esteemo.estimo.modules.users.domain.usecases;

import de.esteemo.estimo.modules.users.domain.Role;
import de.esteemo.estimo.modules.users.domain.User;
import de.esteemo.estimo.modules.users.infrastructure.RoleRepository;
import de.esteemo.estimo.modules.users.infrastructure.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUser {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(String username, String rawPassword, String email, Set<Role> roles) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        newUser.setEmail(email);
        newUser.setEnabled(true);
        newUser.setRoles(roles);

        userRepository.save(newUser);
    }
}
