package com.example.demo.service

import com.example.demo.entity.Lyric
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.PreparedStatement
import java.sql.SQLException

@Repository
class LyricDaoImpl : LyricDao {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    //指定歌手の曲情報を取得
    override fun getSongs(artistName: String?): List<Lyric> {
        // preparedStatementがSQLインジェクション対策になるので書き換えたいが、今回はこのまま。
//        val sql = "SELECT * FROM lyricTable WHERE artist='$artistName';"
        var resultList:List<Map<String,Any?>> = mutableListOf()
        var sql = StringBuilder()
        sql.append("SELECT * FROM lyricTable WHERE 1=1 ")
        if (artistName.isNullOrEmpty()) {
            resultList = jdbcTemplate.queryForList(sql.toString())
        } else {
            sql.append(" and artist = ?")
            resultList = jdbcTemplate.queryForList(sql.toString(), artistName)
        }

        val list: MutableList<Lyric> = ArrayList()
        for (result in resultList) {
            val lyric = Lyric()
            lyric.artist = result["artist"] as String?
            lyric.title = result["title"] as String?
            lyric.words_writer = result["words_writer"] as String?
            lyric.music_composer = result["music_composer"] as String?
            lyric.lyric = result["lyric"] as String?
            lyric.url = result["url"] as String?
            list.add(lyric)
        }
        return list
    }

    //リストの曲情報を複数INSERT。
    override fun insertSongs(list: List<Lyric>) {
//        val sql = "INSERT INTO lyricTable VALUES (?,?,?,?,?,?)"
        val sql = "INSERT INTO lyricTable (artist,title,words_writer,music_composer,lyric,url) VALUES (?,?,?,?,?,?)"
        //preparedstatement複数INSERT。テンプレとしてこのまま使った。これでもう動く。
        jdbcTemplate.batchUpdate(sql, object : BatchPreparedStatementSetter {
            @Throws(SQLException::class)
            override fun setValues(ps: PreparedStatement, i: Int) {
                val lyric = list[i]
                ps.setString(1, lyric.artist)
                ps.setString(2, lyric.title)
                ps.setString(3, lyric.words_writer)
                ps.setString(4, lyric.music_composer)
                ps.setString(5, lyric.lyric)
                ps.setString(6, lyric.url)
            }

            override fun getBatchSize(): Int {
                return list.size
            }
        })
    }
}