package com.url.shortener.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    @JsonIgnore
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Type name;

    @ManyToMany(mappedBy = "roles")
    Set<User> users = new HashSet<>();

    public Role() {
    }

    public Role(Type name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public Type getName() {
        return name;
    }

    public void setName(Type name) {
        this.name = name;
    }

    public enum Type {USER, MODERATOR, ADMIN}
}