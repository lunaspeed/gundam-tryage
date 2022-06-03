package org.lunary

import cats.effect.{IO, Resource}
import com.typesafe.config.Config
import org.http4s.client.Client
import org.lunary.Models.AreaConfig

import java.nio.charset.Charset
import java.nio.file.{Files, OpenOption, Paths, StandardOpenOption}
import scala.collection.immutable.ListMap
import scala.util.Try

class Html(config: Config, areaConfig: AreaConfig) {

  def saveGroup(groupSymbol: String, sets: ListMap[String, String], clientResource: Resource[IO, Client[IO]]): List[IO[Either[Throwable, Unit]]] = {
    sets.toList.map {
      case (category, categoryName) =>
        save(groupSymbol, category, categoryName, clientResource)
    }
  }

  def save(groupSymbol: String, category: String, categoryName: String, clientResource: Resource[IO, Client[IO]]): IO[Either[Throwable, Unit]] = {

    val job = new Job(areaConfig)
    clientResource.use { client =>

      job.request(category, client) flatMap { html =>
        val filePath = config.getString("saveDirectory")
        val file = Paths.get(filePath, s"${groupSymbol}-${category}.html")
        Resource.fromAutoCloseable(IO.delay(Files.newBufferedWriter(file, Charset.forName("UTF-8"), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))).use { w =>
          IO.delay(Try(w.write(html)).toEither)
        }
      }
    }

  }
}
