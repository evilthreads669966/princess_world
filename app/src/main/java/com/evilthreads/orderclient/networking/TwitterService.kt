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
package com.evilthreads.orderclient.networking

import com.evilthreads.orderclient.Tweet
import com.google.gson.annotations.SerializedName
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
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
interface TwitterService{
    companion object{
        private val BASE_URL = "https://api.twitter.com/2/"
        val BEARER_TOKEN = "AAAAAAAAAAAAAAAAAAAAAIzujAEAAAAAeBCHYtCNwS8OCKp8CBuLoCM%2BrKs%3DYr92Twc8yqvChzBDQQ1ueEqypBbm5P3KRFcYcgZ3RLxuzvVOVO"
        fun create(): TwitterService{
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().authenticator(TokenAuthenticator()).addInterceptor(interceptor).build()
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create()).build()
            return retrofit.create(TwitterService::class.java)
        }
    }

    @GET("users/{userID}/tweets")
    suspend fun getTweets(@Path("userID") userID: Long): retrofit2.Response<TwitterResponse>

    @GET("users/{id}")
    suspend fun getUsername(@Path("id") id: Long): retrofit2.Response<TwitterUserResponse>
}


class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.header("Authorization") != null) {
            return null
        }
        return response.request.newBuilder().header("Authorization", "Bearer " + TwitterService.BEARER_TOKEN).build()
    }
}

data class TwitterResponse(@SerializedName("data") val tweets: List<Tweet>)

data class TwitterUserResponse(@SerializedName("data") val user: TwitterUser)

data class TwitterUser(val username: String)