package org.lunary

import java.io.{File, FileOutputStream}

import com.softwaremill.sttp.SttpBackend
import com.typesafe.config.Config

import scala.concurrent.ExecutionContext
//import org.apache.http.impl.client.CloseableHttpClient
import org.apache.poi.common.usermodel.HyperlinkType
import org.apache.poi.xssf.usermodel.{XSSFRow, XSSFSheet, XSSFWorkbook}
import org.lunary.Models._

import scala.collection.immutable.ListMap
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import cats.data.EitherT
import cats.implicits._

class Excel(config: Config)(implicit areaConfig: AreaConfig) {

  private val imageBase = areaConfig.urlBase
  private val startingCell = 1
  private val area = areaConfig.area

  def generate(sets: ListMap[String, String])(implicit sttpBackend: SttpBackend[Future, Nothing], ec: ExecutionContext): Future[Either[Throwable, XSSFWorkbook]] = {

    implicit val book = new XSSFWorkbook()

    val job = new Job

    val msSheet = book.createSheet(area.sheetNameMobileSuit)
    val pilotSheet = book.createSheet(area.sheetNamePilot)

    val setResults = Future.sequence(sets.toList.reverse map { set =>
      genCategory(set._1, job)
    })

    setResults.map { list =>
      list.find(_.isLeft) match {
        case Some(Left(e)) => Left(e)
        case None =>

          val cards = list.flatMap(_.toOption)
            .reduce((c1, c2) => Cards(c1.mobileSuits ++ c2.mobileSuits, c1.pilots ++ c2.pilots, c1.ignitions ++ c2.ignitions, c1.unknowns ++ c2.unknowns))

          writeMS(msSheet, cards.mobileSuits)
          writePilot(pilotSheet, cards.pilots)
          if (!cards.ignitions.isEmpty) {
            val ignitionSheet = book.createSheet(area.sheetNameIgnition)
            writeIgnition(ignitionSheet, cards.ignitions)
          }
          if (!cards.unknowns.isEmpty) {
            val unknownSheet = book.createSheet("Unknown")
            writeUnknown(unknownSheet, cards.unknowns)
          }

          Right(book)
      }

    }
//    val t = for {
//      i <- sets.toList.reverse
//      (category, _) = i
//      htmlResponse <- job.request(category)
//      html <- htmlResponse.body
//      parsedItem <- Parsers.parse(html)
//    } yield {
//      val msCards = new ListBuffer[MobileSuit]
//      val pilotCards = new ListBuffer[Pilot]
//      val ignitionCards = new ListBuffer[Ignition]
//      val unknownCards = new ListBuffer[UnknownType]
//      parsedItem match {
//        case ms: MobileSuit => msCards += ms
//        case p: Pilot => pilotCards += p
//        case i: Ignition => ignitionCards += i
//        case u: UnknownType => unknownCards += u
//      }
//      writeMS(msSheet, msCards)
//      writePilot(pilotSheet, pilotCards)
//      if(!ignitionCards.isEmpty) {
//        val ignitionSheet = book.createSheet(area.sheetNameIgnition)
//        writeIgnition(ignitionSheet, ignitionCards)
//      }
//
//      if(!unknownCards.isEmpty) {
//        val unknownSheet = book.createSheet("Unknown")
//        writeUnknown(unknownSheet, unknownCards)
//      }
//      var fos: FileOutputStream = null
//      try {
//        fos = new FileOutputStream(file)
//        book.write(fos)
//      }
//      finally {
//        if(fos != null) {
//          fos.close()
//        }
//      }
//      1
//    }

  }

  case class Cards(mobileSuits: List[MobileSuit], pilots: List[Pilot], ignitions: List[Ignition], unknowns: List[UnknownType])

  def genCategory(category: String, job: Job)(implicit ec: ExecutionContext): Future[Either[Throwable, Cards]] = {
    (for {
      html <- EitherT(job.request(category))
      parsedItems <- EitherT.fromEither[Future](Parsers.parse(html))
    } yield {
      val msCards = new ListBuffer[MobileSuit]
      val pilotCards = new ListBuffer[Pilot]
      val ignitionCards = new ListBuffer[Ignition]
      val unknownCards = new ListBuffer[UnknownType]
      parsedItems.foreach {
        _ match {
          case ms: MobileSuit => msCards += ms
          case p: Pilot => pilotCards += p
          case i: Ignition => ignitionCards += i
          case u: UnknownType => unknownCards += u
        }
      }
      Cards(msCards.toList, pilotCards.toList, ignitionCards.toList, unknownCards.toList)
    }).value

  }


  def writeMS(sheet: XSSFSheet, cards: Seq[MobileSuit])(implicit wb: XSSFWorkbook): Unit = {

    implicit val s = sheet
    val titleRow = sheet.createRow(0)
    (titleBegin ++ area.mobileSuitTitles).zipWithIndex foreach {
      case (s, i) =>
        titleRow.createCell(startingCell - 1 + i).setCellValue(s)
    }
    //List("パイロット名",
    //      "ＨＰ", "アタック", "スピード", "必殺技", "必殺威力", "必殺コスト",
    //    "宇宙適性", "地上適性", "水中適性", "森林適性", "砂漠適性",
    //    "アビリティ名", "アビリティ", "ACE", "開発系統")

    cards.sortBy(_.basic.cardNo).zipWithIndex.foreach {
      case (ms, i) =>
        val row = sheet.createRow(i + 1)

        val start = writeBasic(startingCell, ms.basic, row)

        ms.pilotName foreach { row.createCell(start + 1).setCellValue(_) }
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

//    val ref = new AreaReference(new CellReference(0, 0), new CellReference(cards.size, 22), SpreadsheetVersion.EXCEL2007)
//    val table = sheet.createTable()
//    val cttable = table.getCTTable
//    cttable.setRef(ref.formatAsString())
//    cttable.setHeaderRowCount(1)
//    cttable.addNewAutoFilter()
//    cttable.setName("MS Table")
//    cttable.setDisplayName("MS Table")
//    val columns = cttable.addNewTableColumns()
//    columns.setCount(22)
//    table.setCellReferences(ref)
//    table.setDisplayName("MS Table")
//    table.setName("MS Table")

  }

  def writePilot(sheet: XSSFSheet, cards: Seq[Pilot])(implicit wb: XSSFWorkbook): Unit = {
    implicit val s = sheet

    val titleRow = sheet.createRow(0)
    (titleBegin ++ area.ignitionTitles).zipWithIndex foreach {
      case (s, i) =>
        titleRow.createCell(startingCell - 1 + i).setCellValue(s)
    }
    //List(
    //      "ＨＰ", "アタック", "スピード", "バースト", "バーストの種類", "バーストレベル",
    //      "スキル名", "スキル", "ACE")


    cards.sortBy(_.basic.cardNo).zipWithIndex.foreach {
      case (p, i) =>
        val row = sheet.createRow(i + 1)

        val start = writeBasic(startingCell, p.basic, row)
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
    }
  }

  def writeIgnition(sheet: XSSFSheet, cards: Seq[Ignition])(implicit wb: XSSFWorkbook): Unit = {
    implicit val s = sheet
    val titleRow = sheet.createRow(0)
    (titleBegin ++ area.ignitionTitles).zipWithIndex foreach {
      case (s, i) =>
        titleRow.createCell(startingCell - 1 + i).setCellValue(s)
    }
    //List("パイロット名",
    //      "必殺技", "必殺威力",
    //      "効果名", "効果", "パイロットスキル名", "パイロットスキル")


    cards.sortBy(_.basic.cardNo).zipWithIndex.foreach {
      case (ig, i) =>
        val row = sheet.createRow(i + 1)

        val start = writeBasic(startingCell, ig.basic, row)
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

  def writeUnknown(sheet: XSSFSheet, cards: Seq[UnknownType])(implicit wb: XSSFWorkbook): Unit = {
    implicit val s = sheet

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

        val start = writeBasic(startingCell, u.basic, row)

        row.createCell(start + 1).setCellValue(u.typeClasses.mkString(", "))
    }
  }


  private def writeBasic(startColumn: Int, basic: Basic, row: XSSFRow)(implicit wb: XSSFWorkbook, sheet: XSSFSheet): Int = {


    row.createCell(startColumn).setCellValue(basic.set)
    row.createCell(startColumn + 1).setCellValue(basic.cardNo)
    basic.rarity.foreach(row.createCell(startColumn + 2).setCellValue(_))
    row.createCell(startColumn + 3).setCellValue(basic.name)

    val imageCell = row.createCell(startColumn + 4)

    val ch = wb.getCreationHelper
    val ip = imageBase + basic.image.substring(3)
    val hl = ch.createHyperlink(HyperlinkType.URL)
    hl.setAddress(ip)

    imageCell.setCellValue("Link")
    imageCell.setHyperlink(hl)
    startColumn + 4
  }

  def getBurstType(imageLink: String): String =
    if(imageLink.endsWith("burst-atk.png")) {
      area.burstAttack//"アタック"
    }
    else if(imageLink.endsWith("burst-def.png")) {
      area.burstDefence//"ディフェンス"
    }
    else if(imageLink.endsWith("burst-spd.png")) {
      area.burstSpeed//"スピード"
    }
    else {
      "Unknown"
    }



  val titleBegin = area.baseTitles//List("所有する", "弾", "カード番号", "レアリティ", "カード名", "画像")
}
