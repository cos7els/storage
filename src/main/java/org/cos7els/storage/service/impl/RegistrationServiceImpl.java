package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.domain.Plan;
import org.cos7els.storage.model.domain.Authority;
import org.cos7els.storage.model.domain.Subscription;
import org.cos7els.storage.model.domain.User;
import org.cos7els.storage.model.request.RegistrationRequest;
import org.cos7els.storage.model.response.UserResponse;
import org.cos7els.storage.service.AuthorityService;
import org.cos7els.storage.service.PlanService;
import org.cos7els.storage.service.RegistrationService;
import org.cos7els.storage.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:variables.properties")
public class RegistrationServiceImpl implements RegistrationService {
    @Value("${FREE_PLAN_ID}")
    private Long FREE_PLAN_ID;
    @Value("${USER_DEFAULT_AUTHORITIES_IDS}")
    private List<Long> USER_DEFAULT_AUTHORITIES_IDS;
    @Value("#{'${ADMIN_DEFAULT_AUTHORITIES_IDS}'.split(',')}")
    private List<Long> ADMIN_DEFAULT_AUTHORITIES_IDS;
    @Value("${FREE_PLAN_SUBSCRIPTION_EXPIRED_DATE}")
    private String FREE_PLAN_SUBSCRIPTION_EXPIRED_DATE;
    private final AuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;
    private final PlanService planService;
    private final UserService userService;

    @Override
    public UserResponse registerUser(RegistrationRequest request) {
        Plan plan = planService.getPlan(FREE_PLAN_ID);
        Subscription subscription = new Subscription();
        subscription.setPlan(plan);
        subscription.setIssuedDate(LocalDate.now());
        subscription.setExpiredDate(LocalDate.parse(FREE_PLAN_SUBSCRIPTION_EXPIRED_DATE));
        subscription.setIsActive(true);
        List<Authority> authorities = USER_DEFAULT_AUTHORITIES_IDS
                .stream()
                .map(authorityService::getAuthority)
                .collect(Collectors.toList());
        User user = new User();
        user.setSubscription(subscription);
        user.setAuthorities(authorities);
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setUsedSpace(0L);
        return userService.userToResponse(userService.saveUser(user));
    }
}