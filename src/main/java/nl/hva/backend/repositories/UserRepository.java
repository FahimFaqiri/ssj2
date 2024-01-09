package nl.hva.backend.repositories;

import jakarta.transaction.Transactional;
import nl.hva.backend.models.Role;
import nl.hva.backend.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<User> findAll();

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.tags")
    List<User> findAllUsersWithTags();

    List<User> findUsersByRolesContaining(Role role);

    List<User> findUsersByRolesContaining(Role role, Pageable pageable);

    @Query("""
            SELECT DISTINCT user FROM User user
            WHERE (:role MEMBER OF user.roles)
            AND (user.selectedSector = UPPER(:selectedSector) OR UPPER(:selectedSector) = 'ALL')
            AND (user.selectedClient = UPPER(:selectedClient) OR UPPER(:selectedClient) = 'ALL')
            AND (user.selectedProject = UPPER(:selectedProject) OR UPPER(:selectedProject) = 'ALL')
            """)
    List<User> findUsersByRolesContainingAndSelectedSectorAndSelectedClientAndSelectedProject(Role role, String selectedSector, String selectedClient, String selectedProject, Pageable pageable);

    List<User> findUsersByRolesContainingAndUsernameContainingIgnoreCase(Role role, String username, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Override
    <S extends User> S save(S entity);
}
