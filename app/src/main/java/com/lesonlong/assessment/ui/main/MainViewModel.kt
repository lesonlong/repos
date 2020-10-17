package com.lesonlong.assessment.ui.main

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lesonlong.assessment.repository.MovieRepository
import com.lesonlong.assessment.repository.MovieRepositoryImpl
import com.lesonlong.assessment.util.FileUtil
import com.lesonlong.assessment.util.ImageUtil
import com.lesonlong.assessment.vo.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val movieRepository: MovieRepository

    private var _currImageIndex = 0
    private val _movie = MutableLiveData<Movie>()
    private val _bitmap = MutableLiveData<Bitmap?>()
    private val _progress = MutableLiveData<String>()
    private val _hasError = MutableLiveData<Boolean>()


    val movie: LiveData<Movie>
        get() = _movie
    val bitmap: LiveData<Bitmap?>
        get() = _bitmap
    val progress: LiveData<String>
        get() = _progress
    val hasError: LiveData<Boolean>
        get() = _hasError

    init {
        movieRepository = MovieRepositoryImpl(app)
    }

    fun loadData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // download Movie from Repository
                val movie = movieRepository.getMovie()
                _movie.postValue(movie)

                // download and display first image
                val url = movie.image[_currImageIndex]
                FileUtil.downloadFile(getApplication(), url, _currImageIndex, _progress)
                    .let { file ->
                        if (file.exists()) {
                            // decode and display first image
                            _bitmap.postValue(
                                ImageUtil.decodeSampledBitmapFromFile(
                                    file,
                                    reqWidth,
                                    reqHeight
                                )
                            )
                            _hasError.postValue(false)
                        } else {
                            _hasError.postValue(true)
                        }
                    }
            }
        }
    }

    fun onImageClicked(increaseIndex: Boolean = true) {
        if (increaseIndex) {
            // calculate current image index
            _currImageIndex++
            if (_currImageIndex >= (_movie.value?.image?.size ?: 0)) {
                _currImageIndex = 0
            }
        }

        // move to next image
        _movie.value?.image?.get(_currImageIndex)?.let { url ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    // recycle current image
                    _bitmap.value?.recycle()
                    _bitmap.postValue(null)
                    FileUtil.downloadFile(getApplication(), url, _currImageIndex, _progress)
                        .let { file ->
                            if (file.exists()) {
                                // decode and display next image
                                _bitmap.postValue(
                                    ImageUtil.decodeSampledBitmapFromFile(
                                        file,
                                        reqWidth,
                                        reqHeight
                                    )
                                )
                                _hasError.postValue(false)
                            } else {
                                _hasError.postValue(true)
                            }
                        }
                }
            }
        }
    }

    fun onRetryClicked() {
        _hasError.value = false
        onImageClicked(false)
    }

    companion object {
        const val reqWidth = 540
        const val reqHeight = 960
    }
}