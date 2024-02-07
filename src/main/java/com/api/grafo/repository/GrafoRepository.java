package com.api.grafo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.grafo.model.Rotas;

@Repository
public interface GrafoRepository extends JpaRepository<Rotas, Long> {

}
