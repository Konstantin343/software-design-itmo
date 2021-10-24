package ru.akirakozov.sd.refactoring.database;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.akirakozov.sd.refactoring.products.Product;
import ru.akirakozov.sd.refactoring.products.ProductsManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

public class ProductsDatabaseManagerTest {
    private final static String DB_NAME = "test.db";
    private ProductsManager productsManager;
    
    @BeforeMethod
    public void startDatabaseManager() throws SQLException {
        productsManager = new ProductsDatabaseManager("jdbc:sqlite:" + DB_NAME);
        productsManager.createProducts();
    }
    
    @AfterMethod
    public void deleteDatabase() throws IOException {
        Files.deleteIfExists(Path.of(DB_NAME));
    }
    
    @Test
    public void testOperations() {
        Product productPhone = new Product("Phone", 80);
        Product productTv = new Product("TV", 120);
        productsManager.addProduct(productPhone);
        productsManager.addProduct(productTv);

        List<Product> products = productsManager.getProducts();
        Assert.assertEquals(productPhone, products.get(0));
        Assert.assertEquals(productTv, products.get(1));
        
        Assert.assertEquals((int) productsManager.countProducts(), 2);
        Assert.assertEquals((long) productsManager.sumPrice(), 200);
        Assert.assertEquals(productsManager.minPriceProduct(), productPhone);
        Assert.assertEquals(productsManager.maxPriceProduct(), productTv);
    }
}
