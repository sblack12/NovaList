package co.novalist.dao;

import co.novalist.model.User;

/**
 * Created by user on 5/30/2017.
 */
public interface UserDao {

    User findByUsername(String username);
    void save(User user);
    void delete(User user);

}
