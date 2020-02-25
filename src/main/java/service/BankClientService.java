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
    BankClient getClientByName(String name) throws DBException {
        try  {
            BankClientDAO dao = getBankClientDAO();
            return dao.getClientByName(name);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
    public @NotNull
    List<BankClient> getAllClient() throws DBException {
        try  {
            BankClientDAO dao = getBankClientDAO();
            return dao.getAllBankClient();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }



    public boolean addClient(BankClient client) throws DBException, SQLException {
        BankClientDAO dao = getBankClientDAO();
        dao.addClient(client);
        return true;
    }


    public boolean sendMoneyToClient(BankClient sender, String name, Long value) throws DBException, SQLException {
        BankClient recipient = getClientByName(name);
        BankClientDAO dao = getBankClientDAO();
        if (sender == null || recipient == null || value <= 0) {
            return false;
        }
        if (dao.validateClient(sender.getName(), sender.getPassword()) && dao.isClientHasSum(sender.getName(), value)) {
            dao.updateClientsMoney(sender.getName(), sender.getPassword(), -value);
            dao.updateClientsMoney(name, recipient.getPassword(), value);
            return true;
        } else {
            return false;
        }
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
//    public boolean deleteClient(String name) {
//        if (getClientByName(name) == null) {
//            retur n false;
//        }
//        try (BankClientDAO dao = getBankClientDAO()) {
//            dao.deleteClient(name);
//            return true;
//        } catch (IllegalStateException | SQLException e) {
//            throw new DBException(e);
//        }
//    }
}

