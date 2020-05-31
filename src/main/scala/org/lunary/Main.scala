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
    val result = res.sequence

    result.map(_ => ExitCode.Success)


//    val result: Result = area.setGroups.par.map {
//        case (groupSymbol, sets) =>
//          val file = new File(folder, s"${areaConfig.filePrefix}-gundam-tryage-$groupSymbol.xlsx")
//          val excel = new Excel(config, areaConfig)
//          excel.generate(file, sets, client)
//      }.foldLeft[Result](Right(Unit)) { (r, e) =>
//
//        (r, e) match {
//          case (Left(es), Left(e)) => Left(es ++ List(e))
//          case (l @ Left(_), _) => l
//          case (_, Left(e)) => Left(NonEmptyList.of(e))
//          case (a, _) => a
//        }
//      }

    //print errors if any
//    for {
//      errors <- result.left
//      error <- errors.toList.zipWithIndex
//    } {
//      println(s"error: ${error._2 + 1}: ")
//      error._1.printStackTrace()
//    }
  }
}
