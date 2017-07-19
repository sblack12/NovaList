package co.novalist.service;

import co.novalist.model.Category;

import java.util.List;

/**
 * Created by user on 5/24/2017.
 */
public interface CategoryService {
    List<Category> findAll();
    Category findByName(String name);
}

