package com.abcbank.token;


import com.abcbank.counter.Counter;
import com.abcbank.counter.CounterFeedback;
import com.abcbank.service.ServiceRequest;
import com.abcbank.service.ServiceRequestDetails;
import com.abcbank.service.ServiceType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Optional;

import static com.abcbank.service.ServiceType.NON_PREMIUM;
import static com.abcbank.token.TokenStatus.COMPLETED;
import static com.abcbank.token.TokenStatus.CREATED;

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

    private Date startTime = new Date();

    private Date endTime;

    public Optional<ServiceRequest> getNextServiceRequest() {
        return serviceRequestList.stream().findFirst()
                .map(ServiceRequestDetails::getServiceRequest);
    }

    public void markLastRequestWithStatus(TokenStatus tokenStatus) {
        ServiceRequestDetails serviceRequestDetails = serviceRequestList.pop();
        serviceRequestDetails.setEndTime(new Date());

        CounterFeedback counterFeedback = new CounterFeedback();
        counterFeedback.setComments(serviceRequestDetails.getComment());
        counterFeedback.setCounterNo(assignedCounter.getNo());
        counterFeedback.setStartTime(serviceRequestDetails.getStartTime());
        counterFeedback.setEndTime(serviceRequestDetails.getEndTime());
        this.tokenStatus = tokenStatus;
        counterFeedbackList.push(counterFeedback);
    }

    public void markLastRequestProcessing() {
        ServiceRequestDetails serviceRequestDetails = serviceRequestList.peek();
        if (serviceRequestDetails != null) {
            serviceRequestDetails.setStartTime(new Date());
            tokenStatus = TokenStatus.IN_PROGRESS;
        }
    }

    public int getPendingRequestSize() {
        return serviceRequestList.size();
    }
}
