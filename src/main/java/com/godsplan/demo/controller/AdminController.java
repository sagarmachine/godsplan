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

    int pageSize=15;

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

    @GetMapping(value = "/data/{category}/{publicFigure}/{verified}/{search}/{page}")
    public ResponseEntity<?> getData(@PathVariable("page") int page,@PathVariable("category") String category,
                                     @PathVariable("publicFigure") String publicFigure,@PathVariable("verified") String verified,
                                     @PathVariable("search") String search){

        log.info(category+" "+publicFigure+" "+verified+" "+page);
        search=search.equals("all")?"":search;

        publicFigure=publicFigure.toUpperCase();
        Pageable pageable= PageRequest.of(page, pageSize, Sort.by("createdOn").descending());
        HashMap<String,Object>hashMap= new HashMap<>();
      if(category.equals("all")) {
          if (publicFigure.equals("ALL")) {
              if (verified.equals("all")) {
                  hashMap.put("content", clientRepository.findByFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(search,search,search,pageable));
                  hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(search,search,search)/(double) pageSize));
                  return new ResponseEntity<>(hashMap, HttpStatus.OK);
              } else {
                  if(verified.equals("true")) {
                      hashMap.put("content", clientRepository.findByVerifiedIsTrueAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc("OTHER",search,search,search, pageable));
                      hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByVerifiedIsTrueAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc("OTHER",search,search,search) / (double) pageSize));
                      return new ResponseEntity<>(hashMap, HttpStatus.OK);
                  }
                  else{
                      hashMap.put("content", clientRepository.findByVerifiedIsFalseAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc("OTHER",search,search,search, pageable));
                      hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByVerifiedIsFalseAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc("OTHER",search,search,search) / (double) pageSize));
                      return new ResponseEntity<>(hashMap, HttpStatus.OK);
                  }
              }
          } else {
              if (verified.equals("all")) {
                  hashMap.put("content",clientRepository.findByPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(publicFigure,search,search,search,pageable));
                  hashMap.put("totalPages",(int)Math.ceil(clientRepository.countByPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(publicFigure,search,search,search)/(double)pageSize));
                  return new ResponseEntity<>(hashMap, HttpStatus.OK);
              } else {
                  if(verified.equals("true")) {
                      hashMap.put("content", clientRepository.findByVerifiedIsTrueAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(publicFigure,search,search,search, pageable));
                      hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByVerifiedIsTrueAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(publicFigure,search,search,search)/(double) pageSize));
                      return new ResponseEntity<>(hashMap, HttpStatus.OK);
                  }
                  else{
                      hashMap.put("content", clientRepository.findByVerifiedIsFalseAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(publicFigure,search,search,search, pageable));
                      hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByVerifiedIsFalseAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(publicFigure,search,search,search)/(double) pageSize));
                      return new ResponseEntity<>(hashMap, HttpStatus.OK);
                  }
              }
          }
      }  else {
          if (publicFigure.equals("ALL")) {
              if (verified.equals("all")) {
                  hashMap.put("content", clientRepository.findByCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(category,search,search,search, pageable));
                  hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(category,search,search,search) / (double) pageSize));
                  return new ResponseEntity<>(hashMap, HttpStatus.OK);
              } else {
                  if(verified.equals("true")) {
                      hashMap.put("content", clientRepository.findByVerifiedIsTrueAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc("OTHER",category,search,search,search, pageable));
                      hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByVerifiedIsTrueAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc("OTHER",category,search,search,search) / (double) pageSize));
                      return new ResponseEntity<>(hashMap, HttpStatus.OK);
                  }
                  else{
                      hashMap.put("content", clientRepository.findByVerifiedIsFalseAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc("OTHER",category,search,search,search, pageable));
                      hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByVerifiedIsFalseAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc("OTHER",category,search,search,search) / (double) pageSize));
                      return new ResponseEntity<>(hashMap, HttpStatus.OK);
                  }
              }
          } else {
              if (verified.equals("all")) {
                  hashMap.put("content",clientRepository.findByPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(publicFigure,category,search,search,search,pageable));
                  hashMap.put("totalPages",(int)Math.ceil(clientRepository.countByPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(publicFigure,category,search,search,search)/(double)pageSize));
                  return new ResponseEntity<>(hashMap, HttpStatus.OK);
              } else {
                  if(verified.equals("true")) {
                      hashMap.put("content", clientRepository.findByVerifiedIsTrueAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(publicFigure,category,search,search,search, pageable));
                      hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByVerifiedIsTrueAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(publicFigure,category,search,search,search)/(double) pageSize));
                      return new ResponseEntity<>(hashMap, HttpStatus.OK);
                  }
                  else{
                      hashMap.put("content", clientRepository.findByVerifiedIsFalseAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(publicFigure,category,search,search,search, pageable));
                      hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByVerifiedIsFalseAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(publicFigure,category,search,search,search)/(double) pageSize));
                      return new ResponseEntity<>(hashMap, HttpStatus.OK);
                  }
              }
          }
      }
    }

    @GetMapping(value = "/data/country/{country}/{category}/{publicFigure}/{verified}/{search}/{page}")
    public ResponseEntity<?> getDataByCountry(@PathVariable("page") int page,@PathVariable("category") String category,@PathVariable("country") String country,
                                              @PathVariable("publicFigure") String publicFigure,@PathVariable("verified") String verified,
                                              @PathVariable("search") String search){
        log.info(country+" "+category+" "+publicFigure+" "+verified+" "+page+" "+search);
        search=search.equals("all")?"":search;
        publicFigure=publicFigure.toUpperCase();
        Pageable pageable= PageRequest.of(page, pageSize, Sort.by("createdOn").descending());
        HashMap<String,Object>hashMap= new HashMap<>();
        if(category.equals("all")) {
            if (publicFigure.equals("ALL")) {
                if (verified.equals("all")) {
                    hashMap.put("content", clientRepository.findByCountryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,search,search,search,pageable));
                    hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByCountryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,search,search,search) / (double) pageSize));
                    return new ResponseEntity<>(hashMap, HttpStatus.OK);
                } else {
                    if(verified.equals("true")) {

                        hashMap.put("content", clientRepository.findByCountryAndVerifiedIsTrueAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,"OTHER",search,search,search, pageable));
                        hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByCountryAndVerifiedIsTrueAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,"OTHER",search,search,search) / (double) pageSize));
                        return new ResponseEntity<>(hashMap, HttpStatus.OK);
                    }
                    else{
                        log.info(country);
                        hashMap.put("content", clientRepository.findByCountryAndVerifiedIsFalseAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,"OTHER",search,search,search, pageable));
                        hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByCountryAndVerifiedIsFalseAndPublicFigureIsNotAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,"OTHER",search,search,search) / (double) pageSize));
                        return new ResponseEntity<>(hashMap, HttpStatus.OK);
                    }
                }
            } else {
                if (verified.equals("all")) {

                    hashMap.put("content",clientRepository.findByCountryAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,publicFigure,search,search,search,pageable));
                    hashMap.put("totalPages",(int)Math.ceil(clientRepository.countByCountryAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,publicFigure,search,search,search)/(double)pageSize));
                    return new ResponseEntity<>(hashMap, HttpStatus.OK);
                } else {
                    if(verified.equals("true")) {
                        hashMap.put("content", clientRepository.findByCountryAndVerifiedIsTrueAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,publicFigure,search,search,search, pageable));
                        hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByCountryAndVerifiedIsTrueAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,publicFigure,search,search,search)/(double) pageSize));
                        return new ResponseEntity<>(hashMap, HttpStatus.OK);
                    }
                    else{
                        hashMap.put("content", clientRepository.findByCountryAndVerifiedIsFalseAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,publicFigure,search,search,search, pageable));
                        hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByCountryAndVerifiedIsFalseAndPublicFigureAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,publicFigure,search,search,search)/(double) pageSize));
                        return new ResponseEntity<>(hashMap, HttpStatus.OK);
                    }
                }
            }
        }  else {
            if (publicFigure.equals("ALL")) {
                if (verified.equals("all")) {
                    hashMap.put("content", clientRepository.findByCountryAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,category,search,search,search, pageable));
                    hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByCountryAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,category,search,search,search) / (double) pageSize));
                    return new ResponseEntity<>(hashMap, HttpStatus.OK);
                } else {
                    if(verified.equals("true")) {
                        hashMap.put("content", clientRepository.findByCountryAndVerifiedIsTrueAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,"OTHER",category,search,search,search, pageable));
                        hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByCountryAndVerifiedIsTrueAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,"OTHER",category,search,search,search) / (double) pageSize));
                        return new ResponseEntity<>(hashMap, HttpStatus.OK);
                    }
                    else{
                        hashMap.put("content", clientRepository.findByCountryAndVerifiedIsFalseAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,"OTHER",category,search,search,search, pageable));
                        hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByCountryAndVerifiedIsFalseAndPublicFigureIsNotAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,"OTHER",category,search,search,search) / (double) pageSize));
                        return new ResponseEntity<>(hashMap, HttpStatus.OK);
                    }
                }
            } else {
                if (verified.equals("all")) {
                    hashMap.put("content",clientRepository.findByCountryAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,publicFigure,category,search,search,search,pageable));
                    hashMap.put("totalPages",(int)Math.ceil(clientRepository.countByCountryAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,publicFigure,category,search,search,search)/(double)pageSize));
                    return new ResponseEntity<>(hashMap, HttpStatus.OK);
                } else {
                    if(verified.equals("true")) {
                        hashMap.put("content", clientRepository.findByCountryAndVerifiedIsTrueAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,publicFigure,category,search,search,search, pageable));
                        hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByCountryAndVerifiedIsTrueAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,publicFigure,category,search,search,search)/(double) pageSize));
                        return new ResponseEntity<>(hashMap, HttpStatus.OK);
                    }
                    else{
                        hashMap.put("content", clientRepository.findByCountryAndVerifiedIsFalseAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,publicFigure,category,search,search,search, pageable));
                        hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByCountryAndVerifiedIsFalseAndPublicFigureAndCategoryAndFirstNameContainingOrLastNameContainingOrEmailContainingOrderByCreatedOnDesc(country,publicFigure,category,search,search,search)/(double) pageSize));
                        return new ResponseEntity<>(hashMap, HttpStatus.OK);
                    }
                }
            }
        }
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
