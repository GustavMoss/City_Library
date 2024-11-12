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

    // see a users active loans
    public List<Loans> getLoansById(Long id) {
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
            // will put values in db to null. Might be solvable with validation?
            // could also check for null values for each and then set it to the old value if the new is old?
            // seems like a lot of code though
            userToUpdate.get().setEmail(user.getEmail());
            userToUpdate.get().setFirst_name(user.getFirst_name());
            userToUpdate.get().setLast_name(user.getLast_name());
            userToUpdate.get().setMember_number(user.getMember_number());
            // set the loans to whatever the loans were, since we are not sending in new loans this way and we don't want them to be null
            userToUpdate.get().setLoans(userToUpdate.get().getLoans());

            return userRepo.save(userToUpdate.get());
        } else {
            throw new LibBadRequest("Could not find user");
        }
    }
}
