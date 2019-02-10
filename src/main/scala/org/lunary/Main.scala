package org.lunary

import java.nio.file.Paths

import com.typesafe.config.ConfigFactory
import org.apache.logging.log4j.scala.Logging
import org.lunary.Main.args2AreaSets
import org.lunary.Models._
import org.lunary.sink._
import org.lunary.source.{HtmlSource, HttpClientHtmlSource}
import slick.jdbc.MySQLProfile.api._

import scala.collection.immutable.ListMap

object Main extends Logging {

  def args2AreaSets(args: Array[String]): List[Area] = {
    if(args.isEmpty) {
      List(Japan, Asia)
    }
    else {
      args.map { s =>
        if (s.toLowerCase.startsWith("j")) {
          Japan
        }
        else {
          Asia
        }
      }.toList
    }
  }

  def main(args: Array[String]): Unit = {

    val areaSets = args2AreaSets(args)

    val config = ConfigFactory.load()
    val directory = Paths.get(config.getString("fileDirectory"))
    val client = Job.createHttpClient()

    areaSets.foreach { implicit area =>

      val areaConfig = AreaConfig(config.getConfig(s"gundam.${area.configName}"), area)

      val source = new HttpClientHtmlSource(client, areaConfig)
      val db = Database.forConfig("gundam.slickdatabase")
      //val sink = new SlickPersistSink(areaConfig, db)
      val sink = new ExcelPersistSink(directory, areaConfig, config.getBoolean("deleteExistingFile"))
      area.setGroups.par.foreach {
        case (groupSymbol, sets) =>
         processGroup(groupSymbol, sets, source, sink)
      }
    }

    client.close()
  }

  def processGroup(groupSymbol: String, sets: ListMap[String, String], htmlSource: HtmlSource, persistSink: PersistSink[CategoryCards]): Unit ={

    logger.info(s"start processing group: $groupSymbol")
    val cc = (sets map {
      case (category, categoryName) =>
        logger.info(s"starting $categoryName, $category")
        htmlSource.loadHtml(category).flatMap(Parsers.parseAsCategoryCards)
    }).toList.foldLeft[Either[Throwable, CategoryCards]](Right(Models.EmptyCategoryCards)) { (b, n) =>
      (b, n) match {
        case (l@Left(_), _) => l
        case (_, l@Left(_)) => l
        case (Right(cc), Right(cc1)) => Right(cc.add(cc1))
      }
    }

    logger.info(s"start persisting group: $groupSymbol")
    cc.map { cards =>
      persistSink.persist(groupSymbol, cards.sort())
    } match {
      case Right(_) => logger.info(s"finished processing group: $groupSymbol")
      case Left(e) => logger.error(s"failed to process group: $groupSymbol", e)
    }

  }
}

object SaveHtml extends Logging {
  def main(args: Array[String]): Unit = {
    val areaSets = args2AreaSets(args)

    val config = ConfigFactory.load()
    val directory = Paths.get(config.getString("fileDirectory"))
    val client = Job.createHttpClient()

    areaSets.foreach { implicit area =>

      val areaConfig = AreaConfig(config.getConfig(s"gundam.${area.configName}"), area)

      val source = new HttpClientHtmlSource(client, areaConfig)
      //val sink = new SlickPersistSink(areaConfig, db)
      val sink = new HtmlPersistSink(directory, areaConfig, config.getBoolean("deleteExistingFile"))
      area.setGroups.par.foreach {
        case (_, sets) =>
           sets.foreach{
             case (category, categoryName) =>
               source.loadHtml(category).map(sink.persist(category, _)) match {
                 case Left(e) => logger.error(s"failed to save $categoryName:$category as html", e)
                 case _ =>
               }
           }
      }
    }

    client.close()
  }
}
