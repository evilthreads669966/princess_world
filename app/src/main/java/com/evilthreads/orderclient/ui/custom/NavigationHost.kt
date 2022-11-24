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
package com.evilthreads.orderclient.ui.custom

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.evilthreads.orderclient.*
import com.evilthreads.orderclient.model.data.Princess
import com.evilthreads.orderclient.model.data.SelectedPhoto
import com.google.gson.Gson
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
@Composable
fun NavigationHost(controller: NavHostController, scaffoldState: ScaffoldState, viewModel: PrincessViewModel, titleState: MutableState<String>){
    val urihandler = LocalUriHandler.current
    val videos = viewModel.videos.observeAsState()
    val tweets = viewModel.tweets.observeAsState()
    val youtubeVideos = viewModel.youtubeVideos.observeAsState()
    NavHost(navController = controller, startDestination = "princesses"){
        composable("princesses"){
            PrincessList(princesses = viewModel.princesses){ princess ->
                val json = Uri.encode(Gson().toJson(princess))
                controller.navigate("princess/$json"){
                    popUpTo("princess/$json"){ inclusive = true}
                }
            }
            titleState.value = "Princesses"
        }
        composable("princess/{princess}", arguments = listOf(navArgument("princess"){ type = NavType.StringType })){
            val json = it.arguments?.getString("princess")
            val princess = Gson().fromJson(Uri.decode(json), Princess::class.java)
            Profile(princess!!, controller)
            LaunchedEffect(scaffoldState.snackbarHostState){
                val result = scaffoldState.snackbarHostState.showSnackbar("Pay me your tribute", "Tribute")
                if(result == SnackbarResult.ActionPerformed)
                    urihandler.openUri("https://cash.app/evilthreads/100.0")
            }
            titleState.value = princess.name
        }
        composable("photos"){
            Photos(viewModel.photos, controller)
            titleState.value = "Photos"
        }
        composable("videos"){
            viewModel.getVideos()
            VideoList(videos.value!!)
            titleState.value = "Videos"
        }
        composable("photo/{selectedPhoto}", arguments = listOf(navArgument("selectedPhoto"){ type = NavType.StringType })){
            val selectedPhoto = Gson().fromJson(Uri.decode(it.arguments?.getString("selectedPhoto")), SelectedPhoto::class.java)
            ViewPhoto(selectedPhoto)
            titleState.value = "${selectedPhoto.princessName} Photos"
        }
        composable("tweets"){
            val twitterIds = viewModel.princesses.map { it.twitter }.filterNotNull()
            viewModel.getTweets(twitterIds)
            Tweets(tweets.value!!)
            titleState.value = "Tweets"
        }
        composable("youtube"){
            viewModel.getYoutubeVideos()
            YoutubeVideos(youtubeVideos.value!!)
            titleState.value = "YouTube"
        }
    }
}