package org.lunary

import java.io.File

import com.typesafe.config.ConfigFactory
import org.lunary.Models._

object Main {

  def main(arg: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val folder = new File(config.getString("fileDirectory"))
    val client = Job.createHttpClient()

    val area = Japan
    implicit val areaConfig = AreaConfig(config.getConfig(s"gundam.${area.configName}"), area)

    area.setGroups.par.foreach {
      case (groupSymbol, sets) =>
        val file = new File(folder, s"${areaConfig.filePrefix}-gundam-tryage-$groupSymbol.xlsx")
        val excel = new Excel(config)
        excel.generate(file, sets, client)
    }
  }
}
