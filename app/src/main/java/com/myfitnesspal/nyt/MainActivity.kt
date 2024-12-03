package com.myfitnesspal.nyt

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var uiState : Boolean = false//: MainViewModelState(savedInstanceState)
    lateinit var articles: MutableStateFlow<List<ArticleData>> // = MutableStateFlow<List<ArticleData>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val articleViewModel: ArticleViewModel by viewModels() //MainViewModel(savedInstanceState)
        //var uiState : Boolean = false //: MainViewModelState(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO){
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                articleViewModel.isLoading.collect {
                    uiState = it
                    println("Collecting it:-UISTATE $it")
                }
            }
        }

        //1a
        val articlesList = MutableLiveData<List<ArticleData>>() //1a
        val articlesList2 = MutableLiveData<List<ArticleData>>() //1c
        //2a
        articles = MutableStateFlow<List<ArticleData>>(emptyList()) //2a
        val articles2 = MutableStateFlow<List<ArticleData>>(emptyList()) //2c
        //3a
        val repoArticles = MutableStateFlow<List<ArticleData>>(emptyList())
        val repoArticles2 = MutableStateFlow<List<ArticleData>>(emptyList())
        //4a
        val repoArticlesList = MutableStateFlow<List<ArticleData>>(emptyList())
        val repoArticlesList2 = MutableStateFlow<List<ArticleData>>(emptyList())


        setContentView(R.layout.activity_main)
        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            MaterialTheme {
                ArticleScreen(articlesList, uiState, { articleData: ArticleData ->
                    refresh(articleViewModel)
                   // val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(articleData.url))
                    //startActivity(browserIntent)
                })
            }
        }


        //setContent{
        // MaterialTheme {
        // ArticleScreen(uiState)
        // }
        //}


        /*val articleList = findViewById<RecyclerView>(R.id.article_list)
        val articleAdapter = ArticleAdapter()
        articleList.adapter = articleAdapter
        articleList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        articleAdapter.itemClickListener = object : ArticleAdapter.ItemClickListener {
            override fun onClicked(articleData: ArticleData) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(articleData.url))
                startActivity(browserIntent)
            }
        }*/



        /////////articleViewModel.fetchArticles()

        //1a
       /* articleViewModel.articleList.observe(this) {
            //articleAdapter.articleList = it
            articlesList2.value = it
        }

        //2a
        lifecycleScope.launch(Dispatchers.IO) {
            articleViewModel.articles.collect {
                articles2.value = it
            }
        }

        //3a
        lifecycleScope.launch(Dispatchers.IO) {
            articleViewModel.repoArticles.collect {
                repoArticles2.value = it
            }
        }

        //4a
        lifecycleScope.launch(Dispatchers.IO) {
            articleViewModel.repoArticleList.collect {
                repoArticlesList2.value = it
            }
        }*/

        //1c
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    //1c
                    articleViewModel.articleList.observe(this@MainActivity) {
                        articlesList.value = it
                        println("Observing it:-LaunchMain1c $it")
                    }
                }
                launch { //2c
                    articleViewModel.articles.collect {
                        articles.value = it
                        println("Collecting it:-LaunchMain2c $it")
                    }
                }

                launch { //3c
                    articleViewModel.repoArticles.collect {
                        repoArticles.value = it
                        println("Collecting it:-LaunchMain3c $it")
                    }
                }

                launch { //4c
                    articleViewModel.repoArticleList.collect {
                        repoArticlesList.value = it
                        println("Collecting it:-LaunchMain4c $it")
                    }
                }
            }
        }

    }

    fun refresh(articleViewModel: ArticleViewModel){
        println("refresh")
        lifecycleScope.launch(Dispatchers.IO){
                articleViewModel.articles.collect {
                    articles.value = it
                    println("refreshed: $it ")
                }
        }
    }
}

@Composable
fun ArticleScreen(articleList: LiveData<List<ArticleData>>? = null,
                  isLoading: Boolean,
                  onClicked: (ArticleData) -> Unit,
                  viewModel : ArticleViewModel = hiltViewModel())
{

    fun onClickItem(data: ArticleData, context: Context) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.url))
        context.startActivity(browserIntent)
    }

    val context = LocalContext.current

    ///
    //Data flows
    //1b
    val articlesList: MutableStateFlow<List<ArticleData>> = MutableStateFlow(emptyList())
    val articleList2 = articleList //Explicit Parameter
    val articleListVm = viewModel.articleList.value //ViewModel LiveData
    val articleListFlow = viewModel.articleListFlow.collectAsStateWithLifecycle(emptyList()) //as a Flow
    //2b
    val articles = viewModel.articles.collectAsStateWithLifecycle()
    val articles2: MutableStateFlow<List<ArticleData>> = MutableStateFlow(emptyList())
    //3b
    val repoArticles = viewModel.repoArticles.collectAsStateWithLifecycle()
    //4b
    val repoArticleList = viewModel.repoArticleList.collectAsStateWithLifecycle()
    ///

    LaunchedEffect(true){
        viewModel.articleList.observe(context as ComponentActivity) {
            articles2.value = it
            println("Observing it:-Compose $it")
        }
    }
    LaunchedEffect(true){
        viewModel.articles.collect {
            articles2.value = it
            println("Collecting it:-Compose $it")
        }
    }

    //Total //with loading state
    val articleState = viewModel.articleState.collectAsStateWithLifecycle(emptyList())


    //Loading state flows
    val loading = isLoading //explicit
    val loading2 = viewModel.isLoading.collectAsStateWithLifecycle(false) //implicit





Column(modifier = Modifier.fillMaxSize()) {

   /* Image(
        painter = rememberAsyncImagePainter(),
        contentDescription = period.shortForecast,
        modifier = Modifier.size(64.dp)
    )*/

    when (loading) {
        true -> {
            Text(text = "Loading..true. ${loading2.value}")
        }

        false -> {

            Button(onClick = {
                onClicked(ArticleData("title", "url"))
                println("loading ahain...")
            }) {
                Text(text = "Load Data")
            }
        }
    }
    when (loading2.value) {
        true -> {
            Text(text = "Loading2")
        }

        false -> {

            Button(onClick = {
                println("loading2 ahain...")
            }) {
                Text(text = "Not Loading2..false.${loading}")
            }
        }
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(articles.value) { article ->
            ListItem(data = article, onClick = { article -> onClickItem(article, context) })
        }
    }
}
}


@Composable
fun ListItem(data: ArticleData, onClick: (ArticleData) -> Unit) {
    Text(text = data.title, modifier = Modifier.clickable { onClick(data) })
}

@Preview
@Composable
fun ListItemPreview(){
    ListItem(ArticleData("title", "url"), onClick = {  })
}
