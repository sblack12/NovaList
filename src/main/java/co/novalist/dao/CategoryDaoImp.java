package co.novalist.dao;

import co.novalist.model.Category;
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
public class CategoryDaoImp implements CategoryDao{

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Category> findAll() {
        Session session = sessionFactory.openSession();
        CriteriaQuery<Category> criteria = session.getCriteriaBuilder().createQuery(Category.class);
        criteria.from(Category.class);
        List<Category> categories = session.createQuery(criteria).getResultList();
        session.close();
        return categories;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Category findByName(String name) {
        Session session = sessionFactory.openSession();
        List<Category> categories = session.createQuery(String.format("from Category where name = '%s'", name)).list();
        session.close();
        if (categories.isEmpty()) {
            return null;
        }
        return categories.get(0);
    }

}
