package com.iic.lab.RealEstate.controller;

import com.iic.lab.RealEstate.dto.LoginDto;
import com.iic.lab.RealEstate.dto.UserDto;
import com.iic.lab.RealEstate.model.User;
import com.iic.lab.RealEstate.service.UserService;
import com.sun.net.httpserver.HttpsServer;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
// ~~ SHOULD USE VALIDATED? ~~
//@Validated
@RequestMapping("/real-estate-api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDto userDto){
        User registeredUser = userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully. UserID: "+ registeredUser.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginDto loginDto, HttpSession session){
        User loggedInUser = userService.loginUser(loginDto);
        if(loggedInUser != null){
            session.setAttribute("user", loggedInUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Logged in.");
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session){
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully.");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpSession session){
        User loggedInUser = userService.getLoggedInUser(session);

        if (loggedInUser != null){
            return ResponseEntity.ok(loggedInUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }
    }
}
