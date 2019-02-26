package com.abcbank.token;


import com.abcbank.counter.Counter;
import com.abcbank.counter.CounterFeedback;
import com.abcbank.service.ServiceRequest;
import com.abcbank.service.ServiceRequestDetails;
import com.abcbank.service.ServiceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Optional;

import static com.abcbank.service.ServiceType.NON_PREMIUM;
import static com.abcbank.token.TokenStatus.CREATED;
import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
public class Token {

    private static final Integer NO_COUNTER = -1;

    private Integer id;

    private Counter assignedCounter = new Counter();

    private Deque<CounterFeedback> counterFeedbackList = new ArrayDeque<>();

    private TokenStatus tokenStatus = CREATED;

    private ServiceType serviceType = NON_PREMIUM;

    private Deque<ServiceRequestDetails> serviceRequestList = new ArrayDeque<>();

    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm a z")
    private Date startTime = new Date();

    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm a z")
    private Date endTime;

    @JsonIgnore
    public Optional<ServiceRequest> getNextServiceRequest() {
        return serviceRequestList.stream().findFirst()
                .map(ServiceRequestDetails::getServiceRequest);
    }

    @JsonIgnore
    public void onRecentRequestCompleted(String comment) {
        ServiceRequestDetails serviceRequestDetails = serviceRequestList.poll();
        serviceRequestDetails.setEndTime(new Date());

        CounterFeedback counterFeedback = new CounterFeedback();
        counterFeedback.setComments(comment);
        counterFeedback.setCounterNo(assignedCounter.getNo());
        counterFeedback.setStartTime(serviceRequestDetails.getStartTime());
        counterFeedback.setEndTime(serviceRequestDetails.getEndTime());
        counterFeedbackList.push(counterFeedback);
    }

    @JsonIgnore
    public void markLastRequestProcessing() {
        ServiceRequestDetails serviceRequestDetails = serviceRequestList.peek();
        if (serviceRequestDetails != null) {
            serviceRequestDetails.setStartTime(new Date());
            tokenStatus = TokenStatus.IN_PROGRESS;
        }
    }

    @JsonIgnore
    public int getPendingRequestSize() {
        return serviceRequestList.size();
    }

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
