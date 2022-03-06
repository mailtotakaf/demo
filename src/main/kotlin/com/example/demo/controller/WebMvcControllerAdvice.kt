package com.example.demo.controller

import com.example.demo.form.InsertForm
import com.example.demo.form.SearchForm
//import org.postgresql.util.PSQLException
import org.springframework.beans.propertyeditors.StringTrimmerEditor
import org.springframework.ui.Model
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.InitBinder
import java.sql.SQLException


@ControllerAdvice
class WebMvcControllerAdvice {
    //htmlから送信された空文字をnullとして扱うらしい
    @InitBinder
    fun initBinder(dataBinder: WebDataBinder) {
        dataBinder.registerCustomEditor(String::class.java, StringTrimmerEditor(true))
    }

    //DBエラーが起こった場合にはエラーメッセージと、空のFormオブジェクトをmodelに入れてhtml返却
    @ExceptionHandler(SQLException::class)
    fun sqlException(e: SQLException, model: Model): String {
        model.addAttribute("errorMessage", e.message)
        model.addAttribute("searchForm", SearchForm())
        model.addAttribute("insertForm", InsertForm())
        return "form.html"
    }
}