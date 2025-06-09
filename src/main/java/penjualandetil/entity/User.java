package penjualandetil.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users") // Nama tabel sudah sesuai
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    // Diubah: field 'email' menjadi 'noTelepon' sesuai dengan skema SQL
    @Column(name = "no_telepon", length = 50)
    private String noTelepon;


    // Default constructor
    public User() {
    }

    // Constructor dengan field yang diperbarui
    public User(String username, String noTelepon) {
        this.username = username;
        this.noTelepon = noTelepon;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Diubah: Getter dan Setter untuk noTelepon
    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", noTelepon='" + noTelepon + '\'' +
                '}';
    }
}
