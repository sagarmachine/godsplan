package com.godsplan.demo.repo;

import com.godsplan.demo.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository  extends JpaRepository<Client,Long> {

    Optional<Client> findByEmail(String email);

    List<Client> findAllByOrderByCountry();

}
