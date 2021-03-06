package bg.dalexiev.bgHistroryRss.data.rss

import android.net.Uri
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

private const val IMG_TAG_REGEX = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>"
private const val DATE_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z"

class RssParser {

    private val xmlParser: XmlPullParser;
    private val imgTagPattern: Pattern;
    private val dateFormat: SimpleDateFormat;

    init {
        xmlParser = XmlPullParserFactory.newInstance().apply {
            isNamespaceAware = true
        }.newPullParser()

        imgTagPattern = Pattern.compile(IMG_TAG_REGEX)

        dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
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
        var imageUrl = ""
        var description = ""
        var link = ""
        var publishDate = Calendar.getInstance()
        var creator = ""
        val categories = mutableListOf<String>()
        itemLoop@ while (true) {
            when (xmlParser.next()) {
                XmlPullParser.START_TAG ->
                    when (xmlParser.name) {
                        "guid" -> guid = parseGuid(xmlParser.nextText())
                        "title" -> title = xmlParser.nextText()
                        "description" -> {
                            parseDescription(xmlParser.nextText()).apply {
                                imageUrl = first
                                description = second
                            }
                        }
                        "link" -> link = xmlParser.nextText()
                        "pubDate" -> publishDate = parseDate(xmlParser.nextText())
                        "creator" -> creator = xmlParser.nextText()
                        "category" -> categories.add(xmlParser.nextText())
                    }
                XmlPullParser.END_TAG -> {
                    if ("item".equals(xmlParser.name)) {
                        break@itemLoop
                    }
                }
            }
        }
        return RssFeedItem(
            guid = guid,
            title = title,
            imageUrl = imageUrl,
            description = description,
            publishDate = publishDate,
            link = link,
            creator = creator,
            categories = categories
        )
    }

    private fun parseGuid(guidLink: String) = Uri.parse(guidLink).getQueryParameter("p")!!

    private fun parseDescription(rawDescription: String): Pair<String, String> {
        val matcher = imgTagPattern.matcher(rawDescription)
        return when {
            matcher.find() -> Pair(matcher.group(1), rawDescription.substring(matcher.end()))
            else -> Pair("", "")
        }
    }

    private fun parseDate(dateString: String)= dateFormat.parse(dateString).let { Calendar.getInstance().apply { time = it }}
}


