package com.abcbank.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
public class ServiceRequestDetails {
    private ServiceRequest serviceRequest;
    private String comment;

    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm a z")
    private Date startTime;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm a z")
    private Date endTime;
}
