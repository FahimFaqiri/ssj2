package nl.hva.backend.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.hva.backend.models.Functionality;
import nl.hva.backend.models.Location;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FunctionalityRepository implements BaseRepository<Functionality> {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<Functionality> findAll() {
        return em.createQuery("SELECT s FROM Functionality s", Functionality.class).getResultList();
    }

    @Override
    @Transactional
    public Functionality findById(Long id) {
        return em.find(Functionality.class, id);
    }

    @Override
    @Transactional
    public Functionality save(Functionality element) {
        if (element.getId() == null) {
            em.persist(element);
        } else {
            element = em.merge(element);
        }
        return element;
    }

    @Override
    @Transactional
    public Functionality deleteById(Long id) {
        return null;
    }
}
