package com.gisunlecture.infleanrestapi.events;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {
    @Test
    public void builder(){
        Event event = Event.builder()
                .name("Inflearn Spring Rest API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        //Given
        Event event = new Event();
        String name = "Event";

        //When
        event.setName(name);
        String description = "Spring";
        event.setDescription(description);

        //Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
}