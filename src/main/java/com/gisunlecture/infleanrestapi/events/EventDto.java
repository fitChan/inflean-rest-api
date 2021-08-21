package com.gisunlecture.infleanrestapi.events;

import lombok.*;

import java.time.LocalDateTime;
@Builder @NoArgsConstructor @AllArgsConstructor @Data
public class EventDto {

    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location;  //(optional) 없으면 온라인 모임인거야. 위치가없으니까.
    private int basePrice; //optional
    private int maxPrice; //optional
    private int limitOfEnrollment;

}
