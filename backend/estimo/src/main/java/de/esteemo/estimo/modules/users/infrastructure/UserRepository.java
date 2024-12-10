package de.esteemo.estimo.modules.users.infrastructure;

import de.esteemo.estimo.modules.users.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findFirstByUsername(String username);
}
