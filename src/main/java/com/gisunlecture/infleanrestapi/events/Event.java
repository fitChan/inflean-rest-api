package com.gisunlecture.infleanrestapi.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter  @Setter
@EqualsAndHashCode(of = "id")
@Entity
//@Data --> 모든 프로퍼티를 다 가져옴 상호참조가 발생함.특히 Entity 위에는 더더욱..
public class Event {
    @Id @GeneratedValue
    private Integer id;
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
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING) // String 을 추천함.
    private EventStatus eventStatus  = EventStatus.DRAFT;

    public void update() {
        //update free
        if(this.basePrice == 0 && this.maxPrice ==0){
            this.free = true;
        }else{
            this.free = false;
        }

        //update offline
        if(this.location == null){
            this.offline = false;
        }else{
            this.offline = true;
        }
    }
}
