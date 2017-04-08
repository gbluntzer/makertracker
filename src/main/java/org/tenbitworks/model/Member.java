package org.tenbitworks.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "member")
public class Member {

  // An autogenerated id (unique for each user in the db)
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  
  @NotNull
  private String email;
  
  @NotNull
  private String firstName;

  @NotNull
  private String lastName;

  // Public methods
  
  public Member() { }

  public Member(long id) { 
    this.id = id;
  }

  public Member(String email, String firstName, String lastName) {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String toString() {
    return "Member{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            '}';
  }
}