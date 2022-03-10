package com.example.demo.controller

import com.example.demo.entity.Post
import com.example.demo.form.CustomerForm
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
import java.util.*


@Controller
@RequestMapping("/sample")
class testController {

    @Autowired
    private lateinit var repository: testRepository

    @GetMapping("/form")
    fun form(
        model: Model,
        insertForm: InsertForm
    ): String {
        model.addAttribute("insertForm", insertForm)

        //jsS
        var syainList = mutableListOf<Syain>()
        var s1 = Syain()
        s1.id = 1
        s1.name = "fuc"
        syainList.add(s1)

        var s2 = Syain()
        s1.id = 2
        s1.name = "mank"
        syainList.add(s2)
//        var syainList = Arrays.asList(s1,s2)
        model.addAttribute("syain", syainList)
        //jsE

        model.addAttribute("title", "artist name")
        return "form.html"
    }

    @GetMapping("/select")
    fun select(model: Model): String {
        val list = repository.getPosts("")
        if (list.size != 0) {
            model.addAttribute("postList", list)
        } else {
            model.addAttribute("noResultMessage", "登録はありません。")
        }
        model.addAttribute("artistName", "")
        return "list.html"
    }

    /**
     * 新規登録
     */
    @PostMapping("/insert")
    fun insert(
        @ModelAttribute @Validated insertForm: InsertForm,
        result: BindingResult,
        model: Model,
//        redirectAttributes: RedirectAttributes
    ): String {
        if (result.hasErrors()) {
//            model.addAttribute("title", "Error Page")
            return "form.html"
        }
//        if (insertForm.title.isNullOrEmpty()) {
//            model.addAttribute("message", "※入力必須項目")
//            return "form.html"
//        }
//        //jsS
//        var s1 = Syain()
//        s1.id = 1
//        s1.name = "fuc"
//        var s2 = Syain()
//        s1.id = 2
//        s1.name = "mank"
//        model.addAttribute("syain", Arrays.asList(s1,s2))
//        //jsE

        val post = convertInsertForm(insertForm)
        repository.insertPost(post)

        return "redirect:select"
    }

//    @ModelAttribute
//    fun setSearchForm(): SearchForm {
//        return SearchForm()
//    }
//
//    @ModelAttribute
//    fun setInsertForm(): InsertForm {
//        return InsertForm()
//    }

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

class Syain {
    var id: Int = 0
    var name: String = ""
}