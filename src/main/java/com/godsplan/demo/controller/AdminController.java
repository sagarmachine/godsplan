package com.godsplan.demo.controller;


import com.godsplan.demo.entity.Admin;
import com.godsplan.demo.repo.ClientRepository;
import com.godsplan.demo.service.IAdminService;
import com.godsplan.demo.service.IClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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

    @DeleteMapping(value="/client/{email}")
    public void removeClientResaponse(@PathVariable("email")String email){
log.info(SecurityContextHolder.getContext().getAuthentication().getName()+"");
        clientService.deleteClient(email);
    }

    @PostMapping(value="")
    public  void addAdmin(@RequestBody Admin admin){
        log.info(SecurityContextHolder.getContext().getAuthentication().getName()+"");

        adminService.addAdmin(admin);
    }


    @PostMapping(value="/authenticate")
    public  void authenticate(@RequestBody Admin admin){
        log.info(SecurityContextHolder.getContext().getAuthentication().getName()+" F");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getAdminId(),admin.getAdminSecret(),new ArrayList<>()));
        log.info(SecurityContextHolder.getContext().getAuthentication().getName()+" L");

    }

    @GetMapping(value = "/data")
    public ResponseEntity<?> getData(){

        return new ResponseEntity<>(clientRepository.findAllByOrderByCountry(), HttpStatus.OK);
    }




}
