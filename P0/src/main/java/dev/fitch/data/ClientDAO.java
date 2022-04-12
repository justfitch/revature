package dev.fitch.data;

import dev.fitch.entities.Client;

public interface ClientDAO {


    //create
    Client createClient(Client client);

    //read
    boolean checkUsername(String username);

    Client getClientDetails(String username);

    //update
    Client updateClientInformation(Client client);

    //delete

}
