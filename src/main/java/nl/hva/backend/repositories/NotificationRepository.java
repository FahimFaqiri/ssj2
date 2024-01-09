package nl.hva.backend.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.hva.backend.models.Notification;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationRepository implements BaseRepository<Notification> {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<Notification> findAll() {
        return em.createQuery("SELECT n FROM Notification n", Notification.class).getResultList();
    }

    @Transactional
    public List<Notification> findNotificationsByPermission() {
        return em.createQuery("SELECT n FROM Notification n WHERE hasPermission = false", Notification.class).getResultList();
    }

    @Override
    @Transactional
    public Notification findById(Long id) {
        return em.find(Notification.class, id);
    }

    @Transactional
    public List<Notification> findByUserId(Long userId) {
        return em.createQuery(
                        "SELECT n FROM Notification n " +
                                "INNER JOIN n.user u " +
                                "WHERE u.id = :userId", Notification.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Transactional
    public List<Notification> findExpertNotifications() {
        return em.createQuery("SELECT n FROM Notification n WHERE type != 'Request' AND type != 'Accepted' AND type != 'Denied'", Notification.class).getResultList();
    }


    @Override
    @Transactional
    public Notification save(Notification element) {
        if (element.getId() == null) {
            em.persist(element);
        } else {
            element = em.merge(element);
        }
        return element;
    }

    @Override
    @Transactional
    public Notification deleteById(Long id) {
        Notification notification = this.findById(id);
        em.remove(notification);
        return notification;
    }
}
