package service;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.*;
import java.util.List;

public class BankClientService {


    public BankClientService() {
    }

    public BankClient getClientById(long id) throws DBException {
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public @Nullable
    BankClient getClientByName(String name)  {
        try  {
            BankClientDAO dao = getBankClientDAO();
            return dao.getClientByName(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public @NotNull
    List<BankClient> getAllClient() {
        try  {
            BankClientDAO dao = getBankClientDAO();
            return dao.getAllBankClient();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }



    public boolean addClient(BankClient client) throws DBException {
        if (getClientByName(client.getName()) != null) {
            return false;
        }
        try  {
            BankClientDAO dao = getBankClientDAO();
            dao.addClient(client);
            return true;
        } catch (SQLException | IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean sendMoneyToClient(BankClient sender, String name, Long value) throws DBException, SQLException {
        try {
            BankClient recipient = getClientByName(name);
            if (getBankClientDAO().isClientHasSum(sender.getName(), value)){
                getBankClientDAO().updateClientsMoney(sender.getName(), sender.getPassword(), -value);
                getBankClientDAO().updateClientsMoney(name, recipient.getPassword(), value);
                return true;
            }
        } catch (SQLException | IllegalStateException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void createTable() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("db?").              //db name
                    append("user=root&").           //login
                    append("password=simonova12&").    //password
                    append("serverTimezone=UTC");   //timezone

            System.out.println("URL: " + url + "\n");
            return DriverManager.getConnection(url.toString());

        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }


    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }

}

