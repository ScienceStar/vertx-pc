package com.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER",catalog = "vertxdatabases")
public class User
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="userId",length=15)
	private Integer userId;
	
	@Column(name="userName",length=25)
	private String userName;
	
	@Column(name="passWord",length=25)
	private String passWord;

	public Integer getUserId()
	{
		return userId;
	}

	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassWord()
	{
		return passWord;
	}

	public void setPassWord(String passWord)
	{
		this.passWord = passWord;
	}
	
	

}
