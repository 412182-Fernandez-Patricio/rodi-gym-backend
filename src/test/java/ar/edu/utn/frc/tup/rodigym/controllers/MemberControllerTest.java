package ar.edu.utn.frc.tup.rodigym.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.utn.frc.tup.rodigym.config.MappersConfig;
import ar.edu.utn.frc.tup.rodigym.dtos.MemberCreateDto;
import ar.edu.utn.frc.tup.rodigym.exceptions.MemberAlreadyExistsException;
import ar.edu.utn.frc.tup.rodigym.models.Member;
import ar.edu.utn.frc.tup.rodigym.services.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Spec 001: el contrato HTTP del alta. El frontend reconoce el DNI repetido por el
 * 409, así que ese código no puede volver a ser un 400.
 */
@WebMvcTest(MemberController.class)
@Import(MappersConfig.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @Test
    void shouldAnswerCreatedWithTheNewMember() throws Exception {
        Member member = new Member();
        member.setId(40123456L);
        member.setName("Laura");
        member.setLastName("Quiroga");
        member.setPhoneNumber("3515550199");
        member.setStatus(true);
        when(memberService.createMember(any())).thenReturn(member);

        create(body("40123456", "Laura", "Quiroga", "3515550199"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(40123456))
                .andExpect(jsonPath("$.last_name").value("Quiroga"))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.expiration_date").value(nullValue()));
    }

    @Test
    void shouldAnswerConflictForARepeatedId() throws Exception {
        when(memberService.createMember(any()))
                .thenThrow(new MemberAlreadyExistsException(12345678L));

        create(body("12345678", "Juan", "Perez", "1122334455"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void shouldRejectAnIdWithTooFewDigits() throws Exception {
        create(body("999999", "Laura", "Quiroga", "3515550199"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("id")));
        verify(memberService, never()).createMember(any(MemberCreateDto.class));
    }

    @Test
    void shouldRejectAnIdWithTooManyDigits() throws Exception {
        create(body("100000000", "Laura", "Quiroga", "3515550199"))
                .andExpect(status().isBadRequest());
        verify(memberService, never()).createMember(any(MemberCreateDto.class));
    }

    @Test
    void shouldAcceptAnIdWithSevenDigits() throws Exception {
        when(memberService.createMember(any())).thenReturn(new Member());

        create(body("1000000", "Laura", "Quiroga", "3515550199"))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldRejectFieldsThatAreOnlySpaces() throws Exception {
        create(body("40123456", "   ", "   ", "   "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("name")))
                .andExpect(jsonPath("$.message").value(containsString("lastName")))
                .andExpect(jsonPath("$.message").value(containsString("phoneNumber")));
        verify(memberService, never()).createMember(any(MemberCreateDto.class));
    }

    private ResultActions create(String json) throws Exception {
        return mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    private static String body(String id, String name, String lastName, String phone) {
        return """
                {"id": %s, "name": "%s", "last_name": "%s", "phone_number": "%s"}
                """.formatted(id, name, lastName, phone);
    }
}
