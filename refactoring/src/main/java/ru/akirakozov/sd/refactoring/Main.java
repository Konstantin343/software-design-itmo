package ru.akirakozov.sd.refactoring;

import ru.akirakozov.sd.refactoring.server.ProductsServer;

/**
 * @author Konstantin343
 * 
 * Main class for starting application
 * Usage: java <app-path> [port [dbConnectionString]] 
 */
public class Main {
    public static void main(String[] args) throws Exception {
        int port = args.length >= 1 ? Integer.parseInt(args[0]) : 8081;
        String databaseConnectionString = args.length >= 2 ? args[1] : "jdbc:sqlite:test.db";
        
        new ProductsServer(port, databaseConnectionString).start();
    }
}
