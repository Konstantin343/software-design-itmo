package ru.akirakozov.sd.refactoring;

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

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        try (ProductsManager productsManager = new ProductsDatabaseManager("jdbc:sqlite:test.db")) {
            productsManager.createProducts();
        }

        Server server = new Server(8081);

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
