package Task.SpringMvc.model;

import Task.SpringMvc.controller.homeController;
import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class UserData {

    private static final Logger Log = LogManager.getLogger(UserData.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;
    
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(name = "country")
    private String country;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "interests") // Change the column name to "interests"
    private String interests; // Change the type to String

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();
    
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserImage> images = new ArrayList<>();

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(String dob) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the format based on your form
        try {
            this.dob = format.parse(dob);
        } catch (ParseException e) {
            // Handle parsing exception
Log.error("dob"+e);
        }
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        // Convert the List<String> to a comma-separated String
        this.interests = String.join(",", interests);
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
    
    public List<UserImage> getImages() {
        return images;
    }

    public void setImages(List<UserImage> images) {
        this.images = images;
        // Populate base64Data field for each UserImage object
        if (images != null) {
            for (UserImage image : images) {
                if (image.getData() != null) {
                    image.setBase64Data(Base64.getEncoder().encodeToString(image.getData()));
                }
            }
        }
    }

    public UserData() {
    }

    public UserData(String firstName, String lastName, String email, String password, Date dob, String country, String userType, String interests, List<Address> addresses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.country = country;
        this.userType = userType;
        this.interests = interests;
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dob=" + dob +
                ", country='" + country + '\'' +
                ", userType='" + userType + '\'' +
                ", interests=" + interests +
                ", addresses=" + addresses +
                '}';
    }
}
