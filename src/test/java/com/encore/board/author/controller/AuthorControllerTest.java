package com.encore.board.author.controller;

//간단한 HTTP 객체만 만들거면 이 어노테이션
//Controller 계층을 테스트. 모든 스프링 빈을 생성하고 주입하지는 않음.
//@WebMvcTest(AuthorController.class)

//모든 어노테이션 생성 후, 사용할 때마다 주입받을거면 아래 두 개 어노테이션
//@SpringBootTest
//@AutoConfigureMockMvc
//public class AuthorControllerTest {
//    //가짜 User와 가짜 Service 만들어 Controller 로직을 테스트
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private AuthorService authorService;
//
//    @Test
//    //security 의존성 추가 필요
//    @WithMockUser(username = "test1", password = "test1111")
//    void authorDetailTest() throws Exception {
//        AuthorDetailResDto authorDetailResDto = new AuthorDetailResDto();
//        authorDetailResDto.setName("test1");
//        authorDetailResDto.setEmail("test1@example.com");
//        authorDetailResDto.setPassword("test1111");
//        //가짜 메서드 정의 및 가짜 객체를 넣어 호출
//        Mockito.when(authorService.findAuthorDetail(1L)).thenReturn(authorDetailResDto);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/author/1/circle/dto"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$.name", authorDetailResDto.getName()));
//    }
//}
