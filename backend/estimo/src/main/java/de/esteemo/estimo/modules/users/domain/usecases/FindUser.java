package de.esteemo.estimo.modules.users.domain.usecases;

import de.esteemo.estimo.modules.users.domain.User;
import de.esteemo.estimo.modules.users.infrastructure.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUser {

    private final UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findFirstByUsername(username);
    }
}
