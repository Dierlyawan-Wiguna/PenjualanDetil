package penjualandetil.service.decorator;

import penjualandetil.entity.Product;
import penjualandetil.service.ProductService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoggingProductServiceDecorator implements ProductService {
    private final ProductService wrappedService;
    private static final Logger LOGGER = Logger.getLogger(LoggingProductServiceDecorator.class.getName());


    public LoggingProductServiceDecorator(ProductService productService) {
        this.wrappedService = productService;
    }

    @Override
    public List<String> getAllProductNames() {
        LOGGER.log(Level.INFO, "Attempting to get all product names...");
        List<String> result = wrappedService.getAllProductNames();
        LOGGER.log(Level.INFO, "Finished getting all product names. Count: " + (result != null ? result.size() : "null"));
        return result;
    }

    @Override
    public Product findProductByName(String name) {
        LOGGER.log(Level.INFO, "Attempting to find product by name: " + name);
        Product result = wrappedService.findProductByName(name);
        LOGGER.log(Level.INFO, "Finished finding product by name. Found: " + (result != null));
        return result;
    }

    @Override
    public Product findProductById(int id) {
        LOGGER.log(Level.INFO, "Attempting to find product by id: " + id);
        Product result = wrappedService.findProductById(id);
        LOGGER.log(Level.INFO, "Finished finding product by id. Found: " + (result != null));
        return result;
    }
}