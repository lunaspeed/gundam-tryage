package org.lunary

import java.io.File

import com.typesafe.config.ConfigFactory
import org.lunary.Models._

object Main {

  def main(arg: Array[String]): Unit = {

    val areaSets = if(arg.isEmpty) {
      List(Japan, Asia)
    }
    else {
      arg.map { s =>
        if (s.toLowerCase.startsWith("j")) {
          Japan
        }
        else {
          Asia
        }
      }.toList
    }

    val config = ConfigFactory.load()
    val folder = new File(config.getString("fileDirectory"))
    val client = Job.createHttpClient()

    areaSets.foreach { implicit area =>

      implicit val areaConfig = AreaConfig(config.getConfig(s"gundam.${area.configName}"), area)

      area.setGroups.par.foreach {
        case (groupSymbol, sets) =>
          val file = new File(folder, s"${areaConfig.filePrefix}-gundam-tryage-$groupSymbol.xlsx")
          val excel = new Excel(config)
          excel.generate(file, sets, client)
      }
    }
  }
}
