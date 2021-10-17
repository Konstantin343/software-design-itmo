package ru.akirakozov.sd.refactoring.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.database.ProductsDatabaseManager;
import ru.akirakozov.sd.refactoring.html.ProductsHtmlBuilder;
import ru.akirakozov.sd.refactoring.html.SimpleProductsHtmlBuilder;
import ru.akirakozov.sd.refactoring.products.ProductsManager;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

public class ProductsServer {
    private final String databaseConnectionString;
    private final int port;

    public ProductsServer(int port, String databaseConnectionString) {
        this.port = port;
        this.databaseConnectionString = databaseConnectionString;
    }
    
    public void start() throws Exception {
        try (ProductsManager productsManager = new ProductsDatabaseManager(databaseConnectionString)) {
            productsManager.createProducts();
        }

        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ProductsHtmlBuilder htmlBuilder = new SimpleProductsHtmlBuilder();

        context.addServlet(new ServletHolder(new AddProductServlet(htmlBuilder)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(htmlBuilder)), "/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(htmlBuilder)), "/query");

        server.start();
        server.join();
    }
}
