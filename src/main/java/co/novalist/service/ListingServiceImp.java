package co.novalist.service;

import co.novalist.dao.ListingDao;
import co.novalist.model.Listing;
import co.novalist.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 5/24/2017.
 */

@Service
public class ListingServiceImp implements ListingService {

    @Autowired
    private ListingDao listingDao;

    @Override
    public List<Listing> findAll() {
        return listingDao.findAll();
    }

    @Override
    public Listing findById(Long id) {
        return listingDao.findById(id);
    }

    @Override
    public List<Listing> findByCategoryId(Long id) { return listingDao.findByCategoryId(id); }

    @Override
    public List<Listing> findByUser(User user) { return listingDao.findByUser(user); }

    @Override
    public void save(Listing listing, List<MultipartFile> files) {
        List<byte[]> bytes = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                bytes.add(file.getBytes());
            }
        } catch (IOException e) {
            System.err.println("Unable to get byte array from uploaded file.");
        }
        if (bytes.size() >= 1) {
            listing.setPhoto1(bytes.get(0));
            if (bytes.size() >= 2) {
                listing.setPhoto2(bytes.get(1));
                if (bytes.size() >= 3) {
                    listing.setPhoto3(bytes.get(2));
                    if (bytes.size() >= 4) {
                        listing.setPhoto4(bytes.get(3));
                        if (bytes.size() == 5) {
                            listing.setPhoto5(bytes.get(4));
                        }
                    }
                }
            }
        }
        listingDao.save(listing);
    }

    @Override
    public void delete(Listing listing) {
        listingDao.delete(listing);
    }
}
