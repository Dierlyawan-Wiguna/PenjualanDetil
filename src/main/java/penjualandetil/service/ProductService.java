package penjualandetil.service;

import penjualandetil.entity.Product;
import java.util.List;

public interface ProductService {
    List<String> getAllProductNames();
    Product findProductByName(String name);
    Product findProductById(int id);
}