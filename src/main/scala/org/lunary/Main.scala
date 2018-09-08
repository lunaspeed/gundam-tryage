package org.lunary

import java.io.File

import com.typesafe.config.ConfigFactory
import org.lunary.Models._

object Main {

  def main(arg: Array[String]): Unit = {

    val areaSets = if(arg.isEmpty) {
      List("japan", "asia")
    }
    else {
      arg.toList
    }

    val config = ConfigFactory.load()
    val folder = new File(config.getString("fileDirectory"))
    val client = Job.createHttpClient()

    for(areaSet <- areaSets) {

      implicit val area = AreaConfig(config.getConfig(s"gundam.$areaSet"), if(areaSet == "japan") JAPAN_SETS else ASIA_SETS)

      area.areaSets.setGroups.par.foreach {
        case (groupSymbol, sets) =>
          val file = new File(folder, s"${area.filePrefix}-gundam-tryage-$groupSymbol.xlsx")
          val excel = new Excel(config)
          excel.generate(file, sets, client)
      }
    }
  }
}
