package com.mysite.sbb.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 6, max = 20)
    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    private String username;

    @Size(min = 10)
    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{10,16}",
            message = "비밀번호는 10~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;

    @NotEmpty(message = "휴대폰 번호는 필수항목입니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$",
            message = "휴대폰 번호는 숫자만 입력하세요.")
    private String cellphone;
}
