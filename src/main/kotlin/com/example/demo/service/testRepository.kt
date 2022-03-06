package com.example.demo.service

import com.example.demo.entity.Post
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import java.sql.SQLException

@Repository
class testRepository {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    /**
     * 一覧表示（検索）
     * @param serchStr: String?
     * @return list<Post>
     */
    fun getPosts(serchStr: String?): List<Post> {
        var resultList:List<Map<String,Any?>> = mutableListOf()
        var sql = StringBuilder()
        sql.append("SELECT * FROM post WHERE 1=1 ")
        try {
            if (serchStr.isNullOrEmpty()) {
                resultList = jdbcTemplate.queryForList(sql.toString())
            } else {
                sql.append(" and ( title LIKE '%'||?||'%' or text_area LIKE '%'||?||'%') ")

                resultList = jdbcTemplate.queryForList(sql.toString(), serchStr, serchStr)
            }
        } catch (e:Exception){
            e.printStackTrace()
        }

        val list: MutableList<Post> = ArrayList()
        for (result in resultList) {
            val post = Post()
            post.title = result["title"] as String?
            post.textArea = result["text_area"] as String?
            post.mail = result["mail"] as String?
            list.add(post)
        }
        return list
    }

    /**
     * 新規登録
     */
    fun insertSongs(post:Post) {
        val sql = "INSERT INTO post (title,text_area,mail) VALUES (?,?,?)"
        try {
            jdbcTemplate.update(sql, post.title, post.textArea, post.mail)
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }
}