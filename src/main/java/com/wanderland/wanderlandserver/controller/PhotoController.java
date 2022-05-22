package com.wanderland.wanderlandserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PhotoController {
    @RequestMapping("/")
    @ResponseBody
    String home() { return "Stellt euch hier ein Photo vor"; }
}
