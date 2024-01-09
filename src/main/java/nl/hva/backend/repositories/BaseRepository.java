package nl.hva.backend.repositories;

import java.util.List;

public interface BaseRepository<E> {
    List<E> findAll();

    E findById(Long id);

    E save(E item);

    E deleteById(Long id);
}
