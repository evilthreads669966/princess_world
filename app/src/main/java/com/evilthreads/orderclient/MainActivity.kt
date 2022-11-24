package com.evilthreads.orderclient
/*Copyright 2022 Chris Basinger

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evilthreads.orderclient.model.data.EPornerVideo
import com.evilthreads.orderclient.model.data.FeedPhoto
import com.evilthreads.orderclient.model.data.Princess
import com.evilthreads.orderclient.model.data.VideoResponse
import com.evilthreads.orderclient.networking.*
import com.evilthreads.orderclient.ui.custom.AppScaffold
import com.evilthreads.orderclient.ui.custom.isTablet
import com.evilthreads.orderclient.ui.theme.OrderClientTheme
import com.google.gson.annotations.Expose
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/*
            (   (                ) (             (     (
            )\ ))\ )    *   ) ( /( )\ )     (    )\ )  )\ )
 (   (   ( (()/(()/(  ` )  /( )\()|()/((    )\  (()/( (()/(
 )\  )\  )\ /(_))(_))  ( )(_)|(_)\ /(_))\((((_)( /(_)) /(_))
((_)((_)((_|_))(_))   (_(_()) _((_|_))((_))\ _ )(_))_ (_))
| __\ \ / /|_ _| |    |_   _|| || | _ \ __(_)_\(_)   \/ __|
| _| \ V /  | || |__    | |  | __ |   / _| / _ \ | |) \__ \
|___| \_/  |___|____|   |_|  |_||_|_|_\___/_/ \_\|___/|___/
....................../´¯/)
....................,/¯../
.................../..../
............./´¯/'...'/´¯¯`·¸
........../'/.../..../......./¨¯\
........('(...´...´.... ¯~/'...')
.........\.................'...../
..........''...\.......... _.·´
............\..............(
..............\.............\...
*/
/**
 * @author Chris Basinger
 * @email evilthreads669966@gmail.com
 * @date 11/17/22
 **/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val config = LocalConfiguration.current
            if(config.screenWidthDp > 600) isTablet = true
            val viewmodel by viewModels<PrincessViewModel>()
            OrderClientTheme {
                AppScaffold(viewmodel = viewmodel)
            }
        }
    }
}

//900 411
class PrincessViewModel: ViewModel(){
    val princesses = listOf(beverly,cindi,mandy,jessica,bella,christina,skylar).sortedBy { it.name.split(" ")[1] }
    val photos = princesses.flatMap { princess -> princess.photos.map { FeedPhoto(princess, it) } }.shuffled()
    private val _videos = MutableLiveData<List<EPornerVideo>>(emptyList())
    val videos: MutableLiveData<List<EPornerVideo>>
        get() = _videos
    private val _tweets = MutableLiveData<List<Tweet>>(emptyList())
    val tweets: MutableLiveData<List<Tweet>>
        get() = _tweets
    private val _youtubeVideos = MutableLiveData("")
    val youtubeVideos: MutableLiveData<String>
        get() = _youtubeVideos

    private val pornService = PornService.create()
    private val youtubeService = YoutubeService.create()
    private val tweetService = TwitterService.create()
    val usernameCache = hashMapOf<Long,String>()
    val tweetCache = hashMapOf<Long,List<Tweet>>()

    fun getYoutubeVideos(){
        viewModelScope.launch {
            val videos = youtubeService.seachVideos(part = "snippet", "findom").body()
            if(videos != null)
                _youtubeVideos.value = videos
        }
    }

    fun getVideos(){
        pornService.getVideos().enqueue(object: Callback<VideoResponse>{
            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                val videos = response.body()!!.videos.filter { it.title.lowercase().contains("mistress") }
                if(videos != null)
                    _videos.value = videos
            }

            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                _videos.value = emptyList()
            }
        })
    }

    fun getTweets(userIDs: List<Long>){
        val tweets = mutableListOf<Tweet>()
        viewModelScope.launch {
            userIDs.forEach{ id ->
                if(usernameCache[id] == null)
                    usernameCache[id] = tweetService.getUsername(id).body()?.user?.username ?: return@launch
                if(tweetCache[id] == null)
                    tweetCache[id] = tweetService.getTweets(id).body()?.tweets.apply { this!!.forEach { it.username = usernameCache[id]!! } } ?: return@launch
                tweetCache.values.forEach { tweets.addAll(it) }
            }
            _tweets.value = tweets
        }
    }
}

data class Tweet(@Expose val id: Long, @Expose val text: String, @Expose val url: String, var username: String = "")

val beverly = Princess(
    name = "Princess Beverly",
    caption = "Your job is to serve.",
    gif = "https://fans.iwantclips.com/uploads/contents/videos/8469/0880733001476732328_81daaf232076ca0356e9cb1f1d9ef74c.gif",
    iwantclips = "https://iwantclips.com/store/8469/Princess-Beverly",
    twitter = 3313671737,
    cashapp = "evilthreads",
    email = "princessbeverly@yahoo.com",
    venmo = "",
    clips4sale = "https://www.clips4sale.com/studio/32364/american-mean-girls/Cat0-AllCategories/Page1/SortBy-bestmatch/Limit24/search/princess%20beverly",
    photos = listOf("https://footstockings.com/contents/videos_screenshots/6000/6251/preview.jpg","https://static-cache.k2s.cc/thumbnail/05fb4b40f508a/main/0.jpeg")
)

val cindi = Princess(
    name = "Princess Cindi",
    caption = "I'm going to turn you into my slave",
    gif = "https://fans.iwantclips.com/uploads/contents/videos/8470/0110217001440107929_c2624021e7760f9898afe80385063471.gif",
    photos = listOf("https://fans.iwantclips.com/uploads/contents/videos/8470/0110217001440107929_c2624021e7760f9898afe80385063471.gif","https://imagecdn.clips4sale.com/accounts99/32364/clip_images/previewlg_12505765.jpg"),
    email = "princesscindi2@yahoo.com",
    cashapp = "evilthreads",
    venmo = "",
    iwantclips = "https://iwantclips.com/store/8470/Princess-Cindi",
    clips4sale = "https://www.clips4sale.com/studio/32364/american-mean-girls/Cat0-AllCategories/Page1/SortBy-bestmatch/Limit24/search/cindi",
    twitter = 1304876400311111680,
)
val jessica = Princess(
    name = "Goddess Jessica",
    caption = "The divine goddess",
    iwantclips = "https://iwantjessica.com/",
    clips4sale = "https://www.clips4sale.com/studio/55619/divine-goddess-jessica",
    gif = "https://www.humiliationpov.com/blog/wp-content/uploads/cumclothes.gif",
    cashapp = "evilthreads",
    venmo = "",
    twitter = 3456625696,
    email = "goddessjessica@yahoo.com",
    photos = listOf("https://pbs.twimg.com/profile_images/1060640005742186496/0p7DU7Of_400x400.jpg","https://pornovideoshub.com/wp-content/uploads/2020/01/Goddess_Jessica_-_Do_Something_For_Me.mp4.00002.jpg")
)
val mandy = Princess(
    name = "Mandy Marx",
    gif = "https://www.wykop.pl/cdn/c3201142/comment_1638748181pZMPETj7rs3dhgRoJxzuDy.jpg",
    iwantclips = "https://iwantclips.com/store/201210/Mandy-Marx",
    clips4sale = "https://www.clips4sale.com/studio/120911/mandy-marx",
    cashapp = "evilthreads",
    twitter = null,
    email ="mandymarx@gmail.com",
    caption = "Crawl to me",
    venmo = "",
    photos = listOf("https://fans.iwantclips.com/uploads/aboutme_previews/201210/480_cc38c931ff66ab84371b39cb80f25e33.jpg","https://www.wykop.pl/cdn/c3201142/comment_1638748181pZMPETj7rs3dhgRoJxzuDy.jpg")
)
val skylar = Princess(
    name = "Princess Skylar",
    gif = "https://fans.iwantclips.com/uploads/aboutme_previews/685717/480_bd8c75b60213cd2afd56b5b3b2d7d804.jpg",
    iwantclips = "https://iwantclips.com/store/685717/ServeSkylar",
    clips4sale = "https://www.clips4sale.com/studio/139161/serve-skylar",
    twitter = 1168929753237094400,
    email = "",
    caption = "Serve Skylar",
    venmo = "",
    photos = listOf("https://joi-me.com/uploads/posts/2019-01/thumbs/1548147427_tmg-smh_mp4_00007.jpg","https://cdn.shopify.com/s/files/1/0631/5678/1284/files/IMG_3518.jpg?v=1656608366"),
    cashapp = "serveskylar"
)
val bella = Princess(
    name = "Princess Bella",
    gif = "https://www.bratprincess.us/sites/default/files/pictures/Clip_Images_6/Bella%20-%20Sits%20on%20her%20step%20brothers%20back%20and%20smokes%20%286%29.jpg",
    iwantclips = null,
    clips4sale = "https://www.clips4sale.com/studio/21233/brat-princess-2/Cat0-AllCategories/Page1/SortBy-bestmatch/Limit10/search/bella",
    venmo = "",
    cashapp = "evilthreads",
    caption = "Good pay piggy",
    twitter = null,
    photos = listOf("https://www.bratprincess.us/sites/default/files/pictures/Clip_Images_4/Bella%20-%20You%20are%20a%20Cuck%20Not%20a%20Boyfriend.jpg"),
    email = ""
)
val christina = Princess(
    name = "Princess Christina",
    gif = "https://www.bratprincess.us/sites/default/files/pictures/Clip_Images_5/Christina%20-%20The%20pathetic%20ugly%20nerd%20practices%20cock%20sucking%20%282%29.jpg",
    iwantclips = null,
    clips4sale = "https://www.clips4sale.com/studio/21233/brat-princess-2/Cat0-AllCategories/Page1/SortBy-bestmatch/Limit10/search/christina",
    cashapp = "evilthreads",
    venmo = "",
    caption = "Bow to your princess",
    twitter = null,
    email = "",
    photos = listOf("https://i.pinimg.com/550x/bb/50/06/bb500665d82a74ada703ac46a1cb9b1a.jpg","https://i2.wp.com/www.bratprincess.us/sites/default/files/carousel/BP%20GhP%202012%20%28134%29.jpg")
)
/*

@Composable
fun VideoPlayer(video: String){
    val ctx = LocalContext.current
    val exo = ExoPlayer.Builder(ctx).build()
    val dataSourceFactory = DefaultDataSource.Factory(ctx)
    val source = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(video))
    exo.addMediaSource(source)
    exo.prepare()
    AndroidView(factory = {
        StyledPlayerView(it).apply {
            player = exo
        }
    }, modifier = Modifier
        .fillMaxWidth()
        .height(300.dp))
}*/

