package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bean.User;

/**
 * 用户数据库操作持久层
 * @author windows7
 *
 */
public interface UserRepository extends JpaRepository<User, Integer>
{

}
