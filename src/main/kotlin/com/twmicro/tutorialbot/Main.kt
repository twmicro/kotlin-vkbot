package com.twmicro.tutorialbot

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.exceptions.ApiException
import com.vk.api.sdk.exceptions.ClientException
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.objects.messages.Message
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery
import java.util.*

val random = Random()
val vk = VkApiClient(HttpTransportClient.getInstance())
val actor = GroupActor(AuthData.id, AuthData.accessToken)
var ts: Int = vk.messages().getLongPollServer(actor).execute().ts
var maxMsgId = 0

fun main() {
    println("Starting bot...")
    while(true) {
        try {
            val message = getMessage()
            if(message != null){
                if(!ControlPanel.executeCommand(message.text, message.peerId)) vk.messages().send(actor).message("Неизвестная команда!").peerId(message.peerId).randomId(random.nextInt(999999)).execute()
            }
        }
        catch(e: Exception) {
            e.printStackTrace()
        }
        Thread.sleep(300)
    }
}

@Throws(ApiException::class, ClientException::class)
fun getMessage(): Message? {
    val eventsQuery: MessagesGetLongPollHistoryQuery = vk.messages()
        .getLongPollHistory(actor)
        .ts(ts)
    if (maxMsgId > 0) {
        eventsQuery.maxMsgId(maxMsgId)
    }
    val messages: List<Message> = eventsQuery
        .execute()
        .messages.items
    if (messages.isNotEmpty()) {
        try {
            ts = vk.messages()
                .getLongPollServer(actor)
                .execute()
                .ts
        } catch (e: ClientException) {
            e.printStackTrace()
        }
    }
    if (messages.isNotEmpty() && !messages[0].isOut) {
        val messageId: Int = messages[0].id
        if (messageId > maxMsgId) {
            maxMsgId = messageId
        }
        return messages[0]
    }
    return null
}