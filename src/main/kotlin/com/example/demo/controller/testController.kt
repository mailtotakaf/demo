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
@RequestMapping("/lyric")
class testController {

    @Autowired
    private lateinit var repository: testRepository

    //modelのキーに"insertCompleteMessage"があればそのまま使用、なければadd
    @GetMapping("/form")
    fun form(model: Model, @ModelAttribute("insertCompleteMessage") message: String?): String {
        model.addAttribute("title", "artist name")
        //templateフォルダ内の該当ファイルを返す
        return "form.html"
    }

    //SearchFormクラスのvalidation anotationに引っかかるとresultにエラーが入る
    @GetMapping("/select")
    fun select(@Validated searchForm: SearchForm, result: BindingResult, model: Model): String {
        if (result.hasErrors()) {
            model.addAttribute("title", "Error Page")
            return "form.html"
        }
        val list = repository.getPosts(searchForm.artistName)
        if (list.size != 0) {
            model.addAttribute("postList", list)
        } else {
            model.addAttribute("noResultMessage", "登録はありません。")
        }
        return "list.html"
    }

    //PRGのためにリダイレクトとフラッシュスコープ(リクエスト1回終わると消える属性)を使用
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
        }
        // まずは入力フォームは一つ。DAOはリスト形式のため、要素が1つのリストにする
        val post = convertInsertForm(insertForm)
        repository.insertSongs(post)
        //@GetMapping("/form")にリダイレクト。フラッシュスコープ設定。
        redirectAttributes.addFlashAttribute("insertCompleteMessage", "insert complete")
        return "redirect:select"
    }

    //上記の引数にSearchFormが入ってなくても勝手にmodelに入れてくれる。メソッド名は何でもいい。
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