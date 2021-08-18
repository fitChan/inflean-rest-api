package com.gisunlecture.infleanrestapi.events;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
//repository 를 만들고 나서 생각해야 할것 --> TDD에 MockBean 추가하기


}
