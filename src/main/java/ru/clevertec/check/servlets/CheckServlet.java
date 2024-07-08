package ru.clevertec.check.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.CheckRunner;
import ru.clevertec.check.models.Receipt;
import ru.clevertec.check.repositories.DBUtils;
import ru.clevertec.check.service.ReceiptPrinter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CheckServlet extends HttpServlet {

    private CheckRunner checkRunner;
    private DBUtils dbUtils;

    @Override
    public void init() throws ServletException {
        dbUtils = new DBUtils(System.getProperty("datasource.url"),
                System.getProperty("datasource.username"),
                System.getProperty("datasource.password"));
        checkRunner = new CheckRunner();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map requestMap = mapper.readValue(req.getInputStream(), Map.class);

            String discountCardNumber = requestMap.get("discountCard") != null ? requestMap.get("discountCard").toString() : null;
            double balanceDebitCard = Double.parseDouble(requestMap.get("balanceDebitCard").toString());
            Map<Integer, Integer> products = new HashMap<>();
            for (Map<String, Object> productMap : (Iterable<Map<String, Object>>) requestMap.get("products")) {
                Integer productId = Integer.parseInt(productMap.get("id").toString());
                Integer quantity = Integer.parseInt(productMap.get("quantity").toString());
                products.merge(productId, quantity, Integer::sum);
            }
            Receipt receipt = checkRunner.generateReceipt(products, discountCardNumber, balanceDebitCard, dbUtils);
            ReceiptPrinter.printToFile(receipt,"./result.csv");
            File downloadFile = new File("./result.csv");
            FileInputStream inStream = new FileInputStream(downloadFile);
            String mimeType = getServletContext().getMimeType("./result.csv");
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            resp.setContentType(mimeType);
            resp.setContentLength((int) downloadFile.length());
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=\"" + downloadFile.getName() + "\"";
            resp.setHeader(headerKey, headerValue);
            OutputStream outStream = resp.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            inStream.close();
            outStream.close();
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ошибка обработки запроса: " + e.getMessage());
        }
    }
}

