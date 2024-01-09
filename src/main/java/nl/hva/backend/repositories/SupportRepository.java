package nl.hva.backend.repositories;
//public interface SupportRepository JpaRepository<Support, Long> {

import nl.hva.backend.models.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {
    List<Support> findAll();

    Support findById(long id);

     <S extends Support> S save(S entity);

    @Override
    <S extends Support> List<S> saveAll(Iterable<S> support);

    @Override
    void deleteById(Long aLong);
}
