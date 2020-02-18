package service;

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

    public BankClient getClientByName(String name) throws DBException {     // не проверен
        return getBankClientDAO().getClientByName(name);                   //
    }                                                                     //

    public List<BankClient> getAllClient() {
        return  null;
    }

//    public boolean deleteClient(String name) throws DBException {
//        if (getClientByName(name) != null) {
//            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM bank_client WHERE name='?'")) {
//                stmt.setString(1, name);
//                return false;
//            }
//        }
//    }

    public boolean addClient(BankClient client) throws DBException, SQLException {
        BankClientDAO dao = getBankClientDAO();
        dao.addClient(client);
            return true;
    }


    public boolean sendMoneyToClient(BankClient sender, String name, Long value) {
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
    public void createTable() throws DBException{
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
    public void deleteClient(final String name) throws SQLException {
        int updatedRows = 0;

        if ((name) != null) {
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM bank_client WHERE name='?'")) {
                stmt.setString(1, name);
                updatedRows = stmt.executeUpdate();
            }
        }
        if (updatedRows != 1) {
            // Если изменена не 1 строка в таблице, то что-то пошло не так.
            throw new IllegalStateException("Error while deleting client!");
        }
    }
