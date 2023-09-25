package com.example.MyToss.service;

import com.example.MyToss.domain.Account;
import com.example.MyToss.Type.AccountStatus;
import com.example.MyToss.domain.AccountUser;
import com.example.MyToss.dto.AccountDto;
import com.example.MyToss.repository.AccountRepository;
import com.example.MyToss.repository.AccountUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
// 이 클래스는 Mockito 라이브러리로 테스트되고 있음을 명시.
class AccountServiceTest {

    @Mock
    // 객체 accountRepository를 가짜로 생성
    private AccountRepository accountRepository;

    @Mock
    private AccountUserRepository accountUserRepository;

    @InjectMocks
    // 위에서 가짜로 만든 객체 2개를 accountService에 적용
    private AccountService accountService;

    @Test
    void createAccountSuccess() {
    }



//    @Test
//    @DisplayName("계좌 조회 성공 테스트")
//    void testSomthing() {
//        // # given
//        given(accountRepository.findById(anyLong()))
//                .willReturn(Optional.of(Account.builder()
//                                .accountStatus(AccountStatus.UNREGISTERED)
//                                .accountNumber("123456789")
//                                .build()));
//        // * given부분 코드의 각 부분들
//        // given() : 여기서 리턴한 변수를 가지고 테스트를 돌릴 것이다.
//        // 주의! 이때 given메서드 불러올때 mockito 라이브러리 것으로 가져와야함!!
//        // accountRepository.findById(anyLong()) : 아무 것이나 찾으면(??)
//        // willReturn() : 다음과 같은 변수를 리턴한다.
//        // Optional.of() : ???
//        // Account.builder().~~~.build() : ~~~에 따라서 Account타입 객체 생성
//        // .accountStatus().accountNumber() : Account타입 객체의 속성값 세팅. (위의 ~~~에 해당)
//        //
//        // 원래 accountService 객체의 메서드로 만들수 있는 Account타입 객체는
//        // 계좌번호가 40000, 사용 여부가 IN_USE인 계좌 뿐이었다.
//        // 그러나 @Mock, @InjectMock라는 annotation을 통해
//        // AccountService 클래스를 내가 원하는 세팅으로 통제할 수 있다.
//        // 덕분에 변인이 통제된 상태에서 코드 테스트를 진행할 수 있다.
//
//        // # when
//        Account account = accountService.getAccount(9999L);
//        // AccountService 클래스가 통제되었으므로
//        // accountService 객체는 위에서 세팅된 대로 고정되어있다.
//        // 따라서 getAccount메서드의 전달인자로 아무 값을 넣어줘도 상관 없다.
//        // 즉, 여기서 account 객체는 Mocking으로 제어된 클래스로부터 생성된 객체이다.
//        System.out.println(account.getAccountNumber());
//        System.out.println(account.getAccountStatus());
//
//        // # then
//        assertEquals("123456789", account.getAccountNumber());
//        assertEquals(AccountStatus.UNREGISTERED, account.getAccountStatus());
//        // 모두 문제없이 통과된다.
//    }

}