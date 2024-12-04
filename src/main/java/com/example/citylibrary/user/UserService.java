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
    public List<Loans> getLoansByMemberNumber(String memberNumber) throws LibBadRequest {
        Optional<Users> user = userRepo.findByMemberNumber(memberNumber);

        if (user.isPresent()) {
            return user.get().getLoans();
        } else {
            throw new LibBadRequest("User not found");
        }
    }

    // get a specific user by ID
    public Optional<Users> getUserByMemberNumber(String memberNumber) throws LibBadRequest {
        Optional<Users> user = userRepo.findByMemberNumber(memberNumber);

        if (user.isPresent()) {
            return user;
        } else {
            throw new LibBadRequest("User not found");
        }
    }

    // update a users info
    public Users updateUserByMemberNumber(String memberNumber, Users user) throws LibBadRequest {
        Optional<Users> userToUpdate = userRepo.findByMemberNumber(memberNumber);

        if (userToUpdate.isPresent()) {
            Users existingUser = userToUpdate.get();
            existingUser.setEmail(user.getEmail());
            existingUser.setFirst_name(user.getFirst_name());
            existingUser.setLast_name(user.getLast_name());
            existingUser.setMember_number(user.getMember_number());

            return userRepo.save(existingUser);
        } else {
            throw new LibBadRequest("Could not find user");
        }
    }
}
