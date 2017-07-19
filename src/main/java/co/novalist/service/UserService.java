package co.novalist.service;

import co.novalist.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by user on 5/30/2017.
 */
public interface UserService extends UserDetailsService{

    User findByUsername(String username);
    void save(User user);
    void delete(User user);
}
