package com.riva.oauth.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Map<String, Object> errors = errorAttributes.getErrorAttributes(
                (WebRequest) request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE)
        );

        model.addAttribute("statusCode", errors.get("status"));
        model.addAttribute("exceptionMessage", errors.get("error"));
        model.addAttribute("message", errors.get("message"));

        return "error"; // renders error.html
    }

@GetMapping("/test-404")
    public String test404() {
        return "nonexistent-page"; // triggers 404
    }

    @GetMapping("/test-500")
    public String test500() {
        throw new RuntimeException("Cowboy test error!"); // triggers 500
    }

    @GetMapping("/missing-template")
    public String missingTemplate() {
        return "nope"; // template "nope.html" does not exist
    }

}
