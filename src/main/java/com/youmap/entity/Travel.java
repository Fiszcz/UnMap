package com.youmap.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TRAVELS")
public class Travel {

    @Id
    @GeneratedValue
    private Long id;
    
    @Column(columnDefinition = "VARCHAR(50)")
    private String title;
    @Column(columnDefinition = "VARCHAR(5000)", nullable = true)
    private String description;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date created;
    @Column(nullable = false)
    private Integer howManyPoints = 0;
    @Column(nullable = false)
    private Boolean access = true;
    private String code;
    private Integer likes;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="travel_id")
    private List<Point> points;
    

    // constructors
    public Travel() {
    }


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Date getCreated() {
		return created;
	}


	public void setCreated(Date created) {
		this.created = created;
	}


	public Integer getHowManyPoints() {
		return howManyPoints;
	}


	public void setHowManyPoints(Integer howManyPoints) {
		this.howManyPoints = howManyPoints;
	}


	public Boolean getAccess() {
		return access;
	}


	public void setAccess(Boolean access) {
		this.access = access;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public Integer getLikes() {
		return likes;
	}


	public void setLikes(Integer likes) {
		this.likes = likes;
	}


	public List<Point> getPoints() {
		return points;
	}


	public void setPoints(List<Point> points) {
		this.points = points;
	}



    // getter/setter methods

}