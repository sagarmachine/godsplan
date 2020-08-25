package com.godsplan.demo.repo;

import com.godsplan.demo.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClientRepository  extends JpaRepository<Client,Long> {

    Optional<Client> findByEmail(String email);

  //  List<Client> findAllByOrderByCountryCreatedOnDesc();

    //@Query(  value = "select COUNT(email),country from client group by country")
    @Query(nativeQuery = true, value = "select COUNT(email),country,category,code from client where category='no' group by country  UNION select COUNT(email),country,category,code from client where category='yes' group by country  UNION select COUNT(email),country,category,code from client where category='undecided' group by country"  )
    List<Object> getCountryCount();

    //List<Client> find

}
