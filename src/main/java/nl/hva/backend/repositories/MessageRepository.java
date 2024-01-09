package nl.hva.backend.repositories;

import jakarta.transaction.Transactional;
import nl.hva.backend.models.Message;
import nl.hva.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("""
            SELECT DISTINCT message FROM Message message
            WHERE (message.sender = :personA OR message.sender = :personB) AND
            (message.receiver = :personA OR message.receiver = :personB)
            ORDER BY message.sentOn ASC
            """)
    List<Message> findMessageHistory(String personA, String personB);

    Optional<Message> findTop1BySenderAndReceiverOrReceiverAndSenderOrderBySentOnDesc(String sender, String receiver, String _receiver, String _sender);

    default Optional<Message> findLastMessageBetween(String sender, String receiver) {
        return findTop1BySenderAndReceiverOrReceiverAndSenderOrderBySentOnDesc(sender, receiver, sender, receiver);
    }

    List<Message> findMessagesBySenderAndReceiverOrReceiverAndSenderAndWasReadOrderBySentOnDesc(String sender, String receiver, String _receiver, String _sender, Boolean wasRead);

    /**
     * thank god for defaults
     *
     * @param sender
     * @param receiver
     * @return
     */
    default List<Message> findUnreadMessagesBetween(String sender, String receiver) {
        return findMessagesBySenderAndReceiverOrReceiverAndSenderAndWasReadOrderBySentOnDesc(sender, receiver, sender, receiver, false);
    }

    List<Message> findMessagesBySenderAndReceiverAndWasReadOrderBySentOnDesc(String sender, String receiver, Boolean wasRead);

    default List<Message> findUnreadMessagesWhereSenderAndReceiver(String sender, String receiver) {
        return findMessagesBySenderAndReceiverAndWasReadOrderBySentOnDesc(sender, receiver, false);
    }

    @Transactional
    @Modifying
    @Query("""
            UPDATE Message message
            SET message.wasRead = true
            WHERE (message.sender = :sender AND message.receiver = :receiver)
            """)
    void markAllMessagesReadWhereSenderAndReceiver(String sender, String receiver);

    @Modifying(clearAutomatically = true)
    @Transactional
    <S extends User> S save(S entity);
}
