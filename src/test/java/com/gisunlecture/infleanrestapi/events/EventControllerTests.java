package com.gisunlecture.infleanrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisunlecture.infleanrestapi.common.RestDocsConfiguration;
import com.gisunlecture.infleanrestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest  // MockMvc bean 을 자동 설정함. 따라서 그냥 가져와서 쓰자.. "웹" 관련 빈만 등록해줌.
@SpringBootTest //MOCK이 기본값이라 Mock을 이용한 태스트가 계속해서 가능 ctrl+좌클릭 해볼 것.
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Import(RestDocsConfiguration.class)
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
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {  //perform Exception 처리라는데 Excaption 박아버리네..?
        EventDto event = EventDto.builder()
                // 귀찮긴한데... 빌더로 설정을 해주는거임
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
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing events")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("descriprion of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin new event of enrollment"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close new event of enrollment"),
                                fieldWithPath("beginEventDateTime").description("date time of begin new event"),
                                fieldWithPath("endEventDateTime").description("date time of close new event"),
                                fieldWithPath("location").description("location of close new event"),
                                fieldWithPath("basePrice").description("baseprice of close new event"),
                                fieldWithPath("maxPrice").description("maxPrice of close new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment of close new event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        //전체 조회가 하기 싫어? 그러면 relaxed를 붙여 그러면 깐깐하게 모든 부분을 검사하지 않아.. 안해주면 The following parts of the payload were not documented 에러가 발생해! links는 이미 문서화 해놨어
                        //relaxed를 사용할때 주의: 정확한 문서를 제작하기 어렵다. -> 문서 일부분만 테스트 하는 것이기 때문에 완벽하지 않다. 장점이자 단점임.
//                        relaxedResponseFields(
                        //link 추가
                        relaxedResponseFields(
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("descriprion of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin new event of enrollment"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close new event of enrollment"),
                                fieldWithPath("beginEventDateTime").description("date time of begin new event"),
                                fieldWithPath("endEventDateTime").description("date time of close new event"),
                                fieldWithPath("location").description("location of close new event"),
                                fieldWithPath("basePrice").description("basePrice of close new event"),
                                fieldWithPath("maxPrice").description("maxPrice of close new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment of close new event"),
                                fieldWithPath("free").description("whether free or not"),
                                fieldWithPath("offline").description("offline or online"),
                                fieldWithPath("eventStatus").description("event status")
//                                fieldWithPath("_links.self.href").description("link to self"),
//                                fieldWithPath("_links.query-events.href").description("link to query-event list"),
//                                fieldWithPath("_links.update-event.href").description("link to update an existing event")

                        )
                ))
        ;

    }

    @Test
    @TestDescription("입력받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
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


    @Test
    @TestDescription("입력 값이 비어있는 경우에 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 잘못된 경우에 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Developmetn with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2017, 11, 25, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .endEventDateTime(LocalDateTime.of(2017, 12, 23, 14, 21))
                .basePrice(1007)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2스타텁 팩토리")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
//                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
//                .andExpect(jsonPath("$[0].rejectedValue").exists())
        ;
    }

}
