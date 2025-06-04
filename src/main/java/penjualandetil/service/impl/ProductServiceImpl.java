package penjualandetil.service.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import penjualandetil.entity.Product;
import penjualandetil.service.ProductService;
import java.util.List;
import java.util.Collections; // Import Collections

public class ProductServiceImpl implements ProductService {
    private final SessionFactory sessionFactory;

    public ProductServiceImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<String> getAllProductNames() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT p.name FROM Product p ORDER BY p.name ASC", String.class).list();
        } catch (Exception e) {
            e.printStackTrace(); // Handle logging/exception lebih baik di aplikasi nyata
            return Collections.emptyList(); // Return empty list on error
        }
    }

    @Override
    public Product findProductByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Product p WHERE p.name = :productName", Product.class)
                    .setParameter("productName", name)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product findProductById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Product.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}