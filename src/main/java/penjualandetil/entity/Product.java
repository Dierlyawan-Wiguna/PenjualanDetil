package penjualandetil.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "products") // Sesuaikan dengan nama tabel produk Anda di database
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Cocok untuk auto-increment ID di MySQL
    @Column(name = "product_id")
    private int productId;

    @Column(name = "name", nullable = false, length = 255) // Sesuaikan panjang dan nullability
    private String name;

    @Column(name = "price", nullable = false, precision = 10, scale = 2) // precision dan scale untuk tipe DECIMAL
    private BigDecimal price;

    // Default constructor (diperlukan oleh Hibernate)
    public Product() {
    }

    // Constructor lain jika diperlukan
    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}