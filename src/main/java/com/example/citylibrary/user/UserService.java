package com.example.citylibrary.user;

import com.example.citylibrary.exceptions.LibBadRequest;
import com.example.citylibrary.loan.Loans;
import com.example.citylibrary.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final UserDTOMapper userDTOMapper;
    private final JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserService(UserRepository userRepo, UserDTOMapper userDTOMapper, JWTService jwtService) {
        this.userRepo = userRepo;
        this.userDTOMapper = userDTOMapper;
        this.jwtService = jwtService;
    }

    // create new user, with hashed password
    public Users createNewUser(Users user) {
        // this just runs the password through the encoder and sets the generated hash to the password
        // before saving the user to db.
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setMember_number(generateMemberNumber());
        return userRepo.save(user);
    }

    // get a users loans
    public List<Loans> getLoansByUserId(Long id) {
        return userRepo.findById(id).get().getLoans();
    }

    // get a specific user by ID
    public Optional<UserDTO> getUserById(Long id) throws LibBadRequest {
        Optional<UserDTO> user = userRepo.findById(id).map(userDTOMapper);

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

    // TODO: with the auth here, I'm getting a stack overflow error for some reason, I guess it gets stuck in a loop? Recursive call?
    public String verify(Users user) {
       /* Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if(auth.isAuthenticated()) {
            return jwtService.generateToken(user.getEmail());
        }

        return "fail";*/

        return jwtService.generateToken(user.getEmail());
    }
}
