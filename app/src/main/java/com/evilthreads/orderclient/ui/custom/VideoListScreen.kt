package com.evilthreads.orderclient.ui.custom

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.evilthreads.orderclient.model.data.EPornerVideo
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
fun VideoList(videos: List<EPornerVideo>){
    val urihandler = LocalUriHandler.current
    val loader = ImageLoader.Builder(LocalContext.current).components {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            add(ImageDecoderDecoder.Factory())
        else
            add(GifDecoder.Factory())
    }.build()
    if(videos.isEmpty())
        Box(contentAlignment = Center){
            CircularProgressIndicator()
        }
    else
        LazyColumn(Modifier.fillMaxSize()){
            items(videos){ video ->
                VideoCard(video, loader){ video ->
                    urihandler.openUri(video.url)
                }
            }
        }
}

@Composable
fun VideoCard(video : EPornerVideo, loader: ImageLoader, onVideoClick: (EPornerVideo) -> Unit){
    val fontSizeTablet = 32.sp
    val fontSizePhone = 18.sp
    val imageModifierTablet = Modifier
        .size(300.dp)
        .padding(16.dp)
    val imageModifierPhone = Modifier
        .size(200.dp)
        .padding(8.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .height(if (isTablet) 450.dp else 220.dp)
            .clickable { onVideoClick(video) }, verticalAlignment = Alignment.CenterVertically){
        Column(
            Modifier
                .width(if (isTablet) 400.dp else 200.dp)
                .padding(horizontal = if (isTablet) 28.dp else 16.dp)) {
            Text(text = video.length_min, fontSize = if(isTablet) fontSizeTablet else fontSizePhone)
            Text(text = video.title, fontWeight = FontWeight.Bold, maxLines = 5, overflow = TextOverflow.Ellipsis, fontSize = if(isTablet) fontSizeTablet else fontSizePhone)
        }
        AsyncImage(model = video.default_thumb.src, contentDescription = null, imageLoader = loader, contentScale = ContentScale.Crop, modifier = if(isTablet) imageModifierTablet else imageModifierPhone)
    }
}

