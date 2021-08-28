package com.gisunlecture.infleanrestapi.events;

import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
@Builder @NoArgsConstructor @AllArgsConstructor @Data
public class EventDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private LocalDateTime beginEnrollmentDateTime;
    @NotNull
    private LocalDateTime closeEnrollmentDateTime;
    @NotNull
    private LocalDateTime beginEventDateTime;
    @NotNull
    private LocalDateTime endEventDateTime;

    private String location;  //(optional) 없으면 온라인 모임인거야. 위치가없으니까.
    @Min(0)
    private int basePrice; //optional
    @Min(0)
    private int maxPrice; //optional
    @Min(0)
    private int limitOfEnrollment;

}
