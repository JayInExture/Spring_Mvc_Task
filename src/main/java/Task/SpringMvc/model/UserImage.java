package Task.SpringMvc.model;

import jakarta.persistence.*;

import java.util.Arrays;

@Entity
@Table(name = "users_img")
public class UserImage {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserData user;

    @Lob
    @Column(name = "img_data", columnDefinition = "LONGBLOB")
    private byte[] data;

    
    @Transient // This field will not be persisted in the database
    private String base64Data;
    
    
    public String getBase64Data() {
        return base64Data;
    }

    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }
    // Getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserImage{" +
                "id=" + id +
                ", user=" + user +
                ", data=" + Arrays.toString(data) +
                ", base64Data='" + base64Data + '\'' +
                '}';
    }
}
