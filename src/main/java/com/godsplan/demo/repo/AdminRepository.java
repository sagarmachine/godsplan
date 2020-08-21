package com.godsplan.demo.repo;


import com.godsplan.demo.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Long> {

    Optional<Admin> findByAdminId(String adminId);

}
