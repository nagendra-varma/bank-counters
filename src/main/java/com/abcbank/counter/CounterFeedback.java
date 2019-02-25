package com.abcbank.counter;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CounterFeedback {
    private Integer counterNo;
    private String comments;
    private Date startTime;
    private Date endTime;
}
