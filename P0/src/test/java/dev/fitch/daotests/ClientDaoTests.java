package dev.fitch.daotests;

import dev.fitch.data.AccountDAO;
import dev.fitch.data.AccountDAOPostgresImpl;
import dev.fitch.data.ClientDAO;
import dev.fitch.data.ClientDAOPostgresImpl;
import dev.fitch.entities.Client;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)// to run tests in order

public class ClientDaoTests {

    static ClientDAO clientDAO = new ClientDAOPostgresImpl();
    static Client testClient = null;

    @Test
    @Order(1)
    void create_and_get_client_test(){
        testClient = null;
        Client fred = new Client("flinty23", "Fred", "Flintstone", "stoneage23", "123 Gneiss Street, Boulder, CO", "fflint@aol.com");
        Client savedClient = clientDAO.createClient(fred);
        System.out.println(savedClient);
        ClientDaoTests.testClient = clientDAO.getClientDetails(savedClient.getUsername());
        //ClientDaoTests.testClient = savedClient;
        Assertions.assertNotNull(testClient);
        System.out.println(testClient);
    }

 /*   @Test
    @Order(2)
    void get_client_details_test(){
        testClient = null;
        testClient = clientDAO.getClientDetails(testClient.getUsername());
        Assertions.assertEquals("Flintstone", testClient.getLastName());

    }*/

    @Test
    @Order(2)
    void update_client_info_test(){
        System.out.println(testClient);
        testClient = clientDAO.getClientDetails(testClient.getUsername());
        ClientDaoTests.testClient.setAddress("325 Granite Ave.");
        clientDAO.updateClientInformation(testClient);
        Client retrievedClient = clientDAO.getClientDetails(testClient.getUsername());
        Assertions.assertEquals("325 Granite Ave.", retrievedClient.getAddress());
    }
}
