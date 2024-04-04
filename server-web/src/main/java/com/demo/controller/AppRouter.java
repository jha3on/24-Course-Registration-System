package com.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class AppRouter {

    /**
     * 메인 페이지
     */
    @GetMapping(value = {"/"})
    public String userLoginPage() {
        return "/userLoginPage";
    }

    /**
     * 강의 목록 페이지
     */
    @GetMapping(value = {"/course"})
    public String coursePage() {
        return "/coursePage";
    }

    /**
     * 수강 신청 페이지
     */
    @GetMapping(value = {"/course/registration"})
    public String courseRegistrationPage() {
        return "/courseRegistrationPage";
    }

    /**
     * 예비 수강 신청 페이지
     */
    @GetMapping(value = {"/course/registration/cart"})
    public String courseRegistrationCartPage() {
        return "/courseRegistrationCartPage";
    }
}