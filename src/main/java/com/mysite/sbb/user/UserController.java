package com.mysite.sbb.user;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(),
                    userCreateForm.getPassword1(), userCreateForm.getCellphone());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/withdrawal")
    public String withdrawal(UserCreateForm userCreateForm) {
        return "withdrawal_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/withdrawal")
    public String withdrawal(UserCreateForm userCreateForm, BindingResult bindingResult,
            Principal principal) {
        SiteUserDto siteUserDto = this.userService.getUser(principal.getName());
        if (!this.userService.checkPassword(siteUserDto.getUsername(),
                userCreateForm.getPassword1())) {
            bindingResult.rejectValue("password1", "passwordInCorrect", "패스워드가 일치하지 않습니다.");
            return "withdrawal_form";
        } else {
            this.userService.delete(siteUserDto);
        }
        return "redirect:/user/logout";
    }

    @GetMapping("/findid")
    public String findid(UserCreateForm userCreateForm) {
        return "findid_form";
    }

    @PostMapping("/findidresult")
    public String findid(Model model, UserCreateForm userCreateForm, BindingResult bindingResult) {
        SiteUserDto siteUserDto = null;
        try {
            if (userCreateForm.getEmail() != null) {
                siteUserDto = this.userService.getEmail(userCreateForm.getEmail());
            } else {
                siteUserDto = this.userService.getCellphone(userCreateForm.getCellphone());
            }
        } catch (DataNotFoundException e) {
            bindingResult.reject("noID", "ID를 찾을 수 없습니다.");
            return "findid_form";
        }
        userCreateForm.setUsername(siteUserDto.getUsername());
        model.addAttribute("siteuser", siteUserDto);
        return "findid_result";
    }
}
