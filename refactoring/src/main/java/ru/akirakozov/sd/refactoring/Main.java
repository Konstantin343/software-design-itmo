package ru.akirakozov.sd.refactoring;

import ru.akirakozov.sd.refactoring.server.ProductsServer;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        new ProductsServer(8081, "jdbc:sqlite:test.db").start();
    }
}
