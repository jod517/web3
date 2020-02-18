

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import service.BankClientService;
import servlet.ApiServlet;
import servlet.MoneyTransactionServlet;
import servlet.RegistrationServlet;
import servlet.ResultServlet;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {


    public static void main(String[] args) throws Exception{


        ApiServlet apiServlet = new ApiServlet();
        MoneyTransactionServlet moneyTransactionServlet = new MoneyTransactionServlet();
        RegistrationServlet registrationServlet = new RegistrationServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(apiServlet), "/api/*");
        context.addServlet(new ServletHolder(moneyTransactionServlet), "/transaction");
        context.addServlet(new ServletHolder(registrationServlet), "/registration");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
