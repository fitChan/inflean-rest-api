package com.gisunlecture.infleanrestapi.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest  // MockMvc bean 을 자동 설정함. 따라서 그냥 가져와서 쓰자.. "웹" 관련 빈만 등록해줌.

public class EventControllerTests {

    @Autowired
    //스프링 MVC 테스트 핵심 클래스 이며 웹서버를 띄우지 않고도 스프링MVC(DispatcherServlet)가
    // 요청을 처리하는 과정을 확인 가능하기에 컨트롤러에서 많이 사용됌.
    MockMvc mockMvc;
    //단위 태스트라기에는 너무 많은 것들이 들어와있음..
    //dispatcherservlet을 만들어야 하기때문에 웹 서버를 띄우는 테스트보단 빠르지만 단위테스트보단 느림.

    //이제 본격적으로 테스트를 만들거임.
    @Test
    public void createEvent() throws Exception {  //perform Exception 처리라는데 Excaption 박아버리네..?
        mockMvc.perform(post("/api/events") //post Method를 사용한다.
                      .contentType(MediaType.APPLICATION_JSON)
                      .accept(MediaTypes.HAL_JSON)
                       )
                .andExpect(status().is(201)); //isCreated == is(201) 같은 내용
    }


}
