package com.example.copsboot.user.web;

import com.example.copsboot.security.ApplicationUserDetails;
import com.example.copsboot.user.User;
import com.example.copsboot.user.UserNotFoundException;
import com.example.copsboot.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController //<1>
@RequestMapping("/api/users") //<2>
public class UserRestController {

    private final UserService service;

    @Autowired
    public UserRestController(UserService service) { //<3>
        this.service = service;
    }

    @GetMapping("/me") //<4>
    public UserDto currentUser(@AuthenticationPrincipal ApplicationUserDetails userDetails) { //<5>
        User user = service.getUser(userDetails.getUserId()) //<6>
                           .orElseThrow(() -> new UserNotFoundException(userDetails.getUserId()));
        return UserDto.fromUser(user); //<7>
    }

    //tag::post[]
    @PostMapping //<1>
    @ResponseStatus(HttpStatus.CREATED) //<2>
    public UserDto createOfficer(@Valid @RequestBody CreateOfficerParameters parameters) { //<3>
        User officer = service.createOfficer(parameters.getEmail(), //<4>
                parameters.getPassword());
        return UserDto.fromUser(officer); //<5>
    }
    //end::post[]
}
