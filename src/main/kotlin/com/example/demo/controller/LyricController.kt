package com.example.demo.controller

import com.example.demo.entity.Lyric
import com.example.demo.form.InsertForm
import com.example.demo.form.SearchForm
import com.example.demo.service.LyricService
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
class LyricController {

    @Autowired
    private lateinit var lyricService: LyricService

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
        val list = lyricService.getSongs(searchForm.artistName)
        if (list.size != 0) {
            model.addAttribute("lyricList", list)
        } else {
            model.addAttribute("noResultMessage", "該当アーティストの曲が登録されていない")
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
        val list = convertInsertForm(insertForm)
        // TDDO エラーがあった場合の処理を書くこと
        lyricService.insertSongs(list)
        //@GetMapping("/form")にリダイレクト。フラッシュスコープ設定。
        redirectAttributes.addFlashAttribute("insertCompleteMessage", "insert complete")
        return "redirect:form"
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

    companion object {
        //InseartFormからLyricへ変換
        fun convertInsertForm(insertForm: InsertForm): List<Lyric> {
            val list: MutableList<Lyric> = ArrayList()
            val lyric = Lyric()
            lyric.artist = insertForm.artistName
            lyric.title = insertForm.title
            lyric.words_writer = insertForm.wordsWriter
            lyric.music_composer = insertForm.musicComposer
            lyric.lyric = insertForm.lyric
            lyric.url = insertForm.url
            list.add(lyric)
            return list
        }
    }
}