package com.youmap.service;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youmap.entity.User;
import com.youmap.repository.UserDAO;

@Service
public class EditProfileService {
	
	@Autowired
	UserDAO userDAO;

	public String setName(String username, String name, String surname) {
		User user = userDAO.findByUsername(username);
		user.setName(name);
		user.setSurname(surname);
		userDAO.save(user);
		return "Pomyślnie zmieniono!";
	}
	
	public String setEmail(String username, String email) {
		User user = userDAO.findByUsername(username);
		user.setEmail(email);
		userDAO.save(user);
		return "Pomyślnie zmieniono!";
	}

	public String setCountry(String username, String country) {
		User user = userDAO.findByUsername(username);
		user.setCountry(country);
		userDAO.save(user);
		return "Pomyślnie zmieniono!";
	}

	public String setBirthday(String username, int day, int month, int year) {
		User user = userDAO.findByUsername(username);
		Calendar birthday = new GregorianCalendar(year,month,day);
		user.setBirthday(birthday);
		userDAO.save(user);
		return "Pomyślnie zmieniono!";
	}
}