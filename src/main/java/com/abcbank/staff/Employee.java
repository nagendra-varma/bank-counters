package com.abcbank.staff;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "staff")
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue
    private Integer id;

    @NonNull
    private String name;

    @Enumerated(value = STRING)
    @NonNull
    private Role role;
}
