package ru.akirakozov.sd.refactoring.products;

import java.util.List;

public interface ProductsManager extends AutoCloseable {
    List<Product> getProducts();
    
    void createProducts();

    void addProduct(Product product);

    Product minPrice();

    Product maxPrice();

    Long sumPrice();

    Integer countProducts();
}
