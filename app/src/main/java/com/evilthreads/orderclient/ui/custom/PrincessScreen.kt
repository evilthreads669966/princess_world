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
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.evilthreads.orderclient.model.data.Princess
import com.evilthreads.orderclient.model.data.SelectedPhoto
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.gson.Gson
import com.valentinilk.shimmer.shimmer
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
fun Profile(princess: Princess, controller: NavHostController){
    val urihandler = LocalUriHandler.current
    Column(Modifier.fillMaxSize()) {
        ProfileHeader(princess)
        Gallery(princess.name, princess.photos, controller)
    }
}

var isTablet = false

@Composable
fun ProfileHeader(princess: Princess){
    val loader = ImageLoader.Builder(LocalContext.current).components {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            add(ImageDecoderDecoder.Factory())
        else
            add(GifDecoder.Factory())
    }.build()
    val urihandler = LocalUriHandler.current
    val imageTabletModifier = Modifier.size(300.dp).padding(16.dp).clip(CircleShape).border(BorderStroke(8.dp, Color(com.evilthreads.orderclient.R.color.pink_light)), CircleShape)
    val imagePhoneModifier = Modifier.size(200.dp).padding(8.dp).clip(CircleShape).border(BorderStroke(4.dp, Color(com.evilthreads.orderclient.R.color.pink_light)), CircleShape)
    val buttonTabletModifier = Modifier.width(200.dp).height(100.dp).padding(16.dp).shimmer()
    val buttonPhoneModifier = Modifier.padding(8.dp).width(100.dp).height(50.dp).shimmer()

    Column(Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally){
        val visible = remember { mutableStateOf(true) }
        AnimatedVisibility(visible = visible.value, enter = fadeIn(initialAlpha = 0.4f), exit = fadeOut(animationSpec = tween(durationMillis = 250))) {
            AsyncImage(model = princess.gif, contentDescription = null, modifier = if(isTablet) imageTabletModifier else imagePhoneModifier, contentScale = ContentScale.Crop, imageLoader = loader)
        }
        Text(text = princess.caption , fontWeight = FontWeight.Bold, fontSize = if(isTablet) 24.sp else 18.sp, color = Color(com.evilthreads.orderclient.R.color.pink_light), modifier = Modifier.padding(4.dp))
        Row { Button(modifier = if(isTablet) buttonTabletModifier else buttonPhoneModifier, shape = RoundedCornerShape(12.dp), onClick = { urihandler.openUri("https://cash.app/${princess.cashapp}/100.0)") }) {
            Text(text = "Cashapp", fontSize = if(isTablet) 24.sp else 12.sp)
        }
            Button(modifier = if(isTablet) buttonTabletModifier else buttonPhoneModifier, shape = RoundedCornerShape(12.dp), onClick = { /*TODO*/ }) {
                Text(text = "Venmo", fontSize = if(isTablet) 24.sp else 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Gallery(name: String, photos: List<String>, controller: NavHostController){
    val imageTabletModifier = Modifier.width(750.dp).height(750.dp).padding(16.dp)
    val imagePhoneModifier = Modifier.width(350.dp).height(350.dp).padding(8.dp)
    LazyRow(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically){
        itemsIndexed(photos){ idx, photo ->
            Card(elevation = 8.dp, shape = RoundedCornerShape(4.dp), modifier = Modifier.padding(PaddingValues(4.dp))) {
                AsyncImage(model = photo, contentDescription = null, contentScale = ContentScale.Crop, modifier = (if(isTablet) imageTabletModifier else imagePhoneModifier)
                    .clickable {
                        val json = Uri.encode(Gson().toJson(SelectedPhoto(name, idx, photos)))
                        controller.navigate("photo/$json")
                    })
            }
        }
    }
}