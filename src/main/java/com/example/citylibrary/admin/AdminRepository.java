package com.example.citylibrary.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
// TODO: DELETE?
@Repository
public interface AdminRepository extends JpaRepository<Admins, Long> {
    Admins findByUsername(String username);
}
