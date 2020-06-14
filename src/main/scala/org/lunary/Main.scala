package org.lunary

import java.io.File

import cats.data.NonEmptyList
import cats.effect.{ExitCode, IO, IOApp}
import com.typesafe.config.ConfigFactory
import org.lunary.Models._
import cats.implicits._

object Main extends IOApp {

//  type Result = Either[NonEmptyList[Throwable], Unit]

  def run(arg: List[String]): IO[ExitCode] = {

    val config = ConfigFactory.load()
    val folder = new File(config.getString("fileDirectory"))
    val clientResource = Job.createHttpClient()

    val area = Japan
    val areaConfig = AreaConfig(config.getConfig(s"gundam.${area.configName}"), area)

    val res = area.setGroups.map {
      case (groupSymbol, sets) =>
        val file = new File(folder, s"${areaConfig.filePrefix}-gundam-tryage-$groupSymbol.xlsx")
        val excel = new Excel(config, areaConfig)
        excel.generate(file, sets, clientResource)
    }
    val result: IO[List[Either[Throwable, Unit]]] = res.sequence

    val accumulatedResults: IO[Either[NonEmptyList[Throwable], Unit]] = result.map(_.foldLeft[Either[NonEmptyList[Throwable], Unit]](Right(Unit)) { (r, e) =>
      (r, e) match {
        case (Left(es), Left(e)) => Left(es :+ e)
        case (l @ Left(_), _) => l
        case (_, Left(e)) => Left(NonEmptyList.one(e))
        case (a, _) => a
      }
    })


    accumulatedResults.map { results =>
      results match {
        case Right(_) => ExitCode.Success
        case Left(es) =>
          //print errors if any
          es.toList.zipWithIndex.foreach {
            case (e, i) =>
              println(s"error: ${i + 1}: ")
              e.printStackTrace()
          }
          ExitCode.Error
      }
    }
  }
}
