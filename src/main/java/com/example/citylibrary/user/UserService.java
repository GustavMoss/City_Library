package com.example.citylibrary.user;

import com.example.citylibrary.exceptions.LibBadRequest;
import com.example.citylibrary.loan.Loans;
import com.example.citylibrary.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepo;

    @Autowired
    private JWTService jwtService;

    private final UsersDTOMapper usersDTOMapper;

    @Autowired
    AuthenticationManager authManager;

    // init the password encoder, built into spring security. Used to encode password with bcrypt, the strength is the number of rounds
    // seems very simple to implement.
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public UserService(UserRepository userRepo, UsersDTOMapper usersDTOMapper) {
        this.userRepo = userRepo;
        this.usersDTOMapper = usersDTOMapper;
    }

    // create new user
    public Users createNewUser(Users user) {
        // this just runs the password through the encoder and sets the generated hash to the password before saving the user to db.
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    // get a users loans
    public List<Loans> getLoansByUserId(Long id) {
        return userRepo.findById(id).get().getLoans();
    }

    // get a specific user by ID
    public Optional<UsersDTO> getUserById(Long id) throws LibBadRequest {
        Optional<UsersDTO> user = userRepo
                .findById(id)
                .map(usersDTOMapper);

        if(user.isPresent()) {
            System.out.println(user.get());
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

    public String verify(Users user) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if(auth.isAuthenticated()) {
            return jwtService.generateToken(user.getEmail());
        }

        return "fail";
    }
}
