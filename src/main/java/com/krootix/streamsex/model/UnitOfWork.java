package com.krootix.streamsex.model;

import java.util.Objects;

public class UnitOfWork {

    private Long id;
    private String title;
    private String description;

    private Integer laborCosts;

    private Long version = 0L;

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

    public Integer getLaborCosts() {
        return laborCosts;
    }

    public void setLaborCosts(Integer laborCosts) {
        this.laborCosts = laborCosts;
    }

    private UnitOfWork(Long id, String title, String description, Integer laborCosts) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.laborCosts = laborCosts;
    }

    public static UnitOfWork createUnitOfWork(Long id, String title, String description, int laborCosts) {
        return new UnitOfWork(id, title, description, laborCosts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitOfWork that = (UnitOfWork) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UnitOfWork{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", laborCosts=" + laborCosts +
                ", version=" + version +
                '}';
    }
}