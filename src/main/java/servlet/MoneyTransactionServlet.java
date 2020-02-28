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
        String senderName = req.getParameter("SenderName");
        String senderPass = req.getParameter("SenderPass");
        long count = Long.parseLong(req.getParameter("count"));
        String nameTo = req.getParameter("nameTo");

        BankClientService bankClientService = new BankClientService();
        BankClient sender = null;
        sender = bankClientService.getClientByName(senderName);

        boolean result = false;
        if (sender != null && sender.getPassword().equals(senderPass)) {
            try {
                result = bankClientService.sendMoneyToClient(sender, nameTo, count);
            } catch (DBException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        String resultString = result
                ? "The transaction was successful"
                : "transaction rejected";

        /* формируем response */
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("message", resultString);
        resp.getWriter().println(
                PageGenerator
                        .getInstance()
                        .getPage("resultPage.html", pageVariables)
        );
        resp.setStatus(HttpServletResponse.SC_OK);
    }
    }

