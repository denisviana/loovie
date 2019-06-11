package br.com.pixelwolf.loovie.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pixelwolf.loovie.api.response.ErrorClass
import br.com.pixelwolf.loovie.api.response.MoviesResponse
import br.com.pixelwolf.loovie.api.util.ApiConst.LANG
import br.com.pixelwolf.loovie.api.util.ApiConst.REGION
import br.com.pixelwolf.loovie.model.Movie
import br.com.pixelwolf.loovie.repository.IMovieRepository
import br.com.pixelwolf.loovie.ui.movie_details.MovieDetailsViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import java.lang.Exception

class MainViewModel(
    val mainRepository: IMovieRepository,
    val converter : Converter<ResponseBody, ErrorClass>
) : ViewModel(){

    private var page = 0

    val state = MutableLiveData<MoviesState>()

    fun getUpComingMovies(){

        page += 1

        viewModelScope.launch{

            val response : Response<MoviesResponse> = try{
                mainRepository.getUpcomingMovies(language = LANG, region = REGION, page = page)
            }catch (e : Exception){
                e.printStackTrace()
                state.postValue(MoviesState.Error("Unexpected error, please try later"))
                return@launch
            }

            if(response.isSuccessful){
                if(response.body()?.result?.isEmpty()!!)
                    state.postValue(MoviesState.Empty)
                else
                    state.postValue(MoviesState.Success(response.body()?.result!!))
            }else{
                val error = converter.convert(response.errorBody()!!)
                state.postValue(MoviesState.Error(error?.statusMessage!!))
            }
        }

    }

    fun searchMovies(query : String){

        state.value = MoviesState.Loading

        viewModelScope.launch {

            val response : Response<MoviesResponse> = try{
                mainRepository.searchMovies(lang = LANG, region = REGION, query = query)
            }catch (e : Exception){
                e.printStackTrace()
                state.postValue(MoviesState.Error("Unexpected error, please try later"))
                return@launch
            }

            if(response.isSuccessful){
                if(response.body()?.result?.isEmpty()!!)
                    state.postValue(MoviesState.Empty)
                else
                    state.postValue(MoviesState.SearchSuccess(response.body()?.result!!))
            }else{
                val error = converter.convert(response.errorBody()!!)
                state.postValue(MoviesState.Error(error?.statusMessage!!))
            }

        }

    }

    fun resetSearch(){
        page = 0
        state.value = MoviesState.ResetSearch
        state.value = MoviesState.Loading
    }

    sealed class MoviesState {
        object Loading : MoviesState()
        object Empty : MoviesState()
        object ResetSearch : MoviesState()
        data class Error(val message : String) : MoviesState()
        data class Success(val movies: List<Movie>) : MoviesState()
        data class SearchSuccess(val movies : List<Movie>) : MoviesState()
    }

}
