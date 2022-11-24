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

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.evilthreads.orderclient.model.data.Princess

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
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PrincessList(princesses: List<Princess>, onNavigate: (Princess) -> Unit){
    val tabletImageModifier = Modifier.size(150.dp).clip(CircleShape).padding(start = 8.dp)
    val phoneImageModifier = Modifier.size(100.dp).clip(CircleShape).padding(start = 4.dp)
    LazyColumn(Modifier.testTag("princess_list")){
        items(princesses){ princess ->
            Card(modifier = Modifier.padding(4.dp).fillMaxSize().testTag("princess_card"), elevation = 8.dp, shape = RoundedCornerShape(16.dp), onClick = { onNavigate(princess) }) {
                Row (Modifier.wrapContentHeight(), verticalAlignment = Alignment.CenterVertically){
                    AsyncImage(model = princess.gif, contentDescription = null, modifier = if(isTablet) tabletImageModifier else phoneImageModifier, contentScale = ContentScale.Crop)
                    Spacer(modifier = if(isTablet) Modifier.width(48.dp) else Modifier.width(18.dp))
                    Text(text = princess.name, fontSize = if(isTablet) 28.sp else 24.sp, color = Color(com.evilthreads.orderclient.R.color.pink_dark))
                }
            }
        }
    }
}