package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bean.User;
import com.dao.UserRepository;

@Service
public class UserService
{
	
	@Autowired
	private UserRepository userRepository;
	
    public User getUser(String userName,String passWord) {
    	return userRepository.findOne(1);
    };
}
