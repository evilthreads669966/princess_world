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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.rememberNavController
import com.evilthreads.orderclient.PrincessViewModel
import com.evilthreads.orderclient.R
import kotlinx.coroutines.launch
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
fun AppScaffold(viewmodel: PrincessViewModel){
    val state = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val controller = rememberNavController()

    val titleState = remember { mutableStateOf("Princess World") }
    val navMenutItem = listOf(
        NavMenutItem("princesses","Princesses", ImageVector.vectorResource(if(isTablet) R.drawable.princess_48 else R.drawable.princess_32)),
        NavMenutItem("photos", "Photos", ImageVector.vectorResource(if(isTablet) R.drawable.photo_gallery_48 else R.drawable.photo_gallery_32)),
        NavMenutItem("videos", "Videos", ImageVector.vectorResource(if(isTablet) R.drawable.video_gallery_48 else R.drawable.video_gallery_32)),
        NavMenutItem("tweets", "Tweets", ImageVector.vectorResource(if(isTablet) R.drawable.twitter_48 else R.drawable.twitter_32)),
        NavMenutItem("youtube", "Youtube", ImageVector.vectorResource(if (isTablet) R.drawable.youtube_48 else R.drawable.youtube_32))
    )
    Scaffold(
        scaffoldState = state,
        topBar = {
            AppBar(controller,titleState.value) {
                if(controller.previousBackStackEntry == null)
                    scope.launch {
                        state.drawerState.open()
                    }
                else
                    controller.popBackStack()
            }
        }/*,
                floatingActionButton = {
                    FloatingActionButton(backgroundColor = Color(R.color.pink_dark), contentColor = Color.White, onClick = { urlhandler.openUri("mailto:princesscindi2@yahoo.com") }) {
                        Icon(Icons.Default.Email, contentDescription = null)
                    }
                }*/,
        drawerContent = {
            DrawerHeader()

            DrawerBody(navMenutItem){ item ->
                scope.launch {
                    controller.navigate(item.id){
                        popUpTo(item.id){ inclusive = true}
                    }
                    state.drawerState.close()
                }
            }
        },
        bottomBar = {
            BottomBar(controller = controller, items = navMenutItem)
        },
        isFloatingActionButtonDocked = true
    ) {
        Surface(
            Modifier
                .fillMaxSize()
                .padding(it)) {
            NavigationHost(controller =controller, state, viewmodel, titleState)
        }
    }
}