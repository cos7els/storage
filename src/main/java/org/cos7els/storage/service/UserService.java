package org.cos7els.storage.service;

import org.cos7els.storage.model.User;
import org.cos7els.storage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@PropertySource(value = "classpath:plan.properties")
public class UserService {
    private final UserRepository userRepository;
    @Value("${freePlanId}")
    private long freePlanId;
    @Value("${freePlanEndDate}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate freePlanEndDate;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(long id) {
        return userRepository.findById(id);
    }

    public User getUser(String userName) {
        return userRepository.findUserByUserName(userName);
    }

    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        if (user.getPlanId() == 0) {
            setFreePlan(user);
        }
        user.setRegistrationDate(LocalDate.now());
        return userRepository.save(user);
    }

    private void setFreePlan(User user) {
        user.setPlanId(freePlanId);
        user.setPlanStartDate(LocalDate.now());
        user.setPlanEndDate(freePlanEndDate);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public void deleteUsers() {
        userRepository.deleteAll();
    }
}