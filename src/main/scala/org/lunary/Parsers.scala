package org.lunary

import henix.ssoup.Selectors
import Selectors._
import cats.data.NonEmptyList
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.lunary.Models._

import collection.JavaConverters._
import scala.util.Try

object Parsers {

  type ParseResult = Either[Throwable, List[Option[Card]]]

  case class ParseException(html: String, cause: Throwable) extends RuntimeException(s"failed to parse: $html", cause)

  //select1有side effect
  def selectOne(elements: Iterator[Element]): Option[Element] = select(elements).headOption

  def parse(html: String): ParseResult = {

    val doc = Jsoup.parse(html)
    val result: Option[ParseResult] = selectOne(doc |>> "div#list") map { list: Element =>

      val divs = select(list > "div")

      val cards: Stream[ParseResult] = divs map { d =>

        (select(d > "div.carddateCol").headOption match {
          case Some(cardType) =>
            if (cardType.hasClass("mscardCol")) {
              extractMobileSuit(d)
            }
            else if (cardType.hasClass("plcardCol")) {
              extractPilot(d)
            } else {
              extractUnknownType(d, "carddateCol")
            }
          case None =>
            select(d > "div.frame").headOption match {
              case Some(cardType) =>
                if (cardType.hasClass("BgIgnlist")) {
                  extractIgnition(d, false)
                }
                else if (cardType.hasClass("BgIgnlist02")) {
                  extractIgnition(d, true)
                }
                else {
                  extractUnknownType(cardType, "frame")
                }
              case None =>
                extractUnknownType(d, "")
            }
        }).left.map(t => ParseException(d.html, t)).asInstanceOf[ParseResult]

      }

      val cardResult: ParseResult = cards.foldLeft[ParseResult](Right(Nil)) { (r, c) =>
        (r, c) match {
          case (Right(cards), Right(card)) => Right[Throwable, List[Option[Card]]](cards ++ card)
          case (l@Left(_), _) => l
          case (_, l@Left(_)) => l.asInstanceOf[ParseResult]
        }
      }
      cardResult
    }
    result.fold[ParseResult](Left(new RuntimeException("no div#list found")))(identity)
  }

  def extractMobileSuit(e: Element): Either[Throwable, Option[MobileSuit]] = Try {

    def toMs(basic: Basic, info1: Element, transformed: Option[MobileSuit]): Option[MobileSuit] = {
      val so = s1Opt(info1) _
      for {
        info2 <- selectOne(info1 + "div.info2Col")
        info3 <- selectOne(info2 + "div.info3Col")
        attr <- extractAttribute(info1)
        pilotName = select(info2 |>> "img[alt='パイロット']").headOption.map { e =>
          e.parent().nextElementSibling().text()
        }
        special <- so("li.spPower") //.text.trim.toInt
        cost <- so("li.Cost") //.text.trim.toInt
        space <- so("li.pSpace") //.text.trim
        ground <- so("li.pGrand") //.text.trim
        water = so("li.p3").flatMap(_.text.toSome)
        forest = so("li.p4").flatMap(_.text.toSome)
        desert = so("li.p5").flatMap(_.text.toSome)
        wazaName <- so("dd.wazaName") //.text.trim
        mecName = so("dd.MecName").flatMap(_.text.toSome)
        aceEffect = select(info2 |>> "img[alt='エース効果']").headOption.map { e =>
          e.parent().nextElementSibling().text()
        }
        abilityString <- selectOne(info3 >> "dt" > "p") //.text.trim.split("/").toList
        abilities = abilityString.text.trim.split("/").toList
        text <- selectOne(info3 >> "dd" > "p") //.text.trim
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
      firstPart <- selectOne(e |>> "div.info1col")
      secondPart = select(e |>> "div.info1col_v").headOption.flatMap(toMs(basic, _, None))
      firstMs <- toMs(basic, firstPart, secondPart)
    } yield firstMs
  }.toEither


  def extractPilot(e: Element): Either[Throwable, Option[Pilot]] = Try {
    for {
      basic <- extractBasicNew(e)
      attr <- extractAttribute(e)
      info1 <- selectOne(e |>> "div.info1col")
      info2 <- selectOne(info1 + "div.info2Col")
      info3 <- selectOne(info2 + "div.info3Col")

      burst <- selectOne(info1 >> "dd.burstCol")
      burstName <- selectOne(burst > "p.bName") //.text.trim
      burstLevel <- selectOne(burst > "ul.burst" > "li.bLv") //.text.trim.toInt
      burstType <- selectOne(burst >> "li.bType" > "img") //.attr("src").trim
      aceEffect = selectOne(info2 |>> "img[alt='エース効果']").map { e =>
        e.parent().nextElementSibling().text()
      }
      outer <- selectOne(info3 |>> "dt.pl")
      skill <- selectOne(outer > "p") //.text()
      text <- selectOne(outer.nextElementSibling() > "p") //.text()
      info3Ex = selectOne(info3 + "div.ex")
      awaken = info3Ex.flatMap { ex =>
        for {
          e <- selectOne(ex |>> "dt.ex")
          name <- selectOne(e > "p") //.text
          requirement <- selectOne(e.nextElementSibling() > "p") //.text
        } yield ExAwaken(name.text, requirement.text)
      }
    } yield {

      Pilot(basic,
        attr,
        burstName.text.trim, burstLevel.text.trim.toInt, burstType.attr("src").trim, aceEffect,
        skill.text, text.text, awaken)
    }
  }.toEither

  def extractIgnition(e: Element, hasPilotSkill: Boolean): Either[Throwable, Option[Ignition]] = Try {
    val s = s1Opt(e) _
    for {
      basic <- extractBasic(e)
      wazaName <- s("dd.wazaName") //.text.trim
      special <- s("dd.spPower") //.text.trim.toInt
      pilotName <- s("dd.PlName") //.text.trim
      effectElement <- selectOne(e |>> "ul.ignEffect")
      (effectSkill, effectText) <- extractSkillAndText(effectElement)
      skillElement <- selectOne(e |>> "ul.ignPlSkill")
      (pilotSkill, pilotSkillText) = if (hasPilotSkill) {
        extractSkillAndText(skillElement).fold[(Option[String], Option[String])]((None, None)){ case (ps, pt) => (Some(ps), Some(pt)) }
      }
      else {
        (None, None)
      }
    } yield {
      Ignition(basic,
        wazaName.text.trim, special.text.trim.toInt, pilotName.text.trim,
        effectSkill, effectText, pilotSkill, pilotSkillText)
    }
  }.toEither

  def extractUnknownType(e: Element, clazz: String): Either[Throwable, Option[UnknownType]] = Try {
    for {
      basic <- extractBasicNew(e)
      cardType <- selectOne(e > s"div.$clazz")
      classes = cardType.classNames().asScala - clazz
    } yield UnknownType(basic, classes.toSet)
  }.toEither

  def extractBasicNew(e: Element): Option[Basic] = for {
    firstPart <- selectOne(e |>> "div.info1col")
    cardNumber <- selectOne(firstPart |>> "dd.cardNumber") //.text().trim
    cardNo = cardNumber.text().trim
    set = cardNo.split('-')(0)
    name <- selectOne(firstPart |>> "dd.charaName") //.text().trim
    rarity <- selectOne(e |>> "div.reaCol") match {
      case Some(e) => selectOne(e |>> "dd")
      case _ => None
    } //.text().trim
    img <- selectOne(e >> "div.cardCol" > "img") //.attr("src").trim
  } yield {
    Basic(set, cardNo, name.text().trim, Some(rarity.text().trim), img.attr("src").trim)
  }

  def extractBasic(e: Element): Option[Basic] = for {
    cardNumber <- selectOne(e |>> "dd.cardNumber") //.text().trim
    cardNo = cardNumber.text().trim
    set = cardNo.split('-')(0)
    name <- selectOne(e |>> "dd.charaName") //.text().trim
    rarity <- (select(e |>> "dd.PreaP").headOption match {
      case s@Some(_) => s
      case None => selectOne(e |>> "dd.reaP")
    }) //.text.toSome
    img <- selectOne(e >> "dd.cardImg" > "img") //.attr("src").trim
  } yield {
    Basic(set, cardNo, name.text().trim, rarity.text.toSome, img.attr("src").trim)
  }

  def extractAttribute(e: Element): Option[Attribute] = for {
    hp <- selectOne(e |>> "li.hpPoint") //.text().trim.toInt
    power <- selectOne(e |>> "li.powerPoint") //.text().trim.toInt
    speed <- selectOne(e |>> "li.spPoint") //.text().trim.toInt
  } yield Attribute(hp.text().trim.toInt, power.text().trim.toInt, speed.text().trim.toInt)


  def extractSkillAndText(e: Element): Option[(String, String)] = for {
    skill <- selectOne(e |>> "li.Skill") //.text.trim
    text <- selectOne(e |>> "li.txt") //.text.trim
  } yield (skill.text.trim, text.text.trim)

  //def s1(e: Element)(evaluator: String): Element = select1(e |>> evaluator)

  def s1Opt(e: Element)(evaluator: String): Option[Element] =
    select(e |>> evaluator).headOption


  implicit class MyString(s: String) {
    def toSome(): Option[String] = s.trim match {
      case "-" | "" => None
      case _ => Some(s)
    }
  }

}
