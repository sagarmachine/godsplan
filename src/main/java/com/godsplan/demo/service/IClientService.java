package com.godsplan.demo.service;

import com.godsplan.demo.entity.Client;

public interface IClientService {
    public  void addClient(Client client);

    public void deleteClient(String email);
    public void verifyClient(String to,String url);
}
