package com.example.demo.controller

import com.example.demo.entity.Post
import com.example.demo.form.InsertForm
import com.example.demo.form.SearchForm
import com.example.demo.service.testRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@Controller
@RequestMapping("/sample")
class testController {

    @Autowired
    private lateinit var repository: testRepository

    @GetMapping("/form")
    fun form(model: Model, @ModelAttribute("insertCompleteMessage") message: String?): String {
        model.addAttribute("title", "artist name")
        return "form.html"
    }

    @GetMapping("/select")
//    fun select(@Validated searchForm: SearchForm, result: BindingResult, model: Model): String {
    fun select(model: Model): String {
//        if (result.hasErrors()) {
//            model.addAttribute("title", "Error Page")
//            return "form.html"
//        }
//        val list = repository.getPosts(searchForm.artistName)
        val list = repository.getPosts("")
        if (list.size != 0) {
            model.addAttribute("postList", list)
        } else {
            model.addAttribute("noResultMessage", "登録はありません。")
        }
        return "list.html"
    }

    /**
     * 新規登録
     * insertFormクラスのvalidation anotationに引っかかるとresultにエラーが入る
     */
    @PostMapping("/insert")
    fun insert(
        @Validated insertForm: InsertForm,
        result: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String {
        if (result.hasErrors()) {
            model.addAttribute("title", "Error Page")
            return "form.html"
        } else if (insertForm.title.isNullOrEmpty()){
            model.addAttribute("message", "※入力必須項目")
            return "form.html"
        }

        val post = convertInsertForm(insertForm)
        repository.insertPost(post)
        //フラッシュスコープ設定、リダイレクト(PRG)
//        redirectAttributes.addFlashAttribute("insertCompleteMessage", "insert complete")
        return "redirect:select"
    }

    @ModelAttribute
    fun setSearchForm(): SearchForm {
        return SearchForm()
    }

    @ModelAttribute
    fun setInsertForm(): InsertForm {
        return InsertForm()
    }

    /**
     * インサートFormをPostに詰める
     * @param InsertForm
     * @return Post
     */
    companion object {
        fun convertInsertForm(insertForm: InsertForm): Post {
            val post = Post()
            post.title = insertForm.title
            post.textArea = insertForm.textArea
            post.mail = insertForm.mail
            return post
        }
    }
}