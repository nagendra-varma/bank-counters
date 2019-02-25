package com.abcbank.customer;

import com.abcbank.service.ServiceType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "customer")
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String address;

    @Enumerated(value = STRING)
    private ServiceType serviceType;
}
