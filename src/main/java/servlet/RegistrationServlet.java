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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("name");
        String password = req.getParameter("password");
        long money = Long.parseLong(req.getParameter("money"));

        BankClient newClient = new BankClient(name, password, money);

        BankClientService bankClientService = new BankClientService();
        boolean result = false;
        try {
            result = bankClientService.addClient(newClient);
        } catch (DBException e) {
            e.printStackTrace();
        }

        String resultString = result ? "Add client successful" : "Client not add";

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
