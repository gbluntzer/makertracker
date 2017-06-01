package org.tenbitworks.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name="news")
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	@OneToOne
	@JoinColumn(name="username")
	private User user;
	
	@NotNull
	private Date createdAt;
	
	private Date expiresAt;
	
	private String title;
	
	@NotNull
	private String content;

	public News() { }
	
	public News(long id) {
		this.id = id;
	}
}
