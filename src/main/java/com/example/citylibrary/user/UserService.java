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

@Service
public class UserService {


    private final UserRepository userRepo;
    private final UserDTOMapper userDTOMapper;
    private final JWTService jwtService;

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
        user.setPassword(encoder.encode(user.getPassword()));
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
