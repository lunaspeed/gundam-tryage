package org.lunary.source

import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}
import java.security.SecureRandom

import org.apache.commons.io.IOUtils
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.{CloseableHttpResponse, HttpPost}
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.message.BasicNameValuePair
import org.lunary.Models.AreaConfig
import org.lunary.sink.HtmlPersistSink

import collection.JavaConverters._
import scala.util.Try

trait HtmlSource {

  def loadHtml(category: String): Either[Throwable, String]
}

class FileHtmlSource(directory: Path, config: AreaConfig) extends HtmlSource {

  override def loadHtml(category: String): Either[Throwable, String] = {

    val filename = HtmlPersistSink.getHtmlFilename(category, config)

    val file = directory.resolve(filename)
    if (Files.isRegularFile(file)) {
      Try {
        new String(Files.readAllBytes(file), StandardCharsets.UTF_8)
      }.toEither

    } else {
      Left(new FileNotFoundException(s"${file.toString} does not exists or is not a regular file"))
    }

  }
}

class HttpClientHtmlSource(client: CloseableHttpClient, config: AreaConfig) extends HtmlSource {

  private val rand = new SecureRandom

  def loadHtml(category: String): Either[Throwable, String] = {

    val searchUrl = config.searchUrl
    var resp: CloseableHttpResponse = null
    val post = new HttpPost(searchUrl)

    val form = {
      //free=&rarelity=&battle_type=&card_type=&category_exp=168011&title_name=&btn_search.x=96&btn_search.y=44&btn_search=%E6%A4%9C%E7%B4%A2
      val params = List("free" -> "", "rarelity" -> "", "battle_type" -> "", "card_type" -> "",
        "category_exp" -> category, "title_name" -> "",
        "btn_search.x" -> s"${rand.nextInt(96) + 2}", "btn_search.y" -> s"${rand.nextInt(45) + 2}",
        "btn_searc" -> "検索").map {
        case (key, value) => new BasicNameValuePair(key, value)
      }
      new UrlEncodedFormEntity(params.asJava, "UTF-8")
    }

    post.setEntity(form)
    post.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
    post.addHeader("Host", config.domain)
    post.addHeader("Origin", config.urlBase)
    post.addHeader("Referer", searchUrl)
    post.addHeader("Upgrade-Insecure-Requests", "1")
    post.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Safari/537.36")

    val t = Try {
      val start = System.currentTimeMillis()
      resp = client.execute(post)

      val time = System.currentTimeMillis() - start

      println(s"query ${config.area.combinedSets(category)} spent $time ms")
      IOUtils.toString(resp.getEntity.getContent, "UTF-8")
    }

    resp.close()

    t.toEither

  }
}

