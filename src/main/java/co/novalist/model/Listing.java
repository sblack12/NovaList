package co.novalist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by user on 5/24/2017.
 */
@Entity
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    @Lob
    @NotNull
    @Size(min = 3, max = 5000)
    private String description;

    @ManyToOne
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int price;

    @Lob
    private byte[] photo1;

    @Lob
    private byte[] photo2;

    @Lob
    private byte[] photo3;

    @Lob
    private byte[] photo4;

    @Lob
    private byte[] photo5;

    private LocalDateTime dateUploaded = LocalDateTime.now();
    private String hash;

    public Listing() {
    }

    public Long getId() {
        return id;
    }

    public String getTimeSinceUploaded() {
        String unit = "";
        LocalDateTime now = LocalDateTime.now();
        long diff;
        if ((diff = ChronoUnit.SECONDS.between(dateUploaded, now)) < 60) {
            unit = "secs";
        } else if ((diff = ChronoUnit.MINUTES.between(dateUploaded, now)) < 60) {
            unit = "mins";
        } else if ((diff = ChronoUnit.HOURS.between(dateUploaded, now)) < 24) {
            unit = "hours";
        } else if ((diff = ChronoUnit.DAYS.between(dateUploaded, now)) < 30) {
            unit = "days";
        } else if ((diff = ChronoUnit.MONTHS.between(dateUploaded, now)) < 12) {
            unit = "months";
        } else {
            diff = ChronoUnit.YEARS.between(dateUploaded, now);
        }
        return String.format("%d %s", diff, unit);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(LocalDateTime dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public byte[] getPhoto1() {
        return photo1;
    }

    public void setPhoto1(byte[] photo1) {
        this.photo1 = photo1;
    }

    public byte[] getPhoto2() {
        return photo2;
    }

    public void setPhoto2(byte[] photo2) {
        this.photo2 = photo2;
    }

    public byte[] getPhoto3() {
        return photo3;
    }

    public void setPhoto3(byte[] photo3) {
        this.photo3 = photo3;
    }

    public byte[] getPhoto4() {
        return photo4;
    }

    public void setPhoto4(byte[] photo4) {
        this.photo4 = photo4;
    }

    public byte[] getPhoto5() {
        return photo5;
    }

    public void setPhoto5(byte[] photo5) {
        this.photo5 = photo5;
    }
}
