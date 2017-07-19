package co.novalist.service;

import co.novalist.dao.TokenDao;
import co.novalist.model.Token;
import co.novalist.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by user on 6/9/2017.
 */

@Service
public class TokenServiceImp implements TokenService {

    @Autowired
    private TokenDao tokenDao;

    @Override
    public Token createToken(String string, User user, Token.Type type){
        Token token = new Token();
        token.setUser(user);
        token.setString(string);
        token.setType(type);
        return token;
    }

    @Override
    public Token findByString(String string){
        return tokenDao.findByToken(string);
    }

    @Override
    public Token findByUser(User user) { return tokenDao.findByUser(user); }

    @Override
    public void save(Token token){ tokenDao.save(token);}

    @Override
    public void delete(Token token){ tokenDao.delete(token);}

}
