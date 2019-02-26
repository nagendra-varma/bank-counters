package com.abcbank.customer;

import com.abcbank.service.ServiceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @JsonIgnore
    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public String toString() {
        try {
            return toJson();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return super.toString();
    }
}
