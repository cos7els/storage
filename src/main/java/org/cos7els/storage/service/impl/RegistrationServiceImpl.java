package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.Plan;
import org.cos7els.storage.model.Authority;
import org.cos7els.storage.model.Subscription;
import org.cos7els.storage.model.User;
import org.cos7els.storage.model.request.RegistrationRequest;
import org.cos7els.storage.repository.PlanRepository;
import org.cos7els.storage.repository.AuthorityRepository;
import org.cos7els.storage.repository.UserRepository;
import org.cos7els.storage.service.RegistrationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.cos7els.storage.util.ExceptionMessage.PLAN_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.AUTHORITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:variables.properties")
public class RegistrationServiceImpl implements RegistrationService {
    @Value("${FREE_PLAN_ID}")
    private Long FREE_PLAN_ID;
    @Value("#{'${DEFAULT_AUTHORITIES_IDS}'.split(',')}")
    private List<Long> DEFAULT_AUTHORITIES_IDS;
    @Value("${DEFAULT_USED_SPACE}")
    private Long DEFAULT_USED_SPACE;
    @Value("${FREE_PLAN_SUBSCRIPTION_EXPIRED_DATE}")
    private String FREE_PLAN_SUBSCRIPTION_EXPIRED_DATE;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> registerUser(RegistrationRequest request) {
        Plan plan = planRepository.findById(FREE_PLAN_ID)
                .orElseThrow(() -> new NotFoundException(PLAN_NOT_FOUND));
        Subscription subscription = new Subscription();
        subscription.setPlan(plan);
        subscription.setIssuedDate(LocalDate.now());
        subscription.setExpiredDate(LocalDate.parse(FREE_PLAN_SUBSCRIPTION_EXPIRED_DATE));
        subscription.setIsActive(true);
        List<Authority> authorities = DEFAULT_AUTHORITIES_IDS
                .stream()
                .map(l -> authorityRepository
                        .findById(l)
                        .orElseThrow(() -> new NotFoundException(AUTHORITY_NOT_FOUND)))
                .collect(Collectors.toList());
        User user = new User();
        user.setSubscription(subscription);
        user.setAuthorities(authorities);
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setUsedSpace(DEFAULT_USED_SPACE);
        return Optional.of(userRepository.save(user));
    }
}