package co.novalist.service;

import co.novalist.model.User;
import co.novalist.model.Token;

/**
 * Created by user on 6/9/2017.
 */
public interface TokenService {

    Token createToken(String token, User user, Token.Type type);
    Token findByString(String string);
    Token findByUser(User user);
    void save(Token token);
    void delete(Token token);

}
