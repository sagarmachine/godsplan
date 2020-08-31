package com.godsplan.demo.controller;


import com.godsplan.demo.entity.Client;
import com.godsplan.demo.repo.ClientRepository;
import com.godsplan.demo.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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

    @GetMapping(value = "/publicFigure/{country}/{page}")
    public ResponseEntity<?> getList(@PathVariable("page")int page, @PathVariable("country")String country){
        Pageable pageable =PageRequest.of(page, 5);
        HashMap<String,Object> hashMap= new HashMap<>();
        if(country.equals("all")) {
            hashMap.put("content", clientRepository.findByPublicFigureIsNotOrderByCreatedOnDesc("OTHER", pageable));
            hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByPublicFigureIsNotOrderByCreatedOnDesc("OTHER") / (double) 5));
        }else{
            hashMap.put("content", clientRepository.findByPublicFigureIsNotAndCountryOrderByCreatedOnDesc("OTHER",country, pageable));
            hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByPublicFigureIsNotAndCountryOrderByCreatedOnDesc("OTHER",country) / (double) 5));
        }

        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }


    @GetMapping("/country")
    public ResponseEntity<?> getCountries(){
        return new ResponseEntity<>(clientRepository.getAllCountries(),HttpStatus.OK);
    }


}
