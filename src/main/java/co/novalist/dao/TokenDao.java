package co.novalist.dao;

import co.novalist.model.User;
import co.novalist.model.Token;

/**
 * Created by user on 6/9/2017.
 */
public interface TokenDao {

    Token findByToken (String token);
    Token findByUser (User user);
    void save (Token token);
    void delete (Token token);

}
