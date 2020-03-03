package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/transaction")
public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        String senderName = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        String nameTo = req.getParameter("nameTo");
        Long count = Long.parseLong(req.getParameter("count"));
        try {

            BankClient sender = bankClientService.getClientByName(senderName);
            if (sender.getPassword().equals(senderPass)) {
                if (bankClientService.sendMoneyToClient(sender, nameTo, count)) {
                    pageVariables.put("message", "The transaction was successful");
                } else {
                    pageVariables.put("message", "transaction rejected");
                }
            } else {
                pageVariables.put("message", "transaction rejected");
            }
        } catch (DBException e) {
            pageVariables.put("message", "transaction rejected");
        } catch (SQLException e) {
            pageVariables.put("message", "transaction rejected");
        }


        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", pageVariables));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

    }
}