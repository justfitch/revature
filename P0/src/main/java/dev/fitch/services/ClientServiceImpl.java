package dev.fitch.services;

import dev.fitch.data.ClientDAO;
import dev.fitch.entities.Client;

public class ClientServiceImpl implements ClientService{

    private ClientDAO clientDAO;

    public ClientServiceImpl(ClientDAO clientDAO){
        this.clientDAO = clientDAO;
    }

    @Override
    public Client getClientInfo(String username) {
        return clientDAO.getClientDetails(username);
    }

    @Override
    public boolean checkUsername(String username) {
        return this.clientDAO.checkUsername(username);

    }

    @Override
    public Client registerClient(Client client) {

        return this.clientDAO.createClient(client);
    }

    @Override
    public boolean checkPassword(String username, String password) {

        Client client = clientDAO.getClientDetails(username);
        if (password.equals(client.getPassword())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Client updateClient(Client client) {
        return clientDAO.updateClientInformation(client);
    }
}
