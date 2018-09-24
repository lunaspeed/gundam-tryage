package org.lunary

import java.io.{File, FileOutputStream}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import com.softwaremill.sttp.{SttpBackend, SttpBackendOptions}
import com.softwaremill.sttp.asynchttpclient.future.AsyncHttpClientFutureBackend
import com.typesafe.config.ConfigFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.lunary.Models._

import scala.concurrent.{Await, Future}

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
    //val client = Job.createHttpClient()
    val options = SttpBackendOptions.Default.connectionTimeout(5 minutes)
    implicit val sttpBackend: SttpBackend[scala.concurrent.Future,Nothing] = AsyncHttpClientFutureBackend(options)

    val results = areaSets flatMap { implicit area =>

      implicit val areaConfig = AreaConfig(config.getConfig(s"gundam.${area.configName}"), area)

      area.setGroups map {
        case (groupSymbol, sets) =>
          val file = new File(folder, s"${areaConfig.filePrefix}-gundam-tryage-$groupSymbol.xlsx")
          val excel = new Excel(config)
          val book = excel.generate(sets)
          (file, book)
      }
    }

    val deleteExisting = config.getBoolean("deleteExistingFile")
    val writeResults = results.map {
      case (file, workbook) =>
        workbook.map(writeToFile(_, file, deleteExisting))
    }

    val res = Await.result(Future.sequence(writeResults), 30 minutes)

    for (r <- res) {
      r match {
        case Left(e) => println(e)
        case Right(_) =>
      }
    }

  }

  def writeToFile( workbook: Either[Throwable, XSSFWorkbook], file: File,deleteExisting: Boolean): Either[Throwable, Unit] =
    workbook flatMap { wb =>

      if (file.exists() && deleteExisting) {
        file.delete()
      }
      var fos: FileOutputStream = null
      try {
        fos = new FileOutputStream(file)
        wb.write(fos)
        Right(Unit)
      }
      catch {
        case e: Throwable => Left(e)
      }
      finally {
        if(fos != null) {
          fos.close()
        }
      }
    }


}


