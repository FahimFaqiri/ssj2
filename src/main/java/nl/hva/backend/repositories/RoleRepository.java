package nl.hva.backend.repositories;

import jakarta.transaction.Transactional;
import nl.hva.backend.enums.UserRole;
import nl.hva.backend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);
}
