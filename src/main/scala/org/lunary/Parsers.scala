package org.lunary

import henix.ssoup.{QueryParser, Selectors}
import Selectors._
import cats.data.NonEmptyList
import cats.implicits._
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Evaluator
import org.lunary.Models._


import collection.JavaConverters._
import scala.util.Try

object Parsers {

  type ParseResult = Either[Throwable, List[Card]]

  type SelectResult[T] = Either[ElementNotFountException, T]

  case class ParseException(html: String, cause: Throwable) extends RuntimeException(s"failed to parse: $html", cause)

  case class ElementNotFountException(selector: String) extends RuntimeException(s"'${selector}' is not found")

  //select1有side effect
  def selectOne(elements: Iterator[Element]): Option[Element] = elements.toStream.headOption

  def parse(html: String): ParseResult = {

    val doc = Jsoup.parse(html)
    val result: Option[ParseResult] = selectOne(doc |>> "div#list") map { list: Element =>

      val divs = select(list > "div")

      val cards: Stream[Either[Throwable, Card]] = divs map { d =>

        (selectOne(d > "div.carddateCol") match {
          case Some(cardType) if cardType.hasClass("mscardCol") =>
              extractMobileSuit(d)
          case Some(cardType) if cardType.hasClass("plcardCol") =>
              extractPilot(d)
          case Some(cardType) if cardType.hasClass("boostCol") =>
              extractBoost(d)
          case Some(_) =>
              extractUnknownType(d, "carddateCol")
          case None =>
            select(d > "div.frame").headOption match {
              case Some(cardType) if cardType.hasClass("BgIgnlist") =>
                  extractIgnition(d, false)
              case Some(cardType) if cardType.hasClass("BgIgnlist02") =>
                  extractIgnition(d, true)
              case Some(cardType) =>
                  extractUnknownType(cardType, "frame")
              case None =>
                extractUnknownType(d, "")
            }
        }).left.map(t => ParseException(d.html, t))

      }

      val cardResult: ParseResult = cards.toList.sequence


//      cards.foldLeft[ParseResult](Right(Nil)) { (r, c) =>
//        (r, c) match {
//          case (Right(cards), Right(card)) => Right[Throwable, List[Card]](cards :+ card)
//          case (l@Left(_), _) => l
//          case (_, l@Left(_)) => l.asInstanceOf[ParseResult]
//        }
//      }

      cardResult
    }
    result.fold[ParseResult](Left(new RuntimeException("no div#list found")))(identity)
  }

  def extractMobileSuit(e: Element): Either[Throwable, MobileSuit] = Try {

    def toMs(basic: Basic, info1: Element, transformed: Option[MobileSuit]): SelectResult[MobileSuit] = {
      for {
        info2 <- selectOne(info1 + "div.info2Col").asMustResult("info1 + div.info2Col")
        info3 <- selectOne(info2 + "div.info3Col").asMustResult("info2 + div.info3Col")
        attr <- extractAttribute(info1)
        pilotName = select(info2 |>> "img[alt='パイロット']").headOption.map { e =>
          e.parent().nextElementSibling().text()
        }
        special <- info1.findOne("li.spPower") //.text.trim.toInt
        cost <- info1.findOne("li.Cost") //.text.trim.toInt
        space <- info1.findOne("li.pSpace") //.text.trim
        ground <- info1.findOne("li.pGrand") //.text.trim
        water <- info1.findOption("li.p3").map(_.flatMap(_.text.toSome))
        forest <- info1.findOption("li.p4").map(_.flatMap(_.text.toSome))
        desert <- info1.findOption("li.p5").map(_.flatMap(_.text.toSome))
        wazaName <- info1.findOne("dd.wazaName") //.text.trim
        mecName <- info1.findOption("dd.MecName").map(_.flatMap(_.text.toSome))
        aceEffect = select(info2 |>> "img[alt='エース効果']").headOption.map { e =>
          e.parent().nextElementSibling().text()
        }
        abilityString <- selectOne(info3 >> "dt" > "p").asMustResult("info3 >> \"dt\" > \"p\"") //.text.trim.split("/").toList
        abilities = abilityString.text.trim.split("/").toList
        text <- selectOne(info3 >> "dd" > "p").asMustResult("info3 >> \"dd\" > \"p\"") //.text.trim
      } yield {

        MobileSuit(basic,
          pilotName, attr, special.text.trim.toInt, cost.text.trim.toInt,
          PowerRank(space.text.trim, ground.text.trim, water, forest, desert),
          wazaName.text.trim, mecName, aceEffect, NonEmptyList(abilities.head, abilities.tail), text.text.trim,
          transformed)
      }
    }

    for {
      basic <- extractBasicNew(e)
      firstPart <- selectOne(e |>> "div.info1col").asMustResult("e |>> \"div.info1col\"")
      secondPart <- selectOne(e |>> "div.info1col_v") match {
        case Some(info) =>
          for {
            secondBasic <- extractBasicNew(info, basic.rarity, Some(basic.image))
            second <- toMs(secondBasic, info, None)
          } yield Some(second)
        case None => Right(None)
      }
      firstMs <- toMs(basic, firstPart, secondPart)
    } yield firstMs
  }.toEither.flatMap(identity)


  def extractPilot(e: Element): Either[Throwable, Pilot] = Try {
    for {
      basic <- extractBasicNew(e)
      attr <- extractAttribute(e)
      info1 <- selectOne(e |>> "div.info1col").asMustResult("e |>> \"div.info1col\"")
      info2 <- selectOne(info1 + "div.info2Col").asMustResult("info1 + \"div.info2Col\"")
      info3 <- selectOne(info2 + "div.info3Col").asMustResult("info2 + \"div.info3Col\"")
      burst <- selectOne(info1 >> "dd.burstCol").asMustResult("info1 >> \"dd.burstCol\"")
      burstName <- burst.findOne("p.bName")
      burstLevel <- burst.findOne("li.bLv")
      burstTypeLi <- burst.findOne("li.bType")
      burstType <- burstTypeLi.findOne("img")
      aceEffect = selectOne(info2 |>> "img[alt='エース効果']").map { e =>
        e.parent().nextElementSibling().text()
      }
      outer <- selectOne(info3 |>> "dt.pl").asMustResult("info3 |>> \"dt.pl\"")
      skill <- selectOne(outer > "p").asMustResult("outer > \"p\"") //.text()
      text <- selectOne(outer.nextElementSibling() > "p").asMustResult("outer.nextElementSibling() > \"p\"")
      awaken =
        for {
          ex <- selectOne(info3 + "div.ex")
          e <- selectOne(ex |>> "dt.ex")
          name <- selectOne(e > "p") //.text
          requirement <- selectOne(e.nextElementSibling() > "p") //.text
        } yield ExAwaken(name.text, requirement.text)
    } yield {

      Pilot(basic,
        attr,
        burstName.text.trim, burstLevel.text.trim.toInt, burstType.attr("src").trim, aceEffect,
        skill.text, text.text, awaken)
    }
  }.toEither.flatMap(identity)

  def extractIgnition(e: Element, hasPilotSkill: Boolean): Either[Throwable, Ignition] = Try {
    for {
      basic <- extractBasic(e)
      wazaName <- e.findOne("dd.wazaName")
      special <- e.findOne("dd.spPower")
      pilotName <- e.findOne("dd.PlName")
      effectElement <- selectOne(e |>> "ul.ignEffect").asMustResult("e |>> \"ul.ignEffect\"")
      effect <- extractSkillAndText(effectElement)
      (effectSkill, effectText) = effect
      skillElement = selectOne(e |>> "ul.ignPlSkill")
      (pilotSkill, pilotSkillText) =
        if (hasPilotSkill && skillElement.isDefined) {
          extractSkillAndText(skillElement.get)
            .fold[(Option[String], Option[String])](_ => (None, None), { case (ps, pt) => (Some(ps), Some(pt)) })
        }
        else {
          (None, None)
        }
    } yield {
      Ignition(basic,
        wazaName.text.trim, special.text.trim.toInt, pilotName.text.trim,
        effectSkill, effectText, pilotSkill, pilotSkillText)
    }
  }.toEither.flatMap(identity)

  def extractBoost(e: Element): Either[Throwable, Boost] = Try {

    def findLevelInfo(div: Element, lvl: Int): SelectResult[(String, String)] =
      for {
        dt <- div.findOne(s"dt.lv0$lvl")
        dl = dt.parent()
        dd <- dl.findOne("dd")
      } yield {
        (dt.text.trim, dd.text.trim)
      }

    for {
      basic <- extractBasicNew(e)
      boostEffectCol <- selectOne(e |>> ".boostEffectCol").asMustResult(".boostEffectCol")
      bEffectImg <- boostEffectCol.findOne("img[alt='ブースト効果']")
      bEffectDl = bEffectImg.parent().parent()
      effect <- bEffectDl.findOne("dd")
      bEffectImg <- boostEffectCol.findOne("img[alt='ポイント獲得条件']")
      bRequirementDl = bEffectImg.parent().parent()
      requirement <- bRequirementDl.findOne("dd")
      levelDiv <- e.findOne("div.boostLevelCol")
      lvl1 <- findLevelInfo(levelDiv, 1)
      (lvl1Req, lvl1Eff) = lvl1
      lvl2 <- findLevelInfo(levelDiv, 2)
      (lvl2Req, lvl2Eff) = lvl2
      lvl3 <- findLevelInfo(levelDiv, 3)
      (lvl3Req, lvl3Eff) = lvl3
    } yield {
      Boost(basic, effect.text.trim, requirement.text.trim,
        lvl1Req, lvl1Eff,
        lvl2Req, lvl2Eff,
        lvl3Req, lvl3Eff
      )
    }
  }.toEither.flatMap(identity)

  def extractUnknownType(e: Element, clazz: String): Either[Throwable, UnknownType] = Try {
    for {
      basic <- extractBasicNew(e)
      cardType <- selectOne(e > s"div.$clazz").asMustResult(s"""e > "div.$clazz"""")
      classes = cardType.classNames().asScala - clazz
    } yield UnknownType(basic, classes.toSet)
  }.toEither.flatMap(identity)

  def extractBasicNew(e: Element, knownRarity: Option[String] = None, knownImage: Option[String] = None): SelectResult[Basic] = for {
    firstPart <- selectOne(e |>> "dl.date1col").asMustResult("e |>> \"dl.date1col\"")
    cardNumber <- selectOne(firstPart |>> "dd.cardNumber").asMustResult("firstPart |>> \"dd.cardNumber\"")
    cardNo = cardNumber.text().trim
    set = cardNo.split('-')(0)
    name <- selectOne(firstPart |>> "dd.charaName").asMustResult("firstPart |>> \"dd.charaName\"")
    rarity <- knownRarity match {
      case r@Some(_) => Right(r)
      case None =>
        selectOne(e |>> "div.reaCol").asMustResult("e |>> \"div.reaCol\"")
            .flatMap(r => selectOne(r |>> "dd").asMustResult("rarityCol |>> \"dd\""))
            .map(_.text().toSome())
    }
    img <- knownImage match {
      case Some(u) => Right(u)
      case None =>
        selectOne(e >> "div.cardCol" > "img").asMustResult("e >> \"div.cardCol\" > \"img\"")
          .map(_.attr("src").trim)
    }

  } yield {
    Basic(set, cardNo, name.text().trim, rarity, img)
  }

  def extractBasic(e: Element): SelectResult[Basic] = for {
    cardNumber <- selectOne(e |>> "dd.cardNumber").asMustResult("e |>> \"dd.cardNumber\"")
    cardNo = cardNumber.text().trim
    set = cardNo.split('-')(0)
    name <- selectOne(e |>> "dd.charaName").asMustResult("e |>> \"dd.charaName\"")
    rarityCol = selectOne(e |>> "dd.PreaP")
    rarity <- rarityCol.orElse(selectOne(e |>> "dd.reaP")).asMustResult("e |>> \"dd.reaP\"")
    img <- selectOne(e >> "dd.cardImg" > "img").asMustResult("e >> \"dd.cardImg\" > \"img\"")
  } yield {
    Basic(set, cardNo, name.text().trim, rarity.text.toSome, img.attr("src").trim)
  }

  def extractAttribute(e: Element): SelectResult[Attribute] = {
    for {
      hp <- e.findOne("li.hpPoint") //.text().trim.toInt
      power <- e.findOne("li.powerPoint") //.text().trim.toInt
      speed <- e.findOne("li.spPoint") //.text().trim.toInt
    } yield Attribute(hp.text().trim.toInt, power.text().trim.toInt, speed.text().trim.toInt)
  }

  def extractSkillAndText(e: Element): SelectResult[(String, String)] = {
    for {
      skill <- e.findOne("li.Skill")
      text <- e.findOne("li.txt")
    } yield (skill.text.trim, text.text.trim)
  }

  implicit class Parent(e: Element) {
    def findOne(selector: Evaluator): SelectResult[Element] = {
      selectOne(e |>> selector).asMustResult(selector.toString)
    }

    def findOption(evaluator: String): SelectResult[Option[Element]] = {
      Right(selectOne(e |>> evaluator))
    }
  }

  implicit class MyString(s: String) {
    def toSome(): Option[String] = s.trim match {
      case "-" | "" => None
      case _ => Some(s)
    }
  }

  implicit class AsSelectResult(oe: Option[Element]) {
    def asMustResult(selector: String): SelectResult[Element] = oe match {
      case Some(e) => Right(e)
      case _ => Left(ElementNotFountException(selector))
    }
  }

}
