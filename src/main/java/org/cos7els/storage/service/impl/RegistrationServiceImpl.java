package org.cos7els.storage.service.impl;

import org.cos7els.storage.exception.BadDataException;
import org.cos7els.storage.mapper.UserToUserResponseMapper;
import org.cos7els.storage.model.domain.Authority;
import org.cos7els.storage.model.domain.Plan;
import org.cos7els.storage.model.domain.Subscription;
import org.cos7els.storage.model.domain.User;
import org.cos7els.storage.model.request.RegistrationRequest;
import org.cos7els.storage.model.response.UserResponse;
import org.cos7els.storage.service.AuthorityService;
import org.cos7els.storage.service.PlanService;
import org.cos7els.storage.service.RegistrationService;
import org.cos7els.storage.service.SubscriptionService;
import org.cos7els.storage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.cos7els.storage.util.ExceptionMessage.USERNAME_OR_EMAIL_EXISTS;

@Service
@PropertySource("classpath:variables.properties")
public class RegistrationServiceImpl implements RegistrationService {
    private final UserToUserResponseMapper userToUserResponseMapper;
    private final AuthorityService authorityService;
    private final PasswordEncoder passwordEncoder;
    private final PlanService planService;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    @Value("${free_plan_id}")
    private Long freePlanId;
    @Value("#{'${user_authorities_ids}'.split(',')}")
    private List<Long> userAuthoritiesIds;
    @Value("#{'${admin_authorities_ids}'.split(',')}")
    private List<Long> adminAuthoritiesIds;
    @Value("${free_plan_expiration}")
    private String freePlanExpiration;

    @Autowired
    public RegistrationServiceImpl(
            UserToUserResponseMapper userToUserResponseMapper,
            AuthorityService authorityService,
            PasswordEncoder passwordEncoder,
            PlanService planService,
            UserService userService,
            SubscriptionService subscriptionService
    ) {
        this.userToUserResponseMapper = userToUserResponseMapper;
        this.authorityService = authorityService;
        this.passwordEncoder = passwordEncoder;
        this.planService = planService;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    public UserResponse registerUser(RegistrationRequest registrationRequest) {
        return userToUserResponseMapper.userToResponse(createUser(registrationRequest, userAuthoritiesIds));
    }

    public User registerAdmin(RegistrationRequest registrationRequest) {
        return createUser(registrationRequest, adminAuthoritiesIds);
    }

    private User createUser(RegistrationRequest registrationRequest, List<Long> authoritiesIds) {
        if (userService.isUserExists(registrationRequest.getUsername()) ||
                userService.isEmailExists(registrationRequest.getEmail())) {
            throw new BadDataException(USERNAME_OR_EMAIL_EXISTS);
        }
        List<Authority> authorities = authoritiesIds
                .stream()
                .map(authorityService::getAuthority)
                .collect(Collectors.toList());
        User user = new User();
        user.setAuthorities(authorities);
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());
        user.setUsedSpace(0L);
        User savedUser = userService.saveUser(user);
        Plan plan = planService.getPlan(freePlanId);
        Subscription subscription = new Subscription();
        subscription.setUserId(user.getId());
        subscription.setPlan(plan);
        subscription.setIssuedDate(LocalDate.now());
        subscription.setExpiredDate(LocalDate.parse(freePlanExpiration));
        subscription.setIsActive(true);
        subscriptionService.insertSubscription(subscription);
        return savedUser;
    }
}