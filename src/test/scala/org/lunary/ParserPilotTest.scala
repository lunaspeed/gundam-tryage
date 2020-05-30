package org.lunary

import org.jsoup.Jsoup
import org.lunary.Models.{Attribute, ExAwaken}
import org.scalatest.flatspec.AnyFlatSpecLike

class ParserPilotTest extends AnyFlatSpecLike {

  val pilotHtml =
    """<div>
      |  <div class="carddateCol plcardCol ">
      |    <div class="dateCol">
      |      <div class="col_r">
      |        <div class="info1col">
      |          <dl class="date1col">
      |            <dd class="cardNumber">DW1-050</dd>
      |            <dd class="charaName">ヒイロ・ユイ</dd>
      |                                      <dd class="PstatusCol statusdateCol">
      |              <ul class="Pstatus">
      |                <li class="hpPoint">+2500</li>
      |                <li class="powerPoint">+1400</li>
      |                <li class="spPoint">+1000</li>
      |              </ul>
      |            </dd>
      |            <dd class="burstCol">
      |              <p class="bName">
      |                誰よりも戦い抜いてみせる!                            </p>
      |              <ul class="burst">
      |                <li class="bLv">2 </li>
      |                <li class="bType"><img src="../images/cardlist/tekketsu/parts/burst-def.png" width="82" height="13">
      |                </li>
      |              </ul>
      |            </dd>
      |          </dl>
      |        </div>
      |        <div class="info2Col">
      |          <dl>
      |            <dt><img src="../images/cardlist/dw/common/tit_ace.png" width="85" height="20" alt="エース効果"></dt>
      |            <dd>最終ラウンドに仲間全員の受けるダメージ-1500。</dd>
      |          </dl>                      </div>
      |        <div class="info3Col PlAbiCol ">
      |          <dl>
      |            <dt class="pl">
      |              <p>コロニーの使者</p>
      |            </dt>
      |            <dd>
      |              <p>ラウンド1のみ仲間全員の受けるダメージ-1500。</p>
      |            </dd>
      |          </dl>
      |          <ul class="plSkill abiIcon">
      |            <li class="icon"><li class="icon"><span class=""></span></li>
      |            </li>
      |          </ul>
      |        </div>
      |        <div class="info3Col PlAbiCol ex">
      |          <dl>
      |            <dt class="ex">
      |              <p>ゼロシステム</p>
      |            </dt>
      |            <dd>
      |              <p>EX覚醒条件：対象機体搭乗</p>
      |            </dd>
      |          </dl>
      |          <ul class="exKakusei abiIcon">
      |            <li class="icon"><span class="zerosystem"></span>
      |            </li>
      |          </ul>
      |        </div>
      |                          </div>
      |      <div class="col_l">
      |        <div class="cardCol">
      |          <img src="../images/cardlist/dammy/DW1-050.png" width="161" height="235" alt="ヒイロ・ユイ">
      |        </div>
      |        <div class="reaCol">
      |          <dl>
      |            <dt><img src="../images/cardlist/dw/ms/tit_rea.png" width="160" height="28" alt="レアリティ"></dt>
      |            <dd>M</dd>
      |          </dl>
      |        </div>
      |                                                    <div class="logoCol">
      |        <img src="../images/cardlist/tekketsu/parts/logo/logo_gundam-w.png">
      |        </div>
      |                            </div>
      |    </div>
      |                                  </div>
      |
      |</div>""".stripMargin

  val pilot = Jsoup.parse(pilotHtml)

  "pilot" should "parse from pilot html" in {

    Parsers.extractPilot(pilot) match {
      case Right(p) =>
        val basic = p.basic
        assertResult("DW1-050")(basic.cardNo)
        assertResult("../images/cardlist/dammy/DW1-050.png")(basic.image)
        assertResult("ヒイロ・ユイ")(basic.name)
        assertResult(Some("M"))(basic.rarity)
        assertResult("DW1")(basic.set)

        assertResult(Attribute(2500, 1400, 1000))(p.attribute)

        assertResult(2)(p.burstLevel)
        assertResult("誰よりも戦い抜いてみせる!")(p.burstName)

        assertResult(Some(ExAwaken("ゼロシステム", "EX覚醒条件：対象機体搭乗")))(p.exAwaken)
      case Left(e) => throw e
    }
  }
}
