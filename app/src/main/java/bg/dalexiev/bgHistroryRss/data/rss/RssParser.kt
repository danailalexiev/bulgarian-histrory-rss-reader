package bg.dalexiev.bgHistroryRss.data.rss

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.util.*

class RssParser {

    private val xmlParser: XmlPullParser;

    init {
        xmlParser = XmlPullParserFactory.newInstance().apply {
            isNamespaceAware = true
        }.newPullParser()
    }

    fun parseFeed(inputStream: InputStream): RssFeed {
        xmlParser.setInput(inputStream, null)
        return parseFeed(xmlParser)
    }

    private fun parseFeed(xmlParser: XmlPullParser): RssFeed {
        var title = ""
        var link = ""
        var description = ""
        val items = mutableListOf<RssFeedItem>()
        feedLoop@ while (true) {
            when (xmlParser.next()) {
                XmlPullParser.START_TAG ->
                    when (xmlParser.name) {
                        "title" -> title = xmlParser.nextText()
                        "link" -> link = xmlParser.nextText()
                        "description" -> description = xmlParser.nextText()
                        "item" -> items.add(parseItem(xmlParser))
                    }

                XmlPullParser.END_TAG -> {
                    if ("channel".equals(xmlParser.name)) {
                        break@feedLoop
                    }
                }
            }
        }

        return RssFeed(title, link, description, items)
    }

    private fun parseItem(xmlParser: XmlPullParser): RssFeedItem {
        var guid = ""
        var title = ""
        var description = ""
        var link = ""
        var publishDate = Calendar.getInstance()
        var creator = ""
        itemLoop@ while (true) {
            when (xmlParser.next()) {
                XmlPullParser.START_TAG ->
                    when (xmlParser.name) {
                        "guid" -> guid = xmlParser.nextText()
                        "title" -> title = xmlParser.nextText()
                        "description" -> description = xmlParser.nextText()
                        "link" -> link = xmlParser.nextText()
                        "pubDate" -> publishDate = Calendar.getInstance()
                        "dc:creator" -> creator = xmlParser.nextText()
                    }
                XmlPullParser.END_TAG -> {
                    if ("item".equals(xmlParser.name)) {
                        break@itemLoop
                    }
                }
            }
        }
        return RssFeedItem(guid = guid, title = title, description = description, publishDate = publishDate, link = link, creator = creator)
    }
}