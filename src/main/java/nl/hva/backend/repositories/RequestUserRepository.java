package nl.hva.backend.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.hva.backend.models.Notification;
import nl.hva.backend.models.RequestUser;
import org.apache.catalina.session.PersistentManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RequestUserRepository implements BaseRepository<RequestUser> {

    @PersistenceContext
    private EntityManager em;


    @Override
    @Transactional
    public List<RequestUser> findAll() {
       return em.createQuery("SELECT r FROM RequestUser r", RequestUser.class).getResultList();
    }

    @Transactional
    public List<RequestUser> findAcceptedRequestUsers() {
        return em.createQuery("SELECT r FROM RequestUser r WHERE accepted = true", RequestUser.class).getResultList();
    }

    @Transactional
    public List<RequestUser> findDeniedRequestUsers() {
        return em.createQuery("SELECT r FROM RequestUser r WHERE denied = true", RequestUser.class).getResultList();
    }
    @Transactional
    public List<RequestUser> findRequestUsers() {
        return em.createQuery("SELECT r FROM RequestUser r WHERE denied = false AND accepted = false", RequestUser.class).getResultList();
    }



    @Override
    @Transactional
    public RequestUser findById(Long id) {
        return em.find(RequestUser.class, id);
    }

    @Override
    @Transactional
    public RequestUser save(RequestUser element) {
        if (element.getId() == null) {
            em.persist(element);
        } else {
            element = em.merge(element);
        }
        return element;
    }

    @Override
    @Transactional
    public RequestUser deleteById(Long id) {
        RequestUser requestUser = this.findById(id);
        em.remove(requestUser);
        return requestUser;
    }
}
