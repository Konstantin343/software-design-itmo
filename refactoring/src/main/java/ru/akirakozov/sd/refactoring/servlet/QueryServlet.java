package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.database.ProductsDatabaseManager;
import ru.akirakozov.sd.refactoring.products.Product;
import ru.akirakozov.sd.refactoring.products.ProductsManager;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            try (ProductsManager productsManager = new ProductsDatabaseManager("jdbc:sqlite:test.db")) {
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h1>Product with max price: </h1>");
                Product product = productsManager.maxPrice();
                response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
                response.getWriter().println("</body></html>");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("min".equals(command)) {
            try (ProductsManager productsManager = new ProductsDatabaseManager("jdbc:sqlite:test.db")) {
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h1>Product with min price: </h1>");
                Product product = productsManager.minPrice();
                response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
                response.getWriter().println("</body></html>");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("sum".equals(command)) {
            try (ProductsManager productsManager = new ProductsDatabaseManager("jdbc:sqlite:test.db")) {
                    response.getWriter().println("<html><body>");
                    response.getWriter().println("Summary price: ");
                    Long sum = productsManager.sumPrice();
                    if (sum != null) {
                        response.getWriter().println(sum);
                    }
                    response.getWriter().println("</body></html>");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try (ProductsDatabaseManager database = new ProductsDatabaseManager("jdbc:sqlite:test.db")) {
                    response.getWriter().println("<html><body>");
                    response.getWriter().println("Number of products: ");
                    Integer count = database.countProducts();
                    if (count != null) {
                        response.getWriter().println(count);
                    }
                    response.getWriter().println("</body></html>");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
