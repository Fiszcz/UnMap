package com.youmap.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "POINTS")
public class Point {

    @Id
    @GeneratedValue
    private Long id;
    
    @Column(columnDefinition = "VARCHAR(2000)")
    private String description;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date added;
    @Column(name="date_photo")
    private long when;
    private Integer width;
	private Integer height;

	@Column(nullable = true)
    private Float ox;
    @Column(nullable = true)
    private Float oy;
    @Column(nullable = true)
    private Integer type;
    
    @Column(columnDefinition = "VARCHAR(50)", nullable = true)
    private String name;
    private String contentType;
    @Lob
    private byte[] photo;

    // constructors
    public Point() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getAdded() {
		return added;
	}

	public void setAdded(Date added) {
		this.added = added;
	}

	public Float getOx() {
		return ox;
	}

	public void setOx(Float ox) {
		this.ox = ox;
	}

	public Float getOy() {
		return oy;
	}

	public void setOy(Float oy) {
		this.oy = oy;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getPhoto() {
		return photo;
	}
	
    public Long getWhen() {
		return when;
	}

	public void setWhen(Long when) {
		this.when = when;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	
    public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}


    // getter/setter methods

}