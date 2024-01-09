package nl.hva.backend.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import nl.hva.backend.models.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConfigurationRepository {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public Configuration findByConfigName(String name) {
        TypedQuery<Configuration> query = em.createQuery(
                "SELECT e FROM Configuration e WHERE e.configName = :name", Configuration.class);
        query.setParameter("name", name);

        // Handling case when there's no result found
        List<Configuration> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Transactional
    public boolean existsBySeedingDone(boolean status) {
        Configuration conf = findByConfigName("seeding_done");
        return conf != null && conf.getValue() == status;
    }

    @Transactional
    public Configuration save(Configuration element) {
        if (element.getId() == null) {
            em.persist(element);
        } else {
            element = em.merge(element);
        }
        return element;
    }
}
