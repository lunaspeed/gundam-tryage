package org.lunary.sink

import java.nio.file.{Files, Path}

import org.apache.logging.log4j.scala.Logging
import org.lunary.Excel
import org.lunary.Models.{AreaConfig, CategoryCards}

import scala.util.{Failure, Success, Try}

trait PersistSink[A] extends Logging {

  def persist(indicator: String, data: A): Unit
}

class ExcelPersistSink(directory: Path, areaConfig: AreaConfig, deleteExistingFile: Boolean = false) extends PersistSink[CategoryCards] {

  val excel = new Excel(areaConfig)

  override def persist(group: String, data: CategoryCards): Unit = {

    Try {
      val file = directory.resolve(s"${areaConfig.filePrefix}-gundam-tryage-$group.xlsx")

      if (Files.exists(file) && deleteExistingFile) {
        Files.delete(file)
      }

      val wb = excel.createWorkbook(data)

      wb.write(Files.newOutputStream(file))
    } match {
      case Success(_) => logger.info(s"group: $group finished generating excel")
      case Failure(e) => logger.error(s"group: $group failed to generate excel", e)
    }

  }
}
