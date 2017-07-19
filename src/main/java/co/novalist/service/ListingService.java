package co.novalist.service;

import co.novalist.model.Listing;
import co.novalist.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by user on 5/24/2017.
 */
public interface ListingService {

    List<Listing> findAll();
    List<Listing> findByUser(User user);
    Listing findById(Long id);
    List<Listing> findByCategoryId(Long id);
    void save(Listing listing, List<MultipartFile> files);
    void delete(Listing listing);
}
