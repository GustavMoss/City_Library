package com.example.citylibrary.user;

import com.example.citylibrary.exceptions.LibBadRequest;
import com.example.citylibrary.loan.Loans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // create new user
    public Users createNewUser(Users user) {
        return userRepo.save(user);
    }

    // get a users loans
    public List<Loans> getLoansByUserId(Long id) {
        return userRepo.findById(id).get().getLoans();
    }

    // get a specific user by ID
    public Optional<Users> getUserById(Long id) throws LibBadRequest {
        Optional<Users> user = userRepo.findById(id);

        if(user.isPresent()) {
            return user;
        } else {
            throw new LibBadRequest("User not found");
        }

    }

    // update a users info
    public Users updateUserById(Long id, Users user) throws LibBadRequest {
        Optional<Users> userToUpdate = userRepo.findById(id);

        if (userToUpdate.isPresent()) {
            userToUpdate.get().setEmail(user.getEmail());
            userToUpdate.get().setFirst_name(user.getFirst_name());
            userToUpdate.get().setLast_name(user.getLast_name());
            userToUpdate.get().setMember_number(user.getMember_number());

            return userRepo.save(userToUpdate.get());
        } else {
            throw new LibBadRequest("Could not find user");
        }
    }
}
