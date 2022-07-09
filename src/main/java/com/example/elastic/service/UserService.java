package com.example.elastic.service;

import com.example.elastic.domain.User;
import com.example.elastic.repository.search.UserSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserSearchRepository repository;

    @Autowired
    public UserService(UserSearchRepository repository){
        this.repository = repository;
    }

    public void save(final User user) {
        repository.save(user);

    }

    public User findById(final Long id){
        return repository.findById(id).orElse(null);
    }
}
