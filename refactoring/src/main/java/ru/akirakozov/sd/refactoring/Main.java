package ru.akirakozov.sd.refactoring;

import ru.akirakozov.sd.refactoring.server.ProductsServer;

/**
 * @author Konstantin343
 * 
 * Main class for starting application
 * Usage: java <app-path> [port [dbConnectionString]] 
 * 
 * Defaults:
 * * port = 8081
 * * dbConnectionString = "jdbc:sqlite:test.db"
 */
public class Main {
    public static void main(String[] args) throws Exception {
        int port = args.length >= 1 ? Integer.parseInt(args[0]) : 8081;
        String databaseConnectionString = args.length >= 2 ? args[1] : "jdbc:sqlite:test.db";
        
        ProductsServer server = new ProductsServer(port, databaseConnectionString);
        try {
            server.start();
        } finally {
            server.stop();
        }
    }
}
