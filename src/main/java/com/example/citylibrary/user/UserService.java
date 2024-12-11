package com.example.citylibrary.user;

import com.example.citylibrary.exceptions.LibBadRequest;
import com.example.citylibrary.loan.Loans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // create new user
    public Users createNewUser(Users user) {
        // this just runs the password through the encoder and sets the generated hash to the password
        // before saving the user to db.
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setMember_number(generateMemberNumber());
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

    public String generateMemberNumber(){
        String latestMemberNumber = userRepo.findLastMemberNumber();
        String prefix = "M";
        String currentYear = String.valueOf(LocalDate.now().getYear());

        if (latestMemberNumber == null || !latestMemberNumber.contains(currentYear)){
            return prefix + currentYear + String.format("%05d", 1);
        }
            String sequencePart = latestMemberNumber.substring(5);
            int newSequence = Integer.parseInt(sequencePart) + 1;

            return prefix + currentYear + String.format("%05d", newSequence);
    }
}
