package ru.clevertec.check.servlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
            resp.getWriter().write("{\"сообщение\":\"Продукт добавлен успешно\"}");
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат JSON");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String idParam = req.getParameter("id");
            if (idParam == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID продукта не передан");
                return;
            }
            int id = Integer.parseInt(idParam);
            String requestBody = new String(req.getInputStream().readAllBytes());
            ObjectNode objectNode = (ObjectNode) mapper.readTree(requestBody);
            objectNode.put("id", id);
            ProductImpl updatedProduct = mapper.treeToValue(objectNode, ProductImpl.class);
            productRepository.update(updatedProduct);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"сообщение\":\"Продукт обновлен успешно\"}");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный ID продукта");
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат JSON или данных");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID продукта не передан");
            return;
        }
        try {
            int id = Integer.parseInt(idParam);
            boolean isDeleted = productRepository.deleteById(id);
            if (isDeleted) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"сообщение\":\"Продукт удален успешно\"}");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Продукт не найден");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный ID продукта");
        }
    }

}
