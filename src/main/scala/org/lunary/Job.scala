package org.lunary

import java.security.SecureRandom

import cats.effect.{ContextShift, IO, Resource}
import org.http4s.{Header, Headers, Method, Request, Uri, UrlForm}
import org.lunary.Models.AreaConfig

import org.http4s.client.blaze._
import org.http4s.client._

import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._

class Job(config: AreaConfig) {

  private val rand = new SecureRandom

  def request(category: String, client: Client[IO]): IO[String] = {

    val searchUrl = config.searchUrl
    val form = UrlForm("free" -> "", "rarelity" -> "", "battle_type" -> "", "card_type" -> "",
      "category_exp" -> category, "title_name" -> "",
      "btn_search.x" -> s"${rand.nextInt(96) + 2}", "btn_search.y" -> s"${rand.nextInt(45) + 2}",
      "btn_searc" -> "検索")
    val headers = Header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8") ::
      Header("Host", config.domain) ::
      Header("Origin", config.urlBase) ::
      Header("Referer", searchUrl) ::
      Header("Upgrade-Insecure-Requests", "1") ::
      Header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36") :: Nil

    IO.fromEither(Uri.fromString(searchUrl)).flatMap { uri =>
      val req = Request[IO](method = Method.POST, uri = uri, headers = Headers(headers)).withEntity(form)
      client.expect[String](req)
    }
  }

}

object Job {

  import cats.effect.Blocker
  import java.util.concurrent._

  val blockingPool = Executors.newFixedThreadPool(5)
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  val blocker = Blocker.liftExecutorService(blockingPool)

  def createHttpClient(): Resource[IO, Client[IO]] = {
    val blazeBuilder: BlazeClientBuilder[IO] = BlazeClientBuilder[IO](global)
      .withMaxTotalConnections(4)
      .withIdleTimeout(1 minute)

    blazeBuilder.resource
  }

}
