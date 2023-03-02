package org.cos7els.storage.controller;

import org.cos7els.storage.controller.request.RegistrationRequest;
import org.cos7els.storage.model.Plan;
import org.cos7els.storage.model.User;
import org.cos7els.storage.repository.PlanRepository;
import org.cos7els.storage.repository.RoleRepository;
import org.cos7els.storage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RegistrationController {
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public RegistrationController(UserRepository userRepository, PlanRepository planRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegistrationRequest request) {
        Plan plan = planRepository.findById(request.getPlanId() == null ? 1 : request.getPlanId()).get();
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPlan(plan);
        user.setRoles(List.of(roleRepository.findById(1L).get()));
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
