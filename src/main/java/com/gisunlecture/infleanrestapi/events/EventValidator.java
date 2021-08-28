package com.gisunlecture.infleanrestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {

        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
//            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
//            errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong");
            errors.reject("wrongPrices", "Values of prices is wrong");
        }
        //rejectValue -> field Error
        //reject -> global Error

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is wrong");
        }

        //  TODO 나머지 VALIDATOR도 해야함.
        //  TODO >> BeginEventDateTime, CloseEnrollmentDateTime

    }

}
