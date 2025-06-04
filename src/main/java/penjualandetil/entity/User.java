package penjualandetil.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users") // Sesuaikan dengan nama tabel pengguna Anda di database
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Cocok untuk auto-increment ID di MySQL
    @Column(name = "user_id")
    private int userId;

    @Column(name = "username", nullable = false, unique = true, length = 100) // Username biasanya unik
    private String username;

    @Column(name = "email", nullable = true, unique = true, length = 255) // Email juga biasanya unik, bisa nullable
    private String email;

    // Tambahkan field lain jika ada, misalnya password, role, dll.
    // @Column(name = "password")
    // private String password;

    // Default constructor (diperlukan oleh Hibernate)
    public User() {
    }

    // Constructor lain jika diperlukan
    public User(String username, String email) {
        this.username = username;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // public String getPassword() {
    //     return password;
    // }
    //
    // public void setPassword(String password) {
    //     this.password = password;
    // }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}