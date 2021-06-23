package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao<Tag> {
    private final EntityManagerFactory factory;

    @Autowired
    public TagDaoImpl(EntityManagerFactory factory) {
        this.factory = factory;
    }

    @Override
    public long insert(Tag tag) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        em.persist(tag);
        em.getTransaction().commit();
        em.close();
        return tag.getId();
    }

    @Override
    public Optional<Tag> findById(long id) {
        EntityManager em = factory.createEntityManager();
        Optional<Tag> result = Optional.ofNullable(em.find(Tag.class, id));
        em.close();
        return result;
    }

    @Override
    public Optional<Tag> findByName(String name) {
        EntityManager em = factory.createEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Tag> criteria = builder.createQuery(Tag.class);
        Root<Tag> root = criteria.from(Tag.class);
        criteria.where(builder.equal(root.get("name"), name));
        Optional<Tag> result = em.createQuery(criteria).getResultStream().findAny();
        em.close();
        return result;
    }

    @Override
    public List<Tag> findAll() {
        EntityManager em = factory.createEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Tag> criteria = builder.createQuery(Tag.class);
        Root<Tag> root = criteria.from(Tag.class);
        criteria.select(root);
        List<Tag> tags = em.createQuery(criteria).getResultList();
        em.close();
        return tags;
    }

    @Override
    public List<Tag> findTagsConnectedToCertificate(long id) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<Tag> criteria = builder.createCriteriaDelete(Tag.class);
        Root<Tag> root = criteria.from(Tag.class);
        criteria.where(builder.equal(root.get("id"), id));
        boolean result = em.createQuery(criteria).executeUpdate() == 1;
        em.getTransaction().commit();
        return result;
    }
}
