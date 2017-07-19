package co.novalist.service;

import co.novalist.model.Category;
import co.novalist.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by user on 5/24/2017.
 */
@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public Category findByName(String name) {
        return categoryDao.findByName(name);
    }
}
