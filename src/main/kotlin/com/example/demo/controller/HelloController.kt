package com.example.demo.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class HelloController {
    @RequestMapping(value = ["/hello/{msg}"], method = [RequestMethod.POST])
    fun hello(@PathVariable msg: String, model: Model): String {
        model.addAttribute("msg", msg)
        //テンプレート名を指定
        return "user/hello"
    }
}