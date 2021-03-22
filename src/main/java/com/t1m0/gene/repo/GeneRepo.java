package com.t1m0.gene.repo;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.t1m0.gene.model.Gene;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;

@ApplicationScoped
public class GeneRepo {

    private final EntityManager entityManager;

    @Inject
    public GeneRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    @TransactionConfiguration(timeout = 120)
    public int truncate() {
        Query deleteQuery = entityManager.createQuery("delete from Gene");
        return deleteQuery.executeUpdate();
    }

    @Transactional
    public Gene save(Gene gene) {
        entityManager.persist(gene);
        return gene;
    }

    @Transactional
    @TransactionConfiguration(timeout = 120)
    public void saveAll(List<Gene> genes) {
        for (Gene gene : genes) {
            entityManager.persist(gene);
        }
    }

    public long count() {
        Query countQuery = entityManager.createQuery("select count(g.gene_id) from Gene g");
        return (Long) countQuery.getSingleResult();
    }
}
