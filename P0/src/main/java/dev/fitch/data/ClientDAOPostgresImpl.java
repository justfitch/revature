package dev.fitch.data;

import dev.fitch.entities.Client;
import dev.fitch.utilities.ConnectionUtil;
import dev.fitch.utilities.LogLevel;
import dev.fitch.utilities.Logger;

import java.sql.*;

public class ClientDAOPostgresImpl implements ClientDAO {

    @Override
    public Client createClient(Client client) {
        try {
            Connection conn = ConnectionUtil.createConnection();
            String sql = "insert into client values (?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, client.getUsername()); // for the first ? in the PreparedStatement
            ps.setString(2, client.getFirstName());
            ps.setString(3, client.getLastName());
            ps.setString(4, client.getPassword());
            ps.setString(5, client.getAddress());
            ps.setString(6, client.getEmail());

            ps.execute();
            return client;

        } catch (SQLException e) {
            System.out.println("Client could not be created. Email address may already be associated with another user.");
            Logger.log(e.getMessage(), LogLevel.ERROR);
            return null;
        }
    }

    @Override
    public boolean checkUsername(String username) {

        try {
            Connection conn = ConnectionUtil.createConnection();
            String sql = "select count(*) from client where username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            rs.next(); // move to first record

            int count = rs.getInt("count");

            if (count == 1){
                return true;
            }else{
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Logger.log(e.getMessage(), LogLevel.ERROR);
            return false;
        }

    }

    @Override
    public Client getClientDetails(String username) {
        try {
            Connection conn = ConnectionUtil.createConnection();
            String sql = "select * from client where username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            rs.next(); // move to first record
            Client client = new Client();
            client.setUsername(rs.getString("username"));
            client.setFirstName(rs.getString("first_name"));
            client.setLastName(rs.getString("last_name"));
            client.setPassword(rs.getString("user_password"));
            client.setAddress(rs.getString("address"));
            client.setEmail(rs.getString("email"));
            return client;

        } catch (SQLException e) {
            e.printStackTrace();
            Logger.log(e.getMessage(), LogLevel.ERROR);
            return null;
        }


    }

    @Override
    public Client updateClientInformation(Client client) {

        try {
            Connection conn = ConnectionUtil.createConnection();
            String sql = "update client set username = ?, first_name = ?, last_name = ?, user_password = ?, address = ?, email = ? where username = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, client.getUsername());
            ps.setString(2, client.getFirstName());
            ps.setString(3, client.getLastName());
            ps.setString(4, client.getPassword());
            ps.setString(5, client.getAddress());
            ps.setString(6, client.getEmail());
            ps.setString(7, client.getUsername());

            ps.executeUpdate();
            return client;

        } catch (SQLException e) {
            System.out.println("Error. Unable to update client information. Email address may be associated with another user.");
            Logger.log(e.getMessage(), LogLevel.ERROR);
            return null;
        }
    }
}

