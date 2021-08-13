package com.example.article
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import java.util.*

class RssParser {
    private val rssItems = ArrayList<Item>()
    private var rssItem : Item ?= null
    private var text: String? = null
    fun parse(inputStream: InputStream):List<Item> {
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType
            var foundItem = false
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagname = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> if (tagname.equals("item")) {
                        foundItem = true
                        rssItem = Item()
                    } else if (foundItem && tagname.equals("content")) {
                        rssItem!!.content = parser.getAttributeValue(0)
                    }
                    XmlPullParser.TEXT -> text = parser.text
                    XmlPullParser.END_TAG -> if (tagname.equals("item")) {
                            if(rssItem!=null)
                            rssItems.add(rssItem!!)
                        foundItem = false
                    } else if ( foundItem && tagname.equals("title")) {
                        rssItem!!.title = text.toString()
                    } else if (foundItem && tagname.equals("link")) {
                        rssItem!!.link = text.toString()
                    } else if (foundItem && tagname.equals("pubDate")) {
                        rssItem!!.pubDate = text.toString()
                    } else if (foundItem && tagname.equals("category")) {
                        rssItem!!.category = text.toString()
                    } else if (foundItem && tagname.equals("media:content")) {
                        rssItem!!.content = text.toString()
                    } else if (foundItem && tagname.equals("encoded")) {
                        rssItem!!.encoded = text.toString()
                    } else if (foundItem && tagname.equals("creator")) {
                        rssItem!!.creator = text.toString()
                    } else if (foundItem && tagname.equals("description")) {
                        rssItem!!.description = text.toString()
                    }
                }
                eventType = parser.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rssItems
    }
}