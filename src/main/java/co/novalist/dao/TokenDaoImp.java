package co.novalist.dao;

import co.novalist.model.Token;
import co.novalist.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by user on 6/9/2017.
 */

@Repository
public class TokenDaoImp implements TokenDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public Token findByToken(String string) {
        Session session = sessionFactory.openSession();
        List<Token> tokens = session.createQuery(String.format("from Token where string = '%s'", string)).list();
        session.close();
        if (tokens.isEmpty()) {
            return null;
        } else {
            return tokens.get(0);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Token findByUser(User user) {
        Session session = sessionFactory.openSession();
        List<Token> tokens = session.createQuery(String.format("from Token where user_id = %d", user.getId())).list();
        session.close();
        if (tokens.isEmpty()) {
            return null;
        } else {
            return tokens.get(0);
        }
    }

    @Override
    public void save (Token token) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(token);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(Token token) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(token);
        session.getTransaction().commit();
        session.close();
    }
}
