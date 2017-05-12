package org.tenbitworks.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="completedtraining")
public class CompletedTraining {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NotNull
    @ManyToOne
    Training training;

    @NotNull
    @ManyToOne
    Member member;

    @Enumerated(EnumType.STRING)
    TrainingStatus status;

    Date trainingDate;
    
    public CompletedTraining() { }

    public CompletedTraining(long id) {
        this.id = id;
    }

    public CompletedTraining(Training training, Member member, Date trainingDate) {
        this.training = training;
        this.member = member;
        this.trainingDate = trainingDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public TrainingStatus getStatus() {
        return status;
    }

    public void setStatus(TrainingStatus status) {
        this.status = status;
    }

	public Training getTraining() {
		return training;
	}

	public void setTraining(Training training) {
		this.training = training;
	}
}
