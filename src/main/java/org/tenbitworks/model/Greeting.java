package org.tenbitworks.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="greeting")
public class Greeting {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String content;

	public Greeting() { }

	public Greeting(String content) {
		this.content = content;
	}
}
