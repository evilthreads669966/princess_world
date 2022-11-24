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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.evilthreads.orderclient.model.data.FeedPhoto
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
fun Photos(photos: List<FeedPhoto>, controller: NavHostController){
    val tabletModifier = Modifier.height(800.dp).width(720.dp)
    val phoneModifier = Modifier.requiredWidth(400.dp).requiredHeight(360.dp).padding(8.dp)

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally){
        items(photos){ photo ->
            Card(
                Modifier
                    .padding(8.dp).fillMaxSize()
                    .clickable {
                        val princess = Uri.encode(Gson().toJson(photo.princess))
                        controller.navigate("princess/$princess")
                    }, shape = RoundedCornerShape(8.dp), elevation = 8.dp){
                AsyncImage(model = photo.uri, contentDescription = null, modifier = if(isTablet) tabletModifier else phoneModifier, contentScale = ContentScale.Fit)
            }
        }
    }
}