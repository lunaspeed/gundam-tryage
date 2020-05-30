package org.lunary

import org.jsoup.Jsoup
import org.scalatest.flatspec.AnyFlatSpecLike

class ParserBoostTest extends AnyFlatSpecLike {

  val boostHtml =
    """
      |                <div class="carddateCol boostCol">
      |                  <div class="dateCol sidewaysCol">
      |                    <div class="col_r">
      |                      <div class="info1col">
      |                        <dl class="date1col">
      |                          <dt>カードリスト</dt>
      |                          <dd class="cardNumber">EB1-085</dd>
      |                                                    <dd class="cardType"><img src="../images/cardlist/eb/boost/tit_card_type_01.png" width="166" height="23" alt="ブースト"></dd>
      |                          <dd class="charaName">フレイ・アルスター</dd>
      |                        </dl>
      |                      </div>
      |
      |                      <div class="info2Col boostEffectCol">
      |                        <dl>
      |                          <dt><img src="../images/cardlist/eb/boost/tit_effect.png" width="135" height="20" alt="ブースト効果"></dt>
      |                          <dd>仲間の必殺技がアップ</dd>
      |                        </dl>
      |                        <dl>
      |                          <dt><img src="../images/cardlist/eb/boost/tit_point.png" width="135" height="20" alt="ポイント獲得条件"></dt>
      |                          <dd>必殺技を成功させる</dd>
      |                        </dl>
      |                      </div>
      |
      |                      <div class="info3Col boostLevelCol">
      |                        <dl>
      |                          <dt class="levelCol lv01"><b><img src="../images/cardlist/eb/boost/tit_level-01.png" width="60" height="26" alt="LV1"></b>必殺技を1回成功させる</dt>
      |                          <dd>ランダムで仲間1機の必殺技+3000。</dd>
      |                        </dl>
      |                        <dl>
      |                          <dt class="levelCol lv02"><b><img src="../images/cardlist/eb/boost/tit_level-02.png" width="60" height="26" alt="LV2"></b>必殺技を2回成功させる</dt>
      |                          <dd>ランダムで仲間2機の必殺技+3000。</dd>
      |                        </dl>
      |                        <dl>
      |                          <dt class="levelCol lv03"><b><img src="../images/cardlist/eb/boost/tit_level-03.png" width="60" height="26" alt="LV3"></b>必殺技を3回成功させる</dt>
      |                          <dd>仲間全員の必殺技+3000。</dd>
      |                        </dl>
      |                      </div>
      |                    </div>
      |
      |
      |                    <div class="col_l">
      |                      <div class="cardCol">
      |                        <img src="../images/cardlist/dammy/EB1-085.png" width="235" height="161" alt="フレイ・アルスター">
      |                      </div>
      |                      <div class="reaCol">
      |                        <dl>
      |                          <dt><img src="../images/cardlist/eb/boost/tit_rea.png" width="235" height="28" alt="レアリティ"></dt>
      |                          <dd>CP</dd>
      |                        </dl>
      |                      </div>
      |                                                                  <div class="logoCol">
      |                        <img src="../images/cardlist/tekketsu/parts/logo/logo_seed.png">
      |                      </div>
      |                                          </div>
      |                  </div>
      |                                                    </div>
      |""".stripMargin

  final val simpleBoost = Jsoup.parse(boostHtml)
  "boost card basics" should "be extracted" in {
    Parsers.extractBasicNew(simpleBoost) match {
      case Right(b) =>
        assertResult("フレイ・アルスター")(b.name)
      case Left(e) => throw e
    }
  }

  "boost card" should "be extracted" in {
    Parsers.extractBoost(simpleBoost) match {
      case Right(b) =>
        assertResult("仲間の必殺技がアップ")(b.effect)
        assertResult("必殺技を成功させる")(b.requirement)
        assertResult("ランダムで仲間1機の必殺技+3000。")(b.lvl1Effect)
        assertResult("必殺技を1回成功させる")(b.lvl1Requirement)
        assertResult("ランダムで仲間2機の必殺技+3000。")(b.lvl2Effect)
        assertResult("必殺技を2回成功させる")(b.lvl2Requirement)
        assertResult("仲間全員の必殺技+3000。")(b.lvl3Effect)
        assertResult("必殺技を3回成功させる")(b.lvl3Requirement)


      case Left(e) => throw e
    }
  }
}
