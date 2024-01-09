package nl.hva.backend.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import nl.hva.backend.models.FAQ;
import org.springframework.stereotype.Repository;

@Repository
public class FAQRepository implements BaseRepository<FAQ> {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<FAQ> findAll() {
        return em.createQuery("SELECT s FROM FAQ s", FAQ.class).getResultList();
    }

    @Override
    @Transactional
    public FAQ findById(Long id) {
        return em.find(FAQ.class, id);
    }

    @Override
    @Transactional
    public FAQ save(FAQ element) {
        if (element.getId() == null) {
            em.persist(element);
        } else {
            element = em.merge(element);
        }
        return element;
    }

    @Override
    @Transactional
    public FAQ deleteById(Long id) {
        return null;
    }

}
