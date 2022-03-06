package com.example.demo.service

import com.example.demo.entity.Lyric

interface LyricDao {

    fun getSongs(artistName: String?): List<Lyric>

    fun insertSongs(list: List<Lyric>)
}