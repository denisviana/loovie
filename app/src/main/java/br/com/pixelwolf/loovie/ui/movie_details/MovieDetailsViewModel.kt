package br.com.pixelwolf.loovie.ui.movie_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pixelwolf.loovie.api.response.ErrorClass
import br.com.pixelwolf.loovie.api.util.ApiConst.LANG
import br.com.pixelwolf.loovie.model.Movie
import br.com.pixelwolf.loovie.repository.IMovieRepository
import br.com.pixelwolf.loovie.ui.movie_details.MovieDetailsViewModel.MoviesState.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response

class MovieDetailsViewModel(
    val movieRepository: IMovieRepository,
    val converter : Converter<ResponseBody, ErrorClass>
) : ViewModel(){


    val state = MutableLiveData<MoviesState>().apply {
        value = Loading
    }

    fun getMovieDetails(id : Int){

        viewModelScope.launch {

            val response : Response<Movie> = try{
                movieRepository.getMovieById(
                    movieId = id,
                    language = LANG)
            }catch (e : Exception){
                e.printStackTrace()
                state.postValue(Error("Erro Inesperado"))
                return@launch
            }

            if(response.isSuccessful && response.body() != null){
                state.postValue(Success(response.body()!!))
            }else{
                val error = converter.convert(response.errorBody()!!)
                state.postValue(Error(error?.statusMessage!!))
            }

        }

    }

    sealed class MoviesState {
        object Loading : MoviesState()
        data class Error(val message : String) : MoviesState()
        data class Success(val movie: Movie) : MoviesState()
    }

}