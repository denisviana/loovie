package br.com.pixelwolf.loovie.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.pixelwolf.loovie.api.response.MoviesResponse
import br.com.pixelwolf.loovie.model.Movie
import br.com.pixelwolf.loovie.repository.IMovieRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class MainViewModel(
    val mainRepository: IMovieRepository
) : ViewModel(){

    private val LANG = "pt-BR"
    private val REGION = "BR"
    private var page = 0

    val state = MutableLiveData<MoviesState>()

    fun getUpComingMovies(){

        page += 1

        viewModelScope.launch{

            val response : Response<MoviesResponse> = try{
                mainRepository.getUpcomingMovies(apiKey = "1f54bd990f1cdfb230adb312546d765d", language = LANG, region = "BR", page = page)
            }catch (e : Exception){
                e.printStackTrace()
                state.postValue(MoviesState.Error("Error"))
                return@launch
            }

            if(response.isSuccessful){
                if(response.body()?.result?.isEmpty()!!)
                    state.postValue(MoviesState.Empty)
                else
                    state.postValue(MoviesState.Success(response.body()?.result!!))
            }else
                state.postValue(MoviesState.Error("Error"))
        }

    }

    fun searchMovies(query : String){

        state.value = MoviesState.Loading

        viewModelScope.launch {

            val response : Response<MoviesResponse> = try{
                mainRepository.searchMovies(api_key = "1f54bd990f1cdfb230adb312546d765d", lang = LANG, region = REGION, query = query)
            }catch (e : Exception){
                e.printStackTrace()
                state.postValue(MoviesState.Error("Error"))
                return@launch
            }

            if(response.isSuccessful){
                if(response.body()?.result?.isEmpty()!!)
                    state.postValue(MoviesState.Empty)
                else
                    state.postValue(MoviesState.SearchSuccess(response.body()?.result!!))
            }else
                state.postValue(MoviesState.Error("Error"))

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
