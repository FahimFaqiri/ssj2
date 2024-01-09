package nl.hva.backend.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.hva.backend.models.Invitation;
import nl.hva.backend.models.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepository implements BaseRepository<Tag> {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<Tag> findAll() {
        return em.createQuery("SELECT s FROM Tag s", Tag.class).getResultList();
    }

    @Override
    @Transactional
    public Tag findById(Long id) {
        return em.find(Tag.class, id);
    }

    @Override
    @Transactional
    public Tag save(Tag element) {
        if (element.getId() == null) {
            em.persist(element);
        } else {
            element = em.merge(element);
        }
        return element;
    }

    @Override
    @Transactional
    public Tag deleteById(Long id) {
        Tag tag = findById(id);
        if (tag != null) {
            em.remove(tag);
            return tag;
        }
        return null;
    }

    @Transactional
    public void deleteAll() {
        em.createQuery("DELETE FROM Tag").executeUpdate();
    }
}
