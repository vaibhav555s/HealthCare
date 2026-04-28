package com.hospital.hospitalapi.dto.request;

import lombok.Data;

@Data
public class FeedbackRequest {
    private Integer rating;         // 1-5
    private String comment;
}
