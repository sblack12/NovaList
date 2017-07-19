package co.novalist.dao;

import co.novalist.model.Listing;
import co.novalist.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Created by user on 5/24/2017.
 */

@Repository
public class ListingDaoImp implements ListingDao{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Listing> findAll() {
        Session session = sessionFactory.openSession();
        CriteriaQuery<Listing> criteria = session.getCriteriaBuilder().createQuery(Listing.class);
        criteria.from(Listing.class);
        List<Listing> listings = session.createQuery(criteria).getResultList();
        session.close();
        return listings;
    }
    @Override
    @SuppressWarnings("unchecked")
    public Listing findById(long id) {
        Session session = sessionFactory.openSession();
        List<Listing> listings = session.createQuery(String.format("from Listing where id = %d", id)).list();
        session.close();
        if (listings.isEmpty()){
            return null;
        }
        return listings.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Listing> findByCategoryId(long id) {
        Session session = sessionFactory.openSession();
        List<Listing> listings = session.createQuery(String.format("from Listing where category_id = %d", id)).list();
        session.close();
        return listings;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Listing> findByUser(User user) {
        Session session = sessionFactory.openSession();
        List<Listing> listings =
                session.createQuery(String.format("from Listing where user_id = %d", user.getId())).list();
        session.close();
        return listings;
    }

    @Override
    public void save(Listing listing) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(listing);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(Listing listing) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(listing);
        session.getTransaction().commit();
        session.close();
    }
}
