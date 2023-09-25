package com.example.MyToss.controller;

import com.example.MyToss.Type.ErrorCode;
import com.example.MyToss.domain.Account;
import com.example.MyToss.Type.AccountStatus;
import com.example.MyToss.dto.CreateAccount;
import com.example.MyToss.dto.DeleteAccount;
import com.example.MyToss.exception.AccountException;
import com.example.MyToss.service.AccountService;
import com.example.MyToss.service.LockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.MyToss.dto.AccountDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
// MockBean 어노테이션을 이용해서 테스트할때에는
// @WebMvcTest라는 어노테이션을 클래스명 앞에 붙인다. 대소문자 주의!
class AccountControllerTest {
    @MockBean
    // 이 annotation들은 해당 가짜bean을 AccountController에 자동으로 주입시킴.
    private AccountService accountService;

    @MockBean
    private LockService lockService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void failGetAccount() throws Exception {
        // given
        given(accountService.getAccount(anyLong()))
                .willThrow(new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
        // when
        // then
        mockMvc.perform(get("/account/876"))
                .andDo(print())
                .andExpect(jsonPath("$.errorCode").value("ACCOUNT_NOT_FOUND"))
                .andExpect(jsonPath("$.errorMessage").value("계좌가 없습니다."))
                .andExpect(status().isOk());
    }

    @Test
    void successGetAccountByUserId() throws Exception{
        // given
        List<AccountDto> accountDtos =
                Arrays.asList(
                        AccountDto.builder()
                                .accountNumber("12345678")
                                .balance(1000L).build(),
                        AccountDto.builder()
                                .accountNumber("12341234")
                                .balance(2000L).build(),
                        AccountDto.builder()
                                .accountNumber("12345555")
                                .balance(5000L).build());

        given(accountService.getAccountsByUserId(anyLong()))
                .willReturn(accountDtos);
        // when
        // then
        mockMvc.perform(get("/get-account?user_id=1"))
                .andDo(print())
                .andExpect(jsonPath("$[0].accountNumber").value("12345678"))
                .andExpect(jsonPath("$[0].balance").value("1000"));

    }

    @Test
    void successDeleteAccount() throws Exception {
        // given
        given(accountService.deleteAccount(anyLong(), anyString()))
                .willReturn(AccountDto.builder()
                        .userId(1L)
                        .accountNumber("12345678")
                        .registeredAt(LocalDateTime.now())
                        .unRegisteredAt(LocalDateTime.now())
                        .build());
        // when

        // then
        mockMvc.perform(post("/accountDelete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new DeleteAccount.Request(999L, "99998888")
                        // 아무값이나 넣어줘도 됨. 단, DeleteAccount의 양식에 맞춰서
                        // ex) 계좌번호는 8자리만 가능
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.accountNumber").value("12345678"))
                .andDo(print());
    }


    @Test
    void successCreateAccount() throws Exception {
        // given
        given(accountService.createAccount(anyLong(), anyLong()))
                .willReturn(AccountDto.builder()
                        .userId(1L)
                        .accountNumber("123456789")
                        .registeredAt(LocalDateTime.now())
                        .unRegisteredAt(LocalDateTime.now())
                        .build());
        // when
        // then
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new CreateAccount.Request(1L, 1111L) // 아무값이나 넣어줘도 됨
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.accountNumber").value("123456789"))
                .andDo(print());
    }



    @Test
    void successGetAccount() throws Exception {
        // # given
        given(accountService.getAccount(anyLong()))
                .willReturn(Account.builder()
                        .accountNumber("3456")
                        .accountStatus(AccountStatus.IN_USE)
                        .build());

        // # when

        // # then
        mockMvc.perform(get("/account/99999"))
                .andDo(print())
                .andExpect(jsonPath("$.accountNumber").value("3456"))
                .andExpect(jsonPath("$.accountStatus").value("UNREGISTERED")) // 실패
                .andExpect(status().isOk());
        // @Mock, @InjectMocks라는 annotation을 사용한 테스트에 있는
        // assertEquals()와 같은 역할을 한다.
    }

}