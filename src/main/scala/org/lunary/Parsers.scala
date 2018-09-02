package org.lunary

import henix.ssoup.Selectors
import Selectors._
import cats.data.NonEmptyList
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Evaluator
import org.lunary.Models._

import collection.JavaConverters._

object Parsers {


  def parse(html: String): List[Card] = {
    val doc = Jsoup.parse(html)
    val list = select1(doc |>> "div#list")

    val divs = select(list > "div")

    val cards: Stream[Card] = divs map { d =>

      val cardType = select1(d > "div.frame")

      try {
        if (cardType.hasClass("BgMslist") || cardType.hasClass("BgMslist02")) {
          extractMobileSuit(d)
        }
        else if (cardType.hasClass("BgPllist") || cardType.hasClass("BgPllist02")) {
          extractPilot(d)
        }
        else if (cardType.hasClass("BgIgnlist")) {
          extractIgnition(d, false)
        }
        else if (cardType.hasClass("BgIgnlist02")) {
          extractIgnition(d, true)
        }
        else {
          extractUnknownType(d)
        }
      }
      catch {
        case e: Throwable => println("current element " + d.html())
          throw e
      }

    }

    cards.toList

  }



  /*
  <div style="display: block;">
       <div class="frame BgMslist "><!--MSカード-->
           <dl>
               <dt>カードリスト</dt>
               <dd class="cardImg"><img src="../images/cardlist/dammy/00-001.png" width="161" height="235" class="iepngfix"></dd>
               <dd class="cardNumber">00-001</dd>
               <dd class="charaName">ガンダム</dd>
               <dd class="wazaName">Ｇダイヴ・スラッシュ</dd>
               <dd>
                   <ul class="status">
                       <li class="hpPoint">2000</li>
                       <li class="powerPoint">3000</li>
                       <li class="spPoint">2400</li>
                   </ul>
               </dd>
               <dd>
                   <ul class="atack">
                       <li class="spPower">4500</li>
                       <li class="Cost">6</li>
                   </ul>
               </dd>
               <dd>
                   <ul class="place">
                       <li class="pSpace">○</li>
                       <li class="pGrand">○</li>
                       <li class="p3">-</li>
                       <li class="p4">-</li>
                       <li class="p5">-</li>
                   </ul>
               </dd><dd class="MecName">-</dd>
               <dd class="PlName">アムロ・レイ</dd>
               <dd class="aceEffect">-</dd>
               <dd>
                   <ul class="MsAbi">
                       <li class="abiName">反撃</li>
                       <li class="icon"><span class="hangeki"></span></li>
                    <li class="txt">敵の攻撃を回避した後、反撃する。<br>
                   </li></ul>
               </dd><dd class="reaP">P</dd>
                    <dd class="logo"><img src="../images/cardlist/tekketsu/parts/logo/logo_gundam.png"></dd>
                </dl>
           <!--MSカードEnd--></div>
       <!--プロモ-->
   </div>
   */
  def extractMobileSuit(e: Element): MobileSuit = {
    val basic = extractBasic(e)
    val attr = extractAttribute(e)
    val s = s1(e) _
    val so = s1Opt(e) _
    val pilotName = s("dd.PlName").text.toSome
    val special = s("li.spPower").text.trim.toInt
    val cost = s("li.spPower").text.trim.toInt
    val space = s("li.pSpace").text.trim
    val ground = s("li.pGrand").text.trim
    val water = so("li.p3").flatMap(_.text.toSome)
    val forest = so("li.p4").flatMap(_.text.toSome)
    val desert = so("li.p5").flatMap(_.text.toSome)
    val wazaName = s("dd.wazaName").text.trim
    val mecName = so("dd.MecName").flatMap(_.text.toSome)
    val aceEffect = so("dd.aceEffect").flatMap(_.text.toSome)
    val abilities = s("li.abiName").text.trim.split("/").toList
    val text = so("li.txt").fold(s("li.txt-bw"))(identity).text.trim

    //s("li.txt").text

    MobileSuit(basic,
      pilotName, attr, special: Int, cost: Int,
      PowerRank(space, ground, water, forest, desert),
      wazaName, mecName, aceEffect, NonEmptyList(abilities.head, abilities.tail), text)

  }

  /*
  <div style="display: block;">
iv class="frame BgPllist "><!--パイロットカード-->
  <dl>
      <dt>カードリスト</dt>
      <dd class="cardImg"><img src="../images/cardlist/dammy/00-038.png" width="161" height="235" class="iepngfix"></dd>
      <dd class="cardNumber">00-038</dd>
      <dd class="charaName" id=""> アムロ・レイ</dd>
      <dd>
          <ul class="Pstatus">
              <li class="hpPoint">+700</li>
              <li class="powerPoint">+1000</li>
              <li class="spPoint">+1300</li>
          </ul>
      </dd>
      <dd>
          <ul class="burst">
              <li class="bName">
                  <table border="0">
                      <tbody><tr>
                          <td>そこだぁ！うかつな奴め！</td>
                      </tr>
                  </tbody></table>
              </li>
              <li class="bLv">3</li>
              <li class="bType"><img src="../images/cardlist/tekketsu/parts/burst-atk.png" width="82" height="13"></li>
          </ul>
      </dd>
      <dd class="aceEffect">-</dd>
      <dd>
          <ul class="plSkill">
              <li class="Skill">連邦の白いヤツ</li>
              <li class="txt">ガンダム系ＭＳに搭乗すると、スピード＋400【1回限り】</li>

              <li class="icon"><span class=""></span></li>

                        </ul>
                    </dd>
                    <dd class="PreaP">P</dd>
                             <dd class="logo"><img src="../images/cardlist/tekketsu/parts/logo/logo_gundam.png"></dd>
                      </dl>
                <!--パイロットEnd--></div>
            <!--プロモ-->

                        </div>
   */
  def extractPilot(e: Element): Pilot = {
    val basic = extractBasic(e)
    val attr = extractAttribute(e)
    val s = s1(e) _
    val so = s1Opt(e) _
    val burst = s("ul.burst")
    val burstName = select1(burst > "li.bName" >> "td").text.trim
    val burstLevel = select1(burst > "li.bLv").text.trim.toInt
    //TODO extract type from image src
    val burstType = select1(burst > "li.bType" > "img").attr("src").trim
    val aceEffect = so("dd.aceEffect").flatMap(_.text.toSome)
    val (skill, text) = extractSkillAndText(e)

    Pilot(basic,
      attr,
      burstName, burstLevel, burstType, aceEffect,
      skill, text)

  }

  /*
  <div class="frame BgIgnlist02"><!--イグニッションカード-->
           <dl>
               <dt>カードリスト</dt>
               <dd class="cardImg"><img src="../images/cardlist/dammy/OA1-076.png" width="235" height="161" class="iepngfix"></dd>
               <dd class="cardNumber">OA1-076</dd>
               <dd class="charaName">クロスボーン・ガンダムX1フルクロス</dd>
               <dd class="wazaName">クロスボーン・ブラストスマッシャー</dd>
               <dd class="spPower">5200</dd>
               <dd class="PlName">トビア・アロナクス</dd>
               <dd>
                   <ul class="ignEffect">
                       <li class="Skill">必殺反撃</li>
                       <li class="txt">相手の必殺技に反撃する。発動条件：【1ラウンド】【1回限り】</li>

              <li class="icon"><span class="hissatsu-hangeki"></span></li>

              </ul>
          </dd>
                                              <dd>
            <ul class="ignPlSkill">
              <li class="Skill">不殺の意思</li>
              <li class="txt">攻撃時、ずっと相手のアタックを30%減少させる。【1回限り】</li>
            </ul>
          </dd>
           <dd class="PreaP">P</dd>
                      <dd class="logo"><img src="../images/cardlist/tekketsu/parts/logo/logo_x-bone_seven-of-steel.png"></dd>
      </dl>
      <!--イグニッションEnd--></div>


   <div class="frame BgIgnlist"><!--イグニッションカード-->
               <dl>
                   <dt>カードリスト</dt>
                   <dd class="cardImg"><img src="../images/cardlist/dammy/VS1-071.png" width="235" height="161" class="iepngfix"></dd>
                   <dd class="cardNumber">VS1-071</dd>
                   <dd class="charaName">ナイチンゲール</dd>
                   <dd class="wazaName">オーバー・ザ・メビウス［EX］</dd>
                   <dd class="spPower">6400</dd>
                   <dd class="PlName">シャア・アズナブル</dd>
                   <dd>
                       <ul class="ignEffect">
                           <li class="Skill">必殺追撃</li>
                           <li class="txt">必殺技で倒せなかった相手を追撃する。発動条件：【2ラウンド】【1回限り】</li>

              <li class="icon"><span class="hissatsu-tuigeki"></span></li>

                 </ul>
             </dd>
               <dd class="PreaP">P</dd>
                         <dd class="logo"><img src="../images/cardlist/tekketsu/parts/logo/logo_cca-children.png"></dd>
                                             </dl>
         <!--イグニッションEnd--></div>
   */
  def extractIgnition(e: Element, hasPilotSkill: Boolean): Ignition = {
    val basic = extractBasic(e)
    val s = s1(e) _
    val wazaName = s("dd.wazaName").text.trim
    val special = s("dd.spPower").text.trim.toInt
    val pilotName = s("dd.PlName").text.trim
    val (effectSkill, effectText) = extractSkillAndText(select1(e |>> "ul.ignEffect"))

    val (pilotSkill, pilotSkillText) = if(hasPilotSkill) {
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

  def extractUnknownType(e: Element): UnknownType = {
    val basic = extractBasic(e)
    val cardType = select1(e > "div.frame")
    val classes = cardType.classNames().asScala - "frame"
    UnknownType(basic, classes.toSet)
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
