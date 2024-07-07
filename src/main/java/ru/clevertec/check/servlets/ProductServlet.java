package ru.clevertec.check.servlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.ProductImpl;
import ru.clevertec.check.repositories.DBUtils;
import ru.clevertec.check.repositories.ProductRepository;

import java.io.BufferedReader;
import java.io.IOException;

public class ProductServlet extends HttpServlet {

    private ProductRepository productRepository;

    @Override
    public void init() throws ServletException {
        productRepository = new ProductRepository(new DBUtils(System.getProperty("datasource.url"), System.getProperty("datasource.username"), System.getProperty("datasource.password")));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id != null) {
            Product product = productRepository.findById(Integer.parseInt(id));
            if (product != null) {
                final String json = new ObjectMapper().writeValueAsString(product);
                resp.getWriter().write(json);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND,"Продукт не найден");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Необходим ID продукта");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        try {
            ProductImpl product = mapper.readValue(req.getInputStream(), ProductImpl.class);
            productRepository.save(product);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\":\"Product created successfully\"}");
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String idParam = req.getParameter("id");
            if (idParam == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is required");
                return;
            }
            int id = Integer.parseInt(idParam);
            ProductImpl updatedProduct = mapper.readValue(req.getInputStream(), ProductImpl.class);
            updatedProduct.setId(id);
            productRepository.update(updatedProduct);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\":\"Product updated successfully\"}");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID");
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format or data");
        }
    }



}
