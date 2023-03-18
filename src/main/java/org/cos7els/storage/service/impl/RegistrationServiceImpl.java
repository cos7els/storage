package org.cos7els.storage.service.impl;

import org.cos7els.storage.model.User;
import org.cos7els.storage.model.request.RegistrationRequest;
import org.cos7els.storage.repository.PlanRepository;
import org.cos7els.storage.repository.RoleRepository;
import org.cos7els.storage.repository.UserRepository;
import org.cos7els.storage.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final Long DEFAULT_PLAN_ID = 1L;
    private final Long DEFAULT_ROLE_ID = 1L;
    private final Long DEFAULT_USED_SPACE = 0L;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationServiceImpl(UserRepository userRepository, PlanRepository planRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Optional<User> registerUser(RegistrationRequest registrationRequest) {
        User user = new User();
        Long planId = registrationRequest.getPlanId();
        user.setPlan(planRepository.findById(planId == 0 ? DEFAULT_PLAN_ID : planId).get());
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());
        user.setRoles(List.of(roleRepository.findById(DEFAULT_ROLE_ID).get()));
        user.setUsedSpace(DEFAULT_USED_SPACE);
        return Optional.of(userRepository.save(user));
    }
}
