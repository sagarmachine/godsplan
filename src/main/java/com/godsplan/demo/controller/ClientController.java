package com.godsplan.demo.controller;


import com.godsplan.demo.entity.Client;
import com.godsplan.demo.repo.ClientRepository;
import com.godsplan.demo.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    @Autowired
    IClientService clientService;

    @Autowired
    ClientRepository clientRepository;

    @PostMapping(value="")
    public void addClientResponse(@RequestBody Client client){

        clientService.addClient(client);

    }

    @PostMapping(value = "/verify")
    public void verify(@RequestParam("url")String url, @RequestParam("to")String to){
        clientService.verifyClient(to,url);
    }

    @GetMapping(value = "/country/count")
    public ResponseEntity<?> getCountryCount(){

        return new ResponseEntity(clientRepository.getCountryCount(), HttpStatus.OK);
    }



}
