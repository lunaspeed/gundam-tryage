package org.lunary

import henix.ssoup.Selectors
import Selectors._
import cats.data.NonEmptyList
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.lunary.Models._

import collection.JavaConverters._

object Parsers {

  def parse(html: String): List[Card] = {

    val doc = Jsoup.parse(html)
    val list = select1(doc |>> "div#list")

    val divs = select(list > "div")

    val cards: Stream[Card] = divs map { d =>

      try {
        select(d > "div.carddateCol").headOption match {
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

        }

      }
      catch {
        case e: Throwable => println("current element " + d.html())
          throw e
      }

    }

    cards.toList
  }

  def extractMobileSuit(e: Element): MobileSuit = {
    val basic = extractBasicNew(e)

    def toMs(info1: Element): MobileSuit = {

      val info2 = select(info1 + "div.info2Col").head
      val info3 = select(info2 + "div.info3Col").head

      val attr = extractAttribute(info1)
      val s = s1(info1) _
      val so = s1Opt(info1) _


      val pilotName = select(info2 |>> "img[alt='パイロット']").headOption.map { e =>
        e.parent().nextElementSibling().text()
      }

      val special = s("li.spPower").text.trim.toInt
      val cost = s("li.Cost").text.trim.toInt
      val space = s("li.pSpace").text.trim
      val ground = s("li.pGrand").text.trim
      val water = so("li.p3").flatMap(_.text.toSome)
      val forest = so("li.p4").flatMap(_.text.toSome)
      val desert = so("li.p5").flatMap(_.text.toSome)
      val wazaName = s("dd.wazaName").text.trim
      val mecName = so("dd.MecName").flatMap(_.text.toSome)

      val aceEffect = select(info2 |>> "img[alt='エース効果']").headOption.map { e =>
        e.parent().nextElementSibling().text()
      }

      val abilities = select1(info3 >> "dt" > "p").text.trim.split("/").toList
      val text = select1(info3 >> "dd" > "p").text.trim

      MobileSuit(basic,
        pilotName, attr, special: Int, cost: Int,
        PowerRank(space, ground, water, forest, desert),
        wazaName, mecName, aceEffect, NonEmptyList(abilities.head, abilities.tail), text)
    }

    val firstPart = select1(e |>> "div.info1col")
    val firstMs = toMs(firstPart)

    val secondPart = select(e |>> "div.info1col_v").headOption

    (secondPart map toMs).fold(firstMs)(sec => firstMs.copy(transformed = Some(sec)))
  }

  def extractPilot(e: Element): Pilot = {
    val basic = extractBasicNew(e)
    val attr = extractAttribute(e)
    val info1 = select1(e |>> "div.info1col")
    val info2 = select(info1 + "div.info2Col").head
    val info3 = select(info2 + "div.info3Col").head

    val burst = select1(info1 >> "dd.burstCol")
    val burstName = select1(burst > "p.bName").text.trim
    val burstLevel = select1(burst > "ul.burst" > "li.bLv").text.trim.toInt
    //TODO extract type from image src
    val burstType = select1(burst >> "li.bType" > "img").attr("src").trim

    val aceEffect = select(info2 |>> "img[alt='エース効果']").headOption.map { e =>
      e.parent().nextElementSibling().text()
    }

    val (skill, text) = {
      val outer = select1(info3 |>> "dt.pl")
      val skill = select1(outer > "p").text()
      val text = select1(outer.nextElementSibling() > "p").text()
      (skill, text)

    }

    val info3Ex = select(info3 + "div.ex").headOption
    val awaken = info3Ex.map { ex =>
      val e = select1(ex |>> "dt.ex")
      val name = select1(e > "p").text
      val requirement = select1(e.nextElementSibling() > "p").text
      ExAwaken(name, requirement)
    }

    Pilot(basic,
      attr,
      burstName, burstLevel, burstType, aceEffect,
      skill, text, awaken)

  }

  def extractIgnition(e: Element, hasPilotSkill: Boolean): Ignition = {
    val basic = extractBasic(e)
    val s = s1(e) _
    val wazaName = s("dd.wazaName").text.trim
    val special = s("dd.spPower").text.trim.toInt
    val pilotName = s("dd.PlName").text.trim
    val (effectSkill, effectText) = extractSkillAndText(select1(e |>> "ul.ignEffect"))

    val (pilotSkill, pilotSkillText) = if (hasPilotSkill) {
      val (ps, pt) = extractSkillAndText(select1(e |>> "ul.ignPlSkill"))
      (Some(ps), Some(pt))
    }
    else {
      (None, None)
    }
    Ignition(basic: Basic,
      wazaName, special, pilotName,
      effectSkill, effectText, pilotSkill, pilotSkillText)
  }

  def extractUnknownType(e: Element, clazz: String): UnknownType = {
    val basic = extractBasicNew(e)
    val cardType = select1(e > s"div.$clazz")
    val classes = cardType.classNames().asScala - clazz
    UnknownType(basic, classes.toSet)
  }

  def extractBasicNew(e: Element): Basic = {
    
    val firstPart = select1(e |>> "div.info1col")

    val cardNo = select1(firstPart |>> "dd.cardNumber").text().trim

    val set = cardNo.split('-')(0)

    val name = select1(firstPart |>> "dd.charaName").text().trim

    val rarity = select1(select1(e |>> "div.reaCol") |>> "dd").text().trim

    val img = select1(e >> "div.cardCol" > "img").attr("src").trim

    Basic(set, cardNo, name, Some(rarity), img)
  }

  def extractBasic(e: Element): Basic = {

    val cardNo = select1(e |>> "dd.cardNumber").text().trim

    val set = cardNo.split('-')(0)

    val name = select1(e |>> "dd.charaName").text().trim

    val rarity = (select(e |>> "dd.PreaP").headOption match {
      case Some(e) => e
      case None => select1(e |>> "dd.reaP")
    }).text.toSome

    val img = select1(e >> "dd.cardImg" > "img").attr("src").trim

    Basic(set, cardNo, name, rarity, img)
  }

  def extractAttribute(e: Element): Attribute = {
    val hp = select1(e |>> "li.hpPoint").text().trim.toInt
    val power = select1(e |>> "li.powerPoint").text().trim.toInt
    val speed = select1(e |>> "li.spPoint").text().trim.toInt

    Attribute(hp, power, speed)
  }

  def extractSkillAndText(e: Element): (String, String) = {
    val skill = select1(e |>> "li.Skill").text.trim
    val text = select1(e |>> "li.txt").text.trim
    (skill, text)
  }

  def s1(e: Element)(evaluator: String): Element = try {
    select1(e |>> evaluator)
  }
  catch {
    case e: NoSuchElementException =>
      println("selector not found: " + evaluator)
      throw e
  }

  def s1Opt(e: Element)(evaluator: String): Option[Element] =
    select(e |>> evaluator).headOption


  implicit class MyString(s: String) {
    def toSome(): Option[String] = s.trim match {
      case "-" | "" => None
      case _ => Some(s)
    }
  }

}
