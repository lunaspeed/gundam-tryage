package org.lunary

import java.io.File

import com.typesafe.config.ConfigFactory

object Main {

  def main(arg: Array[String]): Unit = {

    implicit val config = ConfigFactory.load()
    val folder = new File(config.getString("fileDirectory"))

    val client = Job.createHttpClient()
    Models.setGroups.par.foreach {
      case (groupSymbol, sets) =>
        val file = new File(folder, s"gundam-tryage-$groupSymbol.xlsx")
        val excel = new Excel
        excel.generate(file, sets, client)
    }
  }
}
