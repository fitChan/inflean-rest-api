package com.gisunlecture.infleanrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cglib.core.Local;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest  // MockMvc bean 을 자동 설정함. 따라서 그냥 가져와서 쓰자.. "웹" 관련 빈만 등록해줌.
@SpringBootTest //MOCK이 기본값이라 Mock을 이용한 태스트가 계속해서 가능 ctrl+좌클릭 해볼 것.
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    //스프링 MVC 테스트 핵심 클래스 이며 웹서버를 띄우지 않고도 스프링MVC(DispatcherServlet)가
    // 요청을 처리하는 과정을 확인 가능하기에 컨트롤러에서 많이 사용됌.
    MockMvc mockMvc;
    //단위 태스트라기에는 너무 많은 것들이 들어와있음..
    //dispatcherservlet을 만들어야 하기때문에 웹 서버를 띄우는 테스트보단 빠르지만 단위테스트보단 느림.

    @Autowired
    ObjectMapper objectMapper;

//    @MockBean
//    EventRepository eventRepository;  -> 실제 repo를 쓸거니까 지운다? 괜찮지?

    //이제 본격적으로 테스트를 만들거임.
    @Test
    public void createEvent() throws Exception {  //perform Exception 처리라는데 Excaption 박아버리네..?
        Event event = Event.builder()
                //존나 귀찮긴한데... 빌더로 설정을 해주는거임
                .id(100)
                .name("Spring")
                .description("REST API Developmetn with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 12, 23, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2스타텁 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();
/*
            repo에 Event event = Event.builder()가 save될때!! then --> event를 돌려줘!
        Mockito.when(eventRepository.save(event)).thenReturn(event);
            지금 문제가... controller Event와 현재 이곳의 Event는 다르다  지역변수 떄문인가? 여하튼
            event객체가 nullPoint가 뜬다 야발 상단 어노테이션도 @SpringBootTest @AutoConfigureMockMvd를 추가했다.
*/
        mockMvc.perform(post("/api/events") //post Method를 사용한다.
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))  //위에 @Autowired된 ObjectMapper objectMapper 확인 할 것.
                .andDo(print())
                .andExpect(status().isCreated()) //isCreated == is(201) 같은 내용
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION)) // "location"보다 Type-Safe한 방식
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))//"Content_Type", "application/hal+json" 보다 Type-Safe한 방식
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));


    }@Test
    public void createEvent_Bad_Request() throws Exception {  //perform Exception 처리라는데 Excaption 박아버리네..?
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Developmetn with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 12, 23, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2스타텁 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();


        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


}