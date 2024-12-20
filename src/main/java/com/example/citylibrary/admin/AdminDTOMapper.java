package com.example.citylibrary.admin;

import org.springframework.stereotype.Service;

@Service
public class AdminDTOMapper {

    public AdminDTO toDTO(Admins admin) {
        if (admin == null) {
            return null;
        }

        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(admin.getAdmin_id());
        adminDTO.setUsername(admin.getUsername());

        return adminDTO;
    }

    public Admins toAdminDTO(AdminDTO adminDTO) {
        if (adminDTO == null) {
            return null;
        }

        Admins admins = new Admins();
        admins.setAdmin_id(adminDTO.getId());
        admins.setUsername(adminDTO.getUsername());

        return admins;
    }
}
