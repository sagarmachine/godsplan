package com.godsplan.demo.service;

import com.godsplan.demo.entity.Client;

public interface IClientService {
    public  void addClient(Client client);

    public void deleteClient(String email);
    public void verifyClient(String name, String category,String to,String url);
    public void verifyClaim(String email,String to,String url);
}
