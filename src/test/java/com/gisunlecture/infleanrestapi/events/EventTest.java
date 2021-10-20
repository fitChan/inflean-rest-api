package com.gisunlecture.infleanrestapi.events;



import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {
    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Inflearn Spring Rest API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
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



    @ParameterizedTest
    @MethodSource("parametersForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // when
        event.update();

        // then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    @ParameterizedTest
    @MethodSource("parametersForTestOffline")
    public void testOffline() {
        Event event = Event.builder()
                .location("강남역 네이버 D2 스타텁 팩토리")
                .build();
        //when
        event.update();
        //Then
        assertThat(event.isOffline()).isTrue();

    }

    private static Stream<Arguments> parametersForTestFree() {
        return Stream.of(
                Arguments.of(0,0, true),
                Arguments.of(100, 0, false),
                Arguments.of(0, 100, false),
                Arguments.of(100, 200, false)
        );
    }

    private static Stream<Arguments>  parametersForTestOffline(){
        return Stream.of(
                Arguments.of("강남", true),
                Arguments.of(null, false),
                Arguments.of("   ", false)
        );
    }
}