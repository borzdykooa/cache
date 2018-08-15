package com.borzdykooa;

import com.borzdykooa.dao.TrainerDao;
import com.borzdykooa.entity.Trainer;
import com.borzdykooa.util.SessionFactoryManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

/*
Класс для демонстрации работы Cache
 */
public class Demo {

    public static void main(String[] args) {
        SessionFactory sessionFactory = SessionFactoryManager.getSessionFactory();
        importTestData(sessionFactory);

        TrainerDao.getInstance().findAll();
        TrainerDao.getInstance().findAll();

        TrainerDao.getInstance().findByLanguage("Java");
        TrainerDao.getInstance().findByLanguage("C#");
        TrainerDao.getInstance().findByLanguage("JS");
        TrainerDao.getInstance().findByLanguage("Java");
        TrainerDao.getInstance().findByLanguage("JS");
        TrainerDao.getInstance().findByLanguage("Ruby");
        TrainerDao.getInstance().findByLanguage("C++");
        TrainerDao.getInstance().findByLanguage("C++");

        Optional<Trainer> first = TrainerDao.getInstance().findAll().stream().findFirst();
        if (first.isPresent()) {
            Trainer trainer = first.get();
            TrainerDao.getInstance().delete(trainer);
        }
        TrainerDao.getInstance().findAll();

        sessionFactory.close();
    }

    private static void importTestData(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Trainer andreiReut = new Trainer("Andrei Reut", "Java", 6);
            Trainer ivanIvanov = new Trainer("Ivan Ivanov", "C++", 4);
            Trainer petrPetrov = new Trainer("Petr Petrov", "C#", 5);
            Trainer sergeiSergeev = new Trainer("Sergei Sergeev", "Java", 10);
            Trainer pavelPavlov = new Trainer("Pavel Pavlov", "C#", 2);
            session.save(andreiReut);
            session.save(ivanIvanov);
            session.save(petrPetrov);
            session.save(sergeiSergeev);
            session.save(pavelPavlov);
            session.getTransaction().commit();
        }
    }
}
