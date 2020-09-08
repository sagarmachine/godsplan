package com.godsplan.demo.controller;


import com.godsplan.demo.entity.Client;
import com.godsplan.demo.repo.ClientRepository;
import com.godsplan.demo.service.IClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    @Autowired
    IClientService clientService;

    @Autowired
    ClientRepository clientRepository;


    int pageSize=24;


    @PostMapping(value="")
    public void addClientResponse(@RequestBody Client client){

        clientService.addClient(client);

    }

    @PostMapping(value = "/verify")
    public void verify(@RequestParam("url")String url,@RequestParam("category")String category,@RequestParam("name")String name,@RequestParam("to")String to){
        clientService.verifyClient(name,category,to,url);
    }

    @GetMapping(value = "/country/count")
    public ResponseEntity<?> getCountryCount(){

        return new ResponseEntity(clientRepository.getCountryCount(), HttpStatus.OK);
    }

    @GetMapping(value = "/publicFigure/{country}/{search}/{page}")
    public ResponseEntity<?> getList(@PathVariable("page")int page, @PathVariable("country")String country, @PathVariable("search")String search){
        Pageable pageable= PageRequest.of(page,24 , Sort.by("createdOn").descending());
        HashMap<String,Object> hashMap= new HashMap<>();
        if(search.equals("all"))
             search="";

        if(country.equals("all")) {
            hashMap.put("content", clientRepository.findByPublicFigureIsNotOrderByCreatedOnDesc("OTHER",search, pageable));
            hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByPublicFigureIsNotOrderByCreatedOnDesc("OTHER",search) / (double) 16));
        }else{
            hashMap.put("content", clientRepository.findByPublicFigureIsNotAndCountryOrderByCreatedOnDesc("OTHER",country,search, pageable));
            hashMap.put("totalPages", (int) Math.ceil(clientRepository.countByPublicFigureIsNotAndCountryOrderByCreatedOnDesc("OTHER",country,search) / (double) 16));
        }

        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }


    @GetMapping("/country")
    public ResponseEntity<?> getCountries(){
        return new ResponseEntity<>(clientRepository.getAllCountries(),HttpStatus.OK);
    }


    @PostMapping("/claim")
     public  ResponseEntity<?> claim(@RequestParam("email")String email,@RequestParam("usedEmail")String usedEmail,@RequestParam("phone") String phone)
    {

        log.info(email+" "+usedEmail+" "+phone);

       // if(clientRepository.findByEmail((usedEmail)).isPresent())


        Optional<Client> clientOptional= clientRepository.findByEmail(email);
        if(!clientOptional.isPresent() || clientOptional.get().getVerified())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST) ;


        Client client=clientOptional.get();
        client.setClaimed(true);
        client.setClaimedOn(new Date());
        client.setPhone(phone);
        client.setEmail(usedEmail);

        if(!email.equals(usedEmail)){
            Optional<Client> clientOptional2= clientRepository.findByEmail(usedEmail);
            if(clientOptional2.isPresent()){
                Client client2=clientOptional2.get();
                clientRepository.delete(client2);
            }
        }

        clientRepository.save(client);
   return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    @PostMapping("/claim/verification")
    public void claimVerification(@RequestParam("url")String url,@RequestParam("email")String email,@RequestParam("to")String to){
        clientService.verifyClaim(email,to,url);

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




}
