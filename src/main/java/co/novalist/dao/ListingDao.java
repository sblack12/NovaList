package co.novalist.dao;

import co.novalist.model.Listing;
import co.novalist.model.User;

import java.util.List;

/**
 * Created by user on 5/24/2017.
 */
public interface ListingDao {

    List<Listing> findAll();
    List<Listing> findByUser(User user);
    Listing findById(long id);
    List<Listing> findByCategoryId(long id);
    void save(Listing listing);
    void delete(Listing listing);

}
