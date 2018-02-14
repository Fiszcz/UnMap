package com.youmap.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    //authorisation and authorization -> see com.youmap.confige.SecurityConfig
    @Column(columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String username;
    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean enabled;
    //two options: ROLE_ADMIN, ROLE_USER
    @Column(name = "authority", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @Column(columnDefinition = "VARCHAR(20)")
    private String name;
    @Column(columnDefinition = "VARCHAR(20)")
    private String surname;
    @Column(columnDefinition = "VARCHAR(40)")
    private String email;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private List<Travel> travels;

    // constructors
    public User() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Travel> getTravels() {
		return travels;
	}

	public void setTravels(List<Travel> travels) {
		this.travels = travels;
	}



}