package ru.kkrukhmalev.vkapi.posts

import org.testng.Assert
import org.testng.annotations.Test

class VkPostsResponseParserTest {
    private val parser = VkPostsResponseParser()

    @Test
    fun parseResponse() {
        val posts = parser.parseResponse("""
            {
                "response": {
                "items": [{
                    "id": 4885,
                    "date": 1633874402,
                    "owner_id": -100889999,
                    "from_id": -100889999,
                    "post_type": "post",
                    "text": "Новости с полей карточных битв! &#127183; 
        
                    Прямо сейчас наш состав по Hearthstone принимает участие в [https://vk.com/wall-199652750_142|турнире от КМА]. Сегодня в борьбу вступает состав по классическим карточным разборкам, а откидавшиеся вчера бгшеры (которые, между прочим, метят на призовые места), продолжат борьбу на следующих выходных. 
                    Также они сегодня примут участие уже в другом турнире от Московского киберспорта (https://vk.cc/c6CJdF)
                    Конкуренция велика, а умение наших игроков делать правильные ходы - еще выше! &#128520; 
        
                    #ITMO #ИТМО #kronbars #kb_esports #hearthstone #hearthstone@kb_esports",
                    "marked_as_ads": 0,
                    "attachments": []
                }, {
                    "id": 4884,
                    "date": 1633867118,
                    "owner_id": -100889999,
                    "from_id": -100889999,
                    "post_type": "post",
                    "text": "Герольд Бездны успел смениться Нашором, а ты так и не зарегистрировался? &#128543; 
        
                    Специально для тех, кто предпочитает стоять в кустах и не высовываться (Тимо мейны, это про вас) лишний раз, мы продлеваем регистрацию в сборную по League of Legends до 15 октября!
                    Ждём только тебя: https://vk.cc/c6n281 
        
                    #ITMO #ИТМО #kronbars #kb_esports #LoL #LoL@kb_esports",
                    "marked_as_ads": 0,
                    "attachments": []
                }],
                    "next_from": "2/-100889999_4884",
                    "count": 1000,
                    "total_count": 31150
                }
            }
        """.trimIndent())

        Assert.assertEquals(posts.size, 2)
        Assert.assertEquals(
            posts[0],
            VkPost(id = 4885, owner_id = -100889999, unixTime = 1633874402, text = """
                Новости с полей карточных битв! &#127183; 

                        Прямо сейчас наш состав по Hearthstone принимает участие в [https://vk.com/wall-199652750_142|турнире от КМА]. Сегодня в борьбу вступает состав по классическим карточным разборкам, а откидавшиеся вчера бгшеры (которые, между прочим, метят на призовые места), продолжат борьбу на следующих выходных. 
                        Также они сегодня примут участие уже в другом турнире от Московского киберспорта (https://vk.cc/c6CJdF)
                        Конкуренция велика, а умение наших игроков делать правильные ходы - еще выше! &#128520; 
                
                        #ITMO #ИТМО #kronbars #kb_esports #hearthstone #hearthstone@kb_esports""".trimIndent())
        )
        Assert.assertEquals(
            posts[1],
            VkPost(id = 4884, owner_id = -100889999, unixTime = 1633867118, text = """
                Герольд Бездны успел смениться Нашором, а ты так и не зарегистрировался? &#128543; 

                        Специально для тех, кто предпочитает стоять в кустах и не высовываться (Тимо мейны, это про вас) лишний раз, мы продлеваем регистрацию в сборную по League of Legends до 15 октября!
                        Ждём только тебя: https://vk.cc/c6n281 
                
                        #ITMO #ИТМО #kronbars #kb_esports #LoL #LoL@kb_esports""".trimIndent())
        )
    }

    @Test
    fun parseEmptyResponse() {
        val posts = parser.parseResponse("""
            {
                "response": {
                "items": [],
                "count": 0,
                "total_count": 0
                }
            }
        """.trimIndent())
        Assert.assertTrue(posts.isEmpty())
    }

    @Test
    fun parseBrokenResponse() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            parser.parseResponse("""
            {
                "response": {
                "items": [],
            }
        """.trimIndent())
        }
    }
}