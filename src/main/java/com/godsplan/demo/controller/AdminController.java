package com.godsplan.demo.controller;


import com.godsplan.demo.entity.Admin;
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

    int pageSize=5;

    @DeleteMapping(value="/client")
    public void removeClientResaponse(@RequestParam("email")String email){
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

    @GetMapping(value = "/data/{category}/{page}")
    public ResponseEntity<?> getData(@PathVariable("page") int page,@PathVariable("category") String category){
        Pageable pageable= PageRequest.of(page, pageSize, Sort.by("createdOn").descending());
        HashMap<String,Object>hashMap= new HashMap<>();
      if(category.equals("all"))
        return new ResponseEntity<>(clientRepository.findAll(pageable), HttpStatus.OK);
      else {
          hashMap.put("content",clientRepository.findByCategory(category, pageable));
          hashMap.put("totalPages",(int)Math.ceil(clientRepository.countByCategory(category)/(double)pageSize));
          return new ResponseEntity<>(hashMap, HttpStatus.OK);
      }
    }

    @GetMapping(value = "/data/country/{country}/{category}/{page}")
    public ResponseEntity<?> getDataByCountry(@PathVariable("page") int page,@PathVariable("category") String category,@PathVariable("country") String country){
        Pageable pageable= PageRequest.of(page, pageSize, Sort.by("createdOn").descending());
        HashMap<String,Object>hashMap= new HashMap<>();
        if(category.equals("all")) {
            hashMap.put("content",clientRepository.findByCountryOrderByCreatedOn(country, pageable));
            hashMap.put("totalPages",(int)Math.ceil(clientRepository.countByCountry(country)/(double)pageSize));
            return new ResponseEntity<>(hashMap, HttpStatus.OK);
        }
        else {
            hashMap.put("content",clientRepository.findByCountryAndCategoryOrderByCreatedOn(country, category, pageable));
            hashMap.put("totalPages",(int)Math.ceil(clientRepository.countByCountryAndCategory(country,category)/(double)pageSize));
            return new ResponseEntity<>(hashMap, HttpStatus.OK);
        }
    }

    @GetMapping("/country")
    public ResponseEntity<?> getCountries(){
        return new ResponseEntity<>(clientRepository.getAllCountries(),HttpStatus.OK);
    }




}
