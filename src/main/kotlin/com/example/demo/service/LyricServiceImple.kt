package com.example.demo.service

import com.example.demo.entity.Lyric
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class LyricServiceImpl : LyricService { //実装クラスのインスタンスがDIされる

//    @Autowired
//    lateinit var dao: LyricService

//    @Autowired
//    override fun LyricServiceImpl(dao: LyricDao) {
//        this.dao = dao
//    }
    @Autowired
    lateinit var dao: LyricDaoImpl

    override fun getSongs(artistName: String?): List<Lyric> {
        return dao.getSongs(artistName)
    }

    override fun insertSongs(list: List<Lyric>) {
        dao.insertSongs(list)
    }
}
