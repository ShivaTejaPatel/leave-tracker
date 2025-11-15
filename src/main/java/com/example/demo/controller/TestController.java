package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping(value = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String hello() {
        return "Hello, Swagger!";
    }

    @GetMapping(value = "/areYouInReadyState", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String areYouInReadyState() {
        long processId = ProcessHandle.current().pid();
        return String.valueOf(processId);
    }
}