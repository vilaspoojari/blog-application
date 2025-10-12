package com.mountblue.blog.blog_application.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {

        model.addAttribute("errorTitle", "Unexpected Error");
        model.addAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "An error occurred.");
        return "error";
    }

    @ExceptionHandler(NoDataFound.class)
    public String handleNoDataFoundException(NoDataFound ex, Model model) {
        model.addAttribute("errorTitle", "Data Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

}
