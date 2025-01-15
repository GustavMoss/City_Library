package com.example.citylibrary.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: behövs inte längre, ta bort. Tas denna bort så klagar den på entiteten.
// TODO: Ta bort allting admin-relaterat

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // login for admins
    @PostMapping("/login")
    public String login(@RequestBody @Valid Admins admin) {
        return adminService.verify(admin);
    }
}
