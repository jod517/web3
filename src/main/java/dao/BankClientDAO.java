package dao;

import com.sun.deploy.util.SessionState;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }


    private @Nullable
    BankClient getClientBySqlQuery(final String sql, final String... args)
            throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                stmt.setString(i + 1, args[i]);
            }
            try (ResultSet result = stmt.executeQuery()) {
                BankClient client = null;
                if (result.next()) {
                    client = new BankClient(
                            result.getLong("id"),
                            result.getString("name"),
                            result.getString("password"),
                            result.getLong("money")
                    );
                }
                return client;
            }
        }
    }

    public @NotNull
    List<BankClient> getAllBankClient() throws SQLException {

        try (Statement stmt = connection.createStatement();
             ResultSet result = stmt.executeQuery("SELECT * FROM bank_client")
        ) {
            List<BankClient> clientsList = new ArrayList<>();

            while (result.next()) {
                BankClient client = new BankClient(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("password"),
                        result.getLong("money")
                );
                clientsList.add(client);
            }
            return (clientsList.isEmpty()) ? Collections.emptyList() : clientsList;
        }
    }

    public boolean validateClient(final String name, final String password)
            throws SQLException {
        BankClient client = getClientBySqlQuery(
                "SELECT * FROM bank_client WHERE name=? AND password=?",
                name,
                password
        );
        return client != null;
    }

    public void updateClientsMoney(
            final String name,
            final String password,
            final long transactValue
    )
            throws SQLException {

        int updatedRows = 0;

        if (validateClient(name, password)) {

            BankClient client = getClientByName(name);
            long finalAmount = client.getMoney() + transactValue;

            if (finalAmount >= 0) {
                try (PreparedStatement stmt = connection.prepareStatement(
                        "UPDATE bank_client SET money=? WHERE id=?")
                ) {
                    stmt.setLong(1, finalAmount);
                    stmt.setLong(2, client.getId());
                    updatedRows = stmt.executeUpdate();
                }
            }
        }
    }


    public @Nullable
    BankClient getClientById(final Long id) throws SQLException {
        return getClientBySqlQuery("SELECT * FROM bank_client WHERE id=?", id.toString());
    }

  public boolean isClientHasSum(final String name, final long expectedSum) throws SQLException {
       BankClient client = getClientByName(name);
       return (client != null) && (client.getMoney() >= expectedSum);
   }


    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_clien where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public @Nullable
    BankClient getClientByName(final String name) throws SQLException {
        return getClientBySqlQuery("SELECT * FROM bank_client WHERE name=?", name);
    }

    public void addClient(BankClient client) throws SQLException {
        int updatesCount = 0;
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO bank_client (name, password, money) values (?, ?, ?)")
        ) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPassword());
            stmt.setLong(3, client.getMoney());
            updatesCount = stmt.executeUpdate();
        }
    }




    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }


    public void deleteClient(final String name) throws SQLException {
        int updatedRows = 0;
        if (getClientByName(name) != null) {
            try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM bank_client WHERE name='?'")) {
                stmt.setString(1, name);
                updatedRows = stmt.executeUpdate();
            }
        }
        if (updatedRows != 1) {
            throw new IllegalStateException("Error");
        }
    }
}
