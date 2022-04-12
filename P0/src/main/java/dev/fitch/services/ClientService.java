package dev.fitch.services;

import dev.fitch.entities.Client;

public interface ClientService {

    Client getClientInfo(String username);

    boolean checkUsername(String username);

    Client registerClient(Client client);

    boolean checkPassword(String username, String password);

    Client updateClient(Client client);
}
