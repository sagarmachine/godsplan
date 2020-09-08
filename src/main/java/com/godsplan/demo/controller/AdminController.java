package com.godsplan.demo.controller;


import com.godsplan.demo.entity.Admin;
import com.godsplan.demo.entity.Client;
import com.godsplan.demo.repo.ClientRepository;
import com.godsplan.demo.security.JWTUtil;
import com.godsplan.demo.service.IAdminService;
import com.godsplan.demo.service.IClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/admin")
@Slf4j
public class AdminController {


    @Autowired
    IAdminService adminService;

    @Autowired
    IClientService clientService;

    @Autowired
    ClientRepository clientRepository;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTUtil jwtUtil;

    int pageSize=100;

    @DeleteMapping(value="/client")
    public void removeClientResponse(@RequestParam("email")String email){
log.info(SecurityContextHolder.getContext().getAuthentication().getName()+"");
        clientService.deleteClient(email);
    }

    @PostMapping(value="")
    public  void addAdmin(@RequestBody Admin admin){
        log.info(SecurityContextHolder.getContext().getAuthentication().getName()+"");

        adminService.addAdmin(admin);
    }


    @PostMapping(value="/authenticate")
    public  ResponseEntity<?> authenticate(@RequestBody Admin admin){
        log.info(admin.getAdminId() +"  "+admin.getAdminSecret());

       Authentication token= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getAdminId(),admin.getAdminSecret(),new ArrayList<>()));
         if(token.isAuthenticated()){
             String jwt=jwtUtil.generateToken(adminService.loadUserByUsername(admin.getAdminId()));
             String authorization="Bearer "+jwt;
             HttpHeaders headers=new HttpHeaders();
             headers.add("Authorization",authorization);
             return new ResponseEntity("Authenticated",headers,HttpStatus.OK);
         }
         return  new ResponseEntity<>("INVALID CREDENTIALS",HttpStatus.NOT_ACCEPTABLE);
    }



    @PostMapping("/verifyToggle")
    public void verifyClient(@RequestParam("email")String email){
             Client client= clientRepository.findByEmail(email).get();
             client.setVerified(!client.getVerified());
             client.setClaimed(false);
             clientRepository.save(client);
    }



    @GetMapping("/country")
    public ResponseEntity<?> getCountries(){
        return new ResponseEntity<>(clientRepository.getAllCountries(),HttpStatus.OK);
    }


    @PostMapping("/client/background")
    public void updateClientBackground(@RequestParam("email")String email , @RequestParam("background")String background){
        Client client= clientRepository.findByEmail(email).get();
        client.setBackground(background);
        clientRepository.save(client);

    }

    @PostMapping("/claim/decline")
    public  void  claimDecline(@RequestParam("email")String email)
    {
        Client client= clientRepository.findByEmail(email).get();
        client.setClaimed(false);
        //client.setClaimedOn(new Date());
        clientRepository.save(client);

    }


    @GetMapping("/claim/{search}/{page}")
    public ResponseEntity<?> getClaims(@PathVariable("page")int page,@PathVariable("search")String search){

        search=search.trim();
        Pageable pageable= PageRequest.of(page,pageSize , Sort.by("createdOn").descending());

        if(search.equals("all"))
            search="";

        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("content",clientRepository.findByClaimedIsTrueOrderByClaimedOnDesc(search,pageable));
        hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByClaimedIsTrueOrderByClaimedOnDesc(search)/(double)pageSize));


        return new ResponseEntity<>(hashMap,HttpStatus.OK);
    }



    @PostMapping("/client")
    public void addClient(@RequestBody Client client){

        clientService.addClient(client);

    }



}
