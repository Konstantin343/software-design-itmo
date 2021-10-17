package ru.akirakozov.sd.refactoring.products;

/**
 * @author Konstantin343
 * 
 * Class for storing information about project
 */
public class Product {
    private final String name;
    
    private final long price;

    public Product(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }
}
