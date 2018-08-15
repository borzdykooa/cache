package com.borzdykooa.dao;

import com.borzdykooa.cache.Cache;
import com.borzdykooa.entity.Trainer;
import com.borzdykooa.util.SessionFactoryManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

/*
Класс, содержащий методы работы с таблицей trainer
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TrainerDao {

    private static final TrainerDao INSTANCE = new TrainerDao();
    private static final Logger log = Logger.getLogger(TrainerDao.class);
    private static final SessionFactory SESSION_FACTORY = SessionFactoryManager.getSessionFactory();
    private Cache cache = new Cache();

    public Long save(Trainer trainer) {
        log.info("Method save is called in TrainerDao");
        cache.clear();
        try (Session session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            Serializable id = session.save(trainer);
            session.getTransaction().commit();
            if (id != null) {
                log.info(trainer.toString() + " has been saved successfully!");
            }
            return (Long) id;
        }
    }

    public Trainer find(Long id) {
        log.info("Method find is called in TrainerDao");
        cache.clear();
        try (Session session = SESSION_FACTORY.openSession()) {
            Trainer trainer = session.find(Trainer.class, id);
            if (trainer != null) {
                log.info(trainer.toString() + " has been found successfully");
            }
            return trainer;
        }
    }

    public void update(Trainer trainer) {
        log.info("Method update is called in TrainerDao for " + trainer.toString());
        cache.clear();
        try (Session session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            session.update(trainer);
            session.getTransaction().commit();
            log.info(trainer.toString() + " has been updated successfully!");
        }
    }

    public void delete(Trainer trainer) {
        log.info("Method delete is called in TrainerDao");
        cache.clear();
        try (Session session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            session.delete(trainer);
            session.getTransaction().commit();
            log.info(trainer.toString() + " has been deleted successfully!");
        }
    }

    public List<Trainer> findAll() {
        log.info("Method findAll is called in TrainerDao");
        if (cache.ifCacheContains("findAll")) {
            return cache.returnFromCache("findAll");
        }
        try (Session session = SESSION_FACTORY.openSession()) {
            List<Trainer> list = session.createQuery("select t from Trainer t", Trainer.class).list();
            if (list.size() > 0) {
                log.info("List of Trainers: " + list.toString());
            } else {
                log.info("List of Trainers is empty");
            }
            cache.add("findAll", list);
            cache.viewInfo();
            return list;
        }
    }

    public List<Trainer> findByLanguage(String language) {
        log.info("Method findByLanguage (" + language + ") is called in TrainerDao");
        if (cache.ifCacheContains("findByLanguage" + language)) {
            return cache.returnFromCache("findByLanguage" + language);
        }
        try (Session session = SESSION_FACTORY.openSession()) {
            List<Trainer> list = session.createQuery("select t from Trainer t where t.language=:language", Trainer.class)
                    .setParameter("language", language)
                    .list();
            if (list.size() > 0) {
                log.info("List of Trainers: " + list.toString());
            } else {
                log.info("List of Trainers is empty");
            }
            cache.add("findByLanguage" + language, list);
            cache.viewInfo();
            return list;
        }
    }

    public static TrainerDao getInstance() {
        return INSTANCE;
    }
}
