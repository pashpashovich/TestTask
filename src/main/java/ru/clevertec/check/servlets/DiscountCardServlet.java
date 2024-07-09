package ru.clevertec.check.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.DiscountCardImpl;
import ru.clevertec.check.repositories.DBUtils;
import ru.clevertec.check.repositories.DiscountCardRepository;

import java.io.IOException;
import java.util.Optional;


public class DiscountCardServlet extends HttpServlet {

    private DiscountCardRepository discountCardRepository;

    @Override
    public void init() throws ServletException {
        try {
            discountCardRepository = new DiscountCardRepository(
                    new DBUtils(System.getProperty("datasource.url"),
                            System.getProperty("datasource.username"),
                            System.getProperty("datasource.password"))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id != null) {
            Optional<DiscountCard> discountCard = discountCardRepository.findById(id);
            if (discountCard.isPresent()) {
                final String json = new ObjectMapper().writeValueAsString(discountCard.get());
                resp.setContentType("application/json");
                resp.getWriter().write(json);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Дисконтная карта не найдена");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Необходим номер дисконтной карты");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        try {
            DiscountCardImpl discountCard = mapper.readValue(req.getInputStream(), DiscountCardImpl.class);
            discountCardRepository.save(discountCard);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"сообщение\":\"Дисконтная карта добавлена успешно\"}");
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
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id дисконтной карты не передан");
                return;
            }
            int id = Integer.parseInt(idParam);
            String requestBody = new String(req.getInputStream().readAllBytes());
            ObjectNode objectNode = (ObjectNode) mapper.readTree(requestBody);
            DiscountCardImpl updatedDiscountCard = mapper.treeToValue(objectNode, DiscountCardImpl.class);
            discountCardRepository.update(updatedDiscountCard,id);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"сообщение\":\"Дисконтная карта обновлена успешно\"}");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный номер дисконтной карты");
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат JSON или данных");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id дисконтной карты не передан");
            return;
        }
        try {
            int id = Integer.parseInt(idParam);
            boolean isDeleted = discountCardRepository.deleteById(id);
            if (isDeleted) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"сообщение\":\"Дисконтная карта удалена успешно\"}");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Дисконтная карта не найдена");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный номер дисконтной карты");
        }
    }
}

