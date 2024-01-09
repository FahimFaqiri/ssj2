package nl.hva.backend.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.hva.backend.exceptions.ResourceNotFound;
import nl.hva.backend.models.Invitation;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class InvitationRepository implements BaseRepository<Invitation> {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<Invitation> findAll() {
        return em.createQuery("SELECT s FROM Invitation s", Invitation.class).getResultList();
    }

    @Override
    @Transactional
    public Invitation findById(Long id) {
        return em.find(Invitation.class, id);
    }

    @Transactional
    public Invitation findByUniqueCode(String uniqueCode) {
        try {
            return em.createQuery("SELECT i FROM Invitation i WHERE i.uniqueCode = :uniqueCode", Invitation.class)
                    .setParameter("uniqueCode", uniqueCode)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public Invitation save(Invitation element) {
        if (element.getId() == null) {
            em.persist(element);
        } else {
            element = em.merge(element);
        }
        return element;
    }

    @Override
    @Transactional
    public Invitation deleteById(Long id) {
        Invitation invitation = findById(id);
        if (invitation != null) {
            em.remove(invitation);
            return invitation;
        }
        return null;
    }
}
