package nl.hva.backend.repositories;

import jakarta.transaction.Transactional;
import nl.hva.backend.models.Comment;
import nl.hva.backend.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByUser(User user);

    List<Comment> findCommentsByUser(User user, Pageable pageable);

    List<Comment> findCommentsByCommenter(User commenter);

    List<Comment> findCommentsByCommenter(User commenter, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Transactional
    <S extends User> S save(S entity);
}
