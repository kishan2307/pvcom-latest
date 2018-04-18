package com.pvcom.services.impl;

import com.pvcom.common.Util;
import com.pvcom.common.WorkflowConstants;
import com.pvcom.model.User;
import com.pvcom.projections.UserIdNames;
import com.pvcom.repository.UserRepository;
import com.pvcom.services.EmailService;
import com.pvcom.services.UserDetailsImpl;
import com.pvcom.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserServices, UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public User create(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        User user1 = repository.save(user);
        if (user1 != null) {
            emailService.sendNewUserWelcomeEmail(user1.getEmail(), user1.getFirstName(), user1.getEmail(), user.getPassword());
        }
        return user1;
    }

    public Iterable<User> list() {
        return repository.findAll();
    }

    @Transactional
    public User update(int id, User user) {
        if (repository.exists(id)) {
            user.setId(id);
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            return repository.save(user);
        }
        return null;
    }

    public User get(int id) {
        return repository.findOne(id);
    }

    public User get(String email) {
        return repository.findByEmail(email);
    }

    @Override
    @Transactional
    public WorkflowConstants.ErrorCodes resetPassword(String email) {
        User user = get(email);
        if (user != null) {
            try {
                String pwd = Util.generateString(6);
                user.setPassword(new BCryptPasswordEncoder().encode(pwd));
                repository.save(user);
                if (emailService.sendForgetPwdEmail(user.getEmail(), user.getFirstName(), pwd)) {
                    return WorkflowConstants.ErrorCodes.SUCCESS;
                } else {
                    return WorkflowConstants.ErrorCodes.EMAIL_SEND_FAIL;
                }
            } catch (Exception e) {
                return WorkflowConstants.ErrorCodes.INTERNAL_ERROR;
            }
        }
        return WorkflowConstants.ErrorCodes.INVALID_USERNAME;
    }

    @Transactional
    public boolean delete(int id) {
        if (repository.exists(id)) {
            repository.delete(id);
            return true;
        }
        return false;
    }

    public List<UserIdNames> getIdNames() {
        return repository.getAllByOrderById();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(user);
    }

    public Integer getIdByEmail(String email) {
        return repository.findIdByEmail(email);
    }
}
