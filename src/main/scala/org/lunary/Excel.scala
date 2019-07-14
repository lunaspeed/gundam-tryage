package org.lunary

import java.io.{File, FileOutputStream}

import com.typesafe.config.Config
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.poi.common.usermodel.HyperlinkType
import org.apache.poi.ss.usermodel.CreationHelper
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.{XSSFRow, XSSFWorkbook}
import org.lunary.Models._

import scala.collection.immutable.ListMap
import scala.util.Try

class Excel(config: Config, areaConfig: AreaConfig) {

  private val imageBase = areaConfig.urlBase
  private val startingCell = 1
  private val area = areaConfig.area

  def generate(file: File, sets: ListMap[String, String], client: CloseableHttpClient): Either[Throwable, Unit] = {

    if (file.exists() && config.getBoolean("deleteExistingFile")) {
      file.delete()
    }

    val job = new Job(areaConfig)

    val cardsResult: List[Either[Throwable, List[Card]]] = sets.toList.reverse.map {
      case (category, _) => job.request(category, client) flatMap Parsers.parse
    }

    //如何變成Either[Throwable, List[Card]]?
    val cards: Either[Throwable, List[Card]] = cardsResult.foldRight[Either[Throwable, List[Card]]](Right(Nil)) { (result, either) =>
      (result, either) match {
        case (Right(aggregated), Right(cards)) => Right(aggregated ++ cards)
        case (r: Left[Throwable, List[Card]], _) => r
        case (_, c: Left[Throwable, List[Card]]) => c
      }
    }


    //分成4種不同的LIST分開處理
    val separated: Either[Throwable, (List[MobileSuit], List[Pilot], List[Ignition], List[UnknownType])] = cards map {
      _.foldLeft[(List[MobileSuit], List[Pilot], List[Ignition], List[UnknownType])]((Nil, Nil, Nil, Nil)) { (lists, card) =>
        card match {
          case ms: MobileSuit => lists.copy(_1 = lists._1 :+ ms)
          case p: Pilot => lists.copy(_2 = lists._2 :+ p)
          case i: Ignition => lists.copy(_3 = lists._3 :+ i)
          case u: UnknownType => lists.copy(_4 = lists._4 :+ u)
        }
      }
    }

    //產生Excel
    val excel: Either[Throwable, XSSFWorkbook] = separated.map {
      case (mobileSuits, pilots, ignitions, unknowns) =>
        val book = new XSSFWorkbook()
        val generateTransformed = config.getBoolean("generateTransformed")
        writeMS(book, area.sheetNameMobileSuit, mobileSuits, generateTransformed)
        writePilot(book, area.sheetNamePilot, pilots)
        writeIgnition(book, area.sheetNameIgnition, ignitions)
        writeUnknown(book, "Unknown", unknowns)
        book
    }

    //寫成檔案，IO
    excel.flatMap { book =>
      var fos: FileOutputStream = null
      val result = Try {
        fos = new FileOutputStream(file)
        book.write(fos)
      }
      if (fos != null) {
        fos.close()
      }
      result.toEither
    }

  }

  def writeMS(wb: XSSFWorkbook, sheetName: String, cards: Seq[MobileSuit], generateTransformed: Boolean = false): Unit =

    if (!cards.isEmpty) {

      val sheet = wb.createSheet(sheetName)
      val titleRow = sheet.createRow(0)
      (titleBegin ++ area.mobileSuitTitles).zipWithIndex foreach {
        case (s, i) =>
          titleRow.createCell(startingCell - 1 + i).setCellValue(s)
      }

      cards.sortBy(_.basic.cardNo).foldLeft(1) {
        case (i, ms) =>
          val row = sheet.createRow(i)

          writeRow(row, ms)
          if (generateTransformed) {
            i + ms.transformed.fold(1) { ms =>
              val row2 = sheet.createRow(i + 1)
              writeRow(row2, ms)
              sheet.addMergedRegion(new CellRangeAddress(i, i + 1, 0, 0))
              sheet.addMergedRegion(new CellRangeAddress(i, i + 1, 1, 1))
              sheet.addMergedRegion(new CellRangeAddress(i, i + 1, 2, 2))
              sheet.addMergedRegion(new CellRangeAddress(i, i + 1, 3, 3))
              2
            }
          }
          else {
            i + 1
          }

      }

      def writeRow(row: XSSFRow, ms: MobileSuit): Unit = {

        val start = writeBasic(row, startingCell, ms.basic, wb.getCreationHelper)

        ms.pilotName foreach {
          row.createCell(start + 1).setCellValue(_)
        }
        //attribute
        row.createCell(start + 2).setCellValue(ms.attribute.hp)
        row.createCell(start + 3).setCellValue(ms.attribute.power)
        row.createCell(start + 4).setCellValue(ms.attribute.speed)
        //waza
        row.createCell(start + 5).setCellValue(ms.wazaName)
        row.createCell(start + 6).setCellValue(ms.special)
        row.createCell(start + 7).setCellValue(ms.cost)

        //power rank
        row.createCell(start + 8).setCellValue(ms.powerRank.space)
        row.createCell(start + 9).setCellValue(ms.powerRank.ground)
        ms.powerRank.water.foreach(row.createCell(start + 10).setCellValue(_))
        ms.powerRank.forest.foreach(row.createCell(start + 11).setCellValue(_))
        ms.powerRank.desert.foreach(row.createCell(start + 12).setCellValue(_))


        row.createCell(start + 13).setCellValue(ms.abilities.toList.mkString(","))
        row.createCell(start + 14).setCellValue(ms.text)
        ms.aceEffect.foreach(row.createCell(start + 15).setCellValue(_))
        ms.mecName.foreach(row.createCell(start + 16).setCellValue(_))
      }

    }


  def writePilot(wb: XSSFWorkbook, sheetName: String, cards: Seq[Pilot]): Unit =
    if (!cards.isEmpty) {

      val sheet = wb.createSheet(sheetName)

      val titleRow = sheet.createRow(0)
      (titleBegin ++ area.pilotTitles).zipWithIndex foreach {
        case (s, i) =>
          titleRow.createCell(startingCell - 1 + i).setCellValue(s)
      }


      cards.sortBy(_.basic.cardNo).zipWithIndex.foreach {
        case (p, i) =>
          val row = sheet.createRow(i + 1)

          val start = writeBasic(row, startingCell, p.basic, wb.getCreationHelper)
          //attribute
          row.createCell(start + 1).setCellValue(p.attribute.hp)
          row.createCell(start + 2).setCellValue(p.attribute.power)
          row.createCell(start + 3).setCellValue(p.attribute.speed)

          //burst
          row.createCell(start + 4).setCellValue(p.burstName)
          row.createCell(start + 5).setCellValue(getBurstType(p.burstType))
          row.createCell(start + 6).setCellValue(p.burstLevel)

          //skill
          row.createCell(start + 7).setCellValue(p.skill)
          row.createCell(start + 8).setCellValue(p.text)
          p.aceEffect.foreach(row.createCell(start + 9).setCellValue(_))
          p.exAwaken.foreach { aw =>
            row.createCell(start + 10).setCellValue(aw.name)
            row.createCell(start + 11).setCellValue(aw.requirement.replace("EX覚醒条件：", ""))
          }
      }

    }

  def writeIgnition(wb: XSSFWorkbook, sheetName: String, cards: Seq[Ignition]): Unit =
    if (!cards.isEmpty) {

      val sheet = wb.createSheet(sheetName)
      val titleRow = sheet.createRow(0)
      (titleBegin ++ area.ignitionTitles).zipWithIndex foreach {
        case (s, i) =>
          titleRow.createCell(startingCell - 1 + i).setCellValue(s)
      }

      cards.sortBy(_.basic.cardNo).zipWithIndex.foreach {
        case (ig, i) =>
          val row = sheet.createRow(i + 1)

          val start = writeBasic(row, startingCell, ig.basic, wb.getCreationHelper)
          row.createCell(start + 1).setCellValue(ig.pilotName)
          row.createCell(start + 2).setCellValue(ig.wazaName)
          row.createCell(start + 3).setCellValue(ig.special)
          //skill
          row.createCell(start + 4).setCellValue(ig.effectSkill)
          row.createCell(start + 5).setCellValue(ig.effectText)

          ig.pilotSkill.foreach(row.createCell(start + 6).setCellValue(_))
          ig.pilotSkillText.foreach(row.createCell(start + 7).setCellValue(_))
      }
    }

  def writeUnknown(wb: XSSFWorkbook, sheetName: String, cards: Seq[UnknownType]): Unit =
    if (!cards.isEmpty) {
      val sheet = wb.createSheet(sheetName)

      val titleRow = sheet.createRow(0)
      (titleBegin ++ List("HTML classes")).zipWithIndex foreach {
        case (s, i) =>
          titleRow.createCell(startingCell - 1 + i).setCellValue(s)
      }
      (titleBegin + "Type Classes").zipWithIndex foreach {
        case (s, i) =>
          titleRow.createCell(startingCell - 1 + i).setCellValue(s)
      }

      cards.sortBy(_.basic.cardNo).zipWithIndex.foreach {
        case (u, i) =>
          val row = sheet.createRow(i + 1)

          val start = writeBasic(row, startingCell, u.basic, wb.getCreationHelper)

          row.createCell(start + 1).setCellValue(u.typeClasses.mkString(", "))
      }
    }


  def writeBasic(row: XSSFRow, startColumn: Int, basic: Basic, ch: CreationHelper): Int = {

    row.createCell(startColumn).setCellValue(basic.set)
    row.createCell(startColumn + 1).setCellValue(basic.cardNo)
    basic.rarity.foreach(row.createCell(startColumn + 2).setCellValue(_))
    row.createCell(startColumn + 3).setCellValue(basic.name)

    val imageCell = row.createCell(startColumn + 4)

    val ip = imageBase + basic.image.substring(3)
    val hl = ch.createHyperlink(HyperlinkType.URL)
    hl.setAddress(ip)

    imageCell.setCellValue("Link")
    imageCell.setHyperlink(hl)
    startColumn + 4
  }

  def getBurstType(imageLink: String): String =
    if (imageLink.endsWith("burst-atk.png")) {
      area.burstAttack //"アタック"
    }
    else if (imageLink.endsWith("burst-def.png")) {
      area.burstDefence //"ディフェンス"
    }
    else if (imageLink.endsWith("burst-spd.png")) {
      area.burstSpeed //"スピード"
    }
    else {
      "Unknown"
    }


  val titleBegin = area.baseTitles //List("所有する", "弾", "カード番号", "レアリティ", "カード名", "画像")
}
