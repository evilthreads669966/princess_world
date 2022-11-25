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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun DrawerHeader(){
    Column(modifier = Modifier.fillMaxWidth().height(if (isTablet) 400.dp else 300.dp).background(Color(com.evilthreads.orderclient.R.color.purple_700)),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
        Text("Princess World", color = Color.White, fontSize = if(isTablet) 74.sp else 36.sp)
        Icon(imageVector = ImageVector.vectorResource(id = com.evilthreads.orderclient.R.drawable.venus_crown_256), contentDescription = null, tint = Color.White)
    }
}

@Composable
fun DrawerBody(items: List<NavMenutItem>, onItemClick: (NavMenutItem) -> Unit){
    LazyColumn{
        items(items){ item ->
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable { onItemClick(item) }, verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = item.icon, contentDescription = null)
                Spacer(modifier = Modifier.width(if(isTablet) 32.dp else 16.dp))
                Text(item.title, fontSize = if(isTablet) 28.sp else 18.sp)
            }
        }
    }
}

data class NavMenutItem(val id: String, val title: String, val icon: ImageVector)