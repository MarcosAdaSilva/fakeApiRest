package com.marcosDev.fake_api_us.infrastructure.repositories;

import com.marcosDev.fake_api_us.infrastructure.entities.ProdutoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, String> {
    Boolean existsByNome(String nome);
    ProdutoEntity findByNome(String nome);
    @Transactional
    void deleteByNome(String nome);
}
