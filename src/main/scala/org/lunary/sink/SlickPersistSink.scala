package org.lunary.sink

import org.lunary.Models._
import org.lunary.sink.Tables._
import slick.jdbc.MySQLProfile
import slick.lifted.TableQuery

import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}
import scala.concurrent.duration._

class SlickPersistSink(areaConfig: AreaConfig, db: MySQLProfile.backend.Database)(implicit ec: ExecutionContext) extends PersistSink[CategoryCards] {
  import SlickPersistSink._
  override def persist(indicator: String, data: CategoryCards): Unit = {

    val msTable = TableQuery[MobileSuits]
    val pTable = TableQuery[Pilots]
    val iTable = TableQuery[Ignitions]
    val uTable = TableQuery[UnknownTypes]
    val area = areaConfig.filePrefix

    val a1 = DBIO.sequence(data.mobileSuits.map(msModel2Db(area, _)).map(msTable.insertOrUpdate))
    val a2 = DBIO.sequence(data.pilots.map(pModel2Db(area, _)).map(pTable.insertOrUpdate))
    val a3 = DBIO.sequence(data.ignitions.map(iModel2Db(area, _)).map(iTable.insertOrUpdate))
    val a4 = DBIO.sequence(data.unknowns.map(uModel2Db(area, _)).map(uTable.insertOrUpdate))

    val f = db.run(DBIO.seq(a1, a2, a3, a4))
    f.onComplete {
      case Success(_) => logger.info(s"inserted $indicator into database")
      case Failure(e) => logger.error(s"failed to insert $indicator into database", e)
    }

    Await.ready(f, 1 minute)
  }
}

object SlickPersistSink {

  def msModel2Db(area: String, ms: MobileSuit): MobileSuitsRow = {
    MobileSuitsRow(
      area,
      ms.basic.set,
      ms.basic.cardNo,
      ms.basic.name,
      ms.basic.rarity,
      ms.basic.image,
      ms.pilotName,
      ms.attribute.hp,
      ms.attribute.power,
      ms.attribute.speed,
      ms.special,
      ms.cost,
      ms.powerRank.space,
      ms.powerRank.ground,
      ms.powerRank.water,
      ms.powerRank.forest,
      ms.powerRank.desert,
      ms.wazaName,
      ms.mecName,
      ms.aceEffect,
      ms.abilities.head,
      ms.abilities.tail.headOption,
      ms.text
    )
  }

  def pModel2Db(area: String, p: Pilot): PilotsRow = {
    PilotsRow(
      area,
      p.basic.set,
      p.basic.cardNo,
      p.basic.name,
      p.basic.rarity,
      p.basic.image,
      p.attribute.hp,
      p.attribute.power,
      p.attribute.speed,
      p.burstName,
      p.burstLevel,
      p.burstType,
      p.aceEffect,
      p.skill,
      p.text
    )
  }

  def iModel2Db(area: String, i: Ignition): IgnitionsRow = {
    IgnitionsRow(
      area,
      i.basic.set,
      i.basic.cardNo,
      i.basic.name,
      i.basic.rarity,
      i.basic.image,
      i.wazaName,
      i.special,
      i.pilotName,
      i.effectSkill,
      i.effectText,
      i.pilotSkill,
      i.pilotSkillText
    )
  }

  def uModel2Db(area: String, u: UnknownType): UnknownTypesRow = {
    UnknownTypesRow(
      area,
      u.basic.set,
      u.basic.cardNo,
      u.basic.name,
      u.basic.rarity,
      u.basic.image,
      u.typeClasses.mkString(",")
    )
  }

//  def main(args: Array[String]): Unit = {
//    import scala.concurrent.ExecutionContext.Implicits.global
//    import slick.jdbc.MySQLProfile.api._
//    import slick.jdbc.MySQLProfile
//    slick.codegen.SourceCodeGenerator.main(Array("slick.jdbc.MySQLProfile", "com.mysql.cj.jdbc.Driver", "jdbc:mysql://127.0.0.1/gundam", "/Users/Lunaspeed/Projects/Steven/gundam-tryage/src/main/scala", "org.lunary.sink", "root", ""))
//  }
}