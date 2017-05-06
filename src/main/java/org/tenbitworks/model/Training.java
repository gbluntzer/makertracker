package org.tenbitworks.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="training")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    String title;

    String description;

    long assetId;

    @Transient
    String assetTitle;

    public Training() { }

    public Training(long id) {
        this.id = id;
    }

    public Training(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Training(String title, String description, long assetId) {
        this.title = title;
        this.description = description;
        this.assetId = assetId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    @Column(name = "description", nullable = false, length = 1000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getAssetId() {
        return assetId;
    }

    public void setAssetId(long assetId) {
        this.assetId = assetId;
    }

    public String getAssetTitle() {
        return assetTitle;
    }

    public void setAssetTitle(String assetTitle) {
        this.assetTitle = assetTitle;
    }
}
