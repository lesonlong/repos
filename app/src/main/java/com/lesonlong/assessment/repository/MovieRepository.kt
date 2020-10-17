package com.lesonlong.assessment.repository

import com.lesonlong.assessment.vo.Movie

interface MovieRepository {

    fun getMovie(): Movie
}