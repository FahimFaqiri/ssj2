package nl.hva.backend.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import nl.hva.backend.models.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectRepository implements BaseRepository<Project> {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<Project> findAll() {
        return em.createQuery("SELECT DISTINCT s FROM Project s LEFT JOIN FETCH s.tags", Project.class).getResultList();
    }

    @Override
    @Transactional
    public Project findById(Long id) {
        return em.find(Project.class, id);
    }

    @Override
    @Transactional
    public Project save(Project element) {
        if (element.getId() == null) {
            em.persist(element);
        } else {
            element = em.merge(element);
        }
        return element;
    }

    @Override
    @Transactional
    public Project deleteById(Long id) {
        return null;
    }
}
