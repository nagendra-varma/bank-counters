package com.abcbank.service;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ServiceRequestDetails {
    private ServiceRequest serviceRequest;
    private String comment;
    private Date startTime;
    private Date endTime;
}
