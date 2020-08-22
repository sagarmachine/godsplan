package com.godsplan.demo.service;

import com.godsplan.demo.entity.Admin;
import com.godsplan.demo.repo.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AdminServiceImpl implements IAdminService{


    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Optional<Admin> adminOptional=adminRepository.findByAdminId(s);

        if(!adminOptional.isPresent())
            return null;
        else
            return new User(adminOptional.get().getAdminId(),adminOptional.get().getAdminSecret(), new ArrayList<>());

    }

    @Override
    public void addAdmin(Admin admin) {

        admin.setAdminSecret(passwordEncoder.encode(admin.getAdminSecret()));
        adminRepository.save(admin);

    }

}
