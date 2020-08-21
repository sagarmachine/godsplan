package com.godsplan.demo.service;

import com.godsplan.demo.entity.Admin;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IAdminService extends UserDetailsService {

    void addAdmin(Admin admin);

}
