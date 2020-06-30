package org.lunary

import org.jsoup.Jsoup
import org.scalatest.flatspec.AnyFlatSpec

class ParserIgnitionTest extends AnyFlatSpec {

//  val ignition1Html = """ <div>
//                        |<div class="frame BgIgnlist"><!--イグニッションカード-->
//                        |  <dl>
//                        |    <dt>カードリスト</dt>
//                        |    <dd class="cardImg"><img src="../images/cardlist/dammy/DW1-075.png" width="235" height="161" class="iepngfix"></dd>
//                        |      <dd class="cardNumber">DW1-075</dd>
//                        |      <dd class="charaName">∀ガンダム</dd>
//                        |      <dd class="wazaName">月光蝶</dd>
//                        |      <dd class="spPower">1200</dd>
//                        |      <dd class="PlName">ロラン・セアック,ソシエ・ハイム</dd>
//                        |      <dd>
//                        |        <ul class="ignEffect">
//                        |          <li class="Skill">必殺制圧</li>
//                        |          <li class="txt">相手から先攻を奪って必殺技で攻撃する。更に相手の攻撃を封印する。発動条件：【1ラウンド】【1回限り】</li>
//                        |
//                        |          <li class="icon"><span class="hissatsu-seiatu"></span></li>
//                        |
//                        |        </ul>
//                        |      </dd>
//                        |      <dd class="PreaP">C</dd>
//                        |      <dd class="logo"><img src="../images/cardlist/tekketsu/parts/logo/logo_turn-a.png"></dd>
//                        |      </dl>
//                        |      <!--イグニッションEnd--></div>
//                        |    <!--プロモ-->
//                        |
//                        |  </div>""".stripMargin
//  val ignition1 = Jsoup.parse(ignition1Html)
//
//  val ignition2Html = """<div>
//                        |<div class="frame BgIgnlist02"><!--イグニッションカード-->
//                        | <dl>
//                        |   <dt>カードリスト</dt>
//                        |   <dd class="cardImg"><img src="../images/cardlist/dammy/DW1-076.png" width="235" height="161" class="iepngfix"></dd>
//                        |     <dd class="cardNumber">DW1-076</dd>
//                        |     <dd class="charaName">0ガンダム（ロールアウトカラー）</dd>
//                        |     <dd class="wazaName">ゼロ・オー</dd>
//                        |     <dd class="spPower">5300</dd>
//                        |     <dd class="PlName">リボンズ・アルマーク</dd>
//                        |     <dd>
//                        |       <ul class="ignEffect">
//                        |         <li class="Skill">必殺制圧</li>
//                        |         <li class="txt">相手から先攻を奪って必殺技で攻撃する。更に相手の攻撃を封印する。発動条件：【3ラウンド】【1回限り】</li>
//                        |
//                        |         <li class="icon"><span class="hissatsu-seiatu"></span></li>
//                        |
//                        |       </ul>
//                        |     </dd>
//                        |     <dd>
//                        |       <ul class="ignPlSkill">
//                        |         <li class="Skill">イノベイドの指導者</li>
//                        |         <li class="txt">攻撃時、ずっと相手の必殺技を30%減少させる。【1回限り】</li>
//                        |       </ul>
//                        |     </dd>
//                        |     <dd class="PreaP">P</dd>
//                        |     <dd class="logo"><img src="../images/cardlist/tekketsu/parts/logo/logo_gundam-oo.png"></dd>
//                        |     </dl>
//                        |     <!--イグニッションEnd--></div>
//                        |   <!--プロモ-->
//                        |
//                        | </div>""".stripMargin
//
//  val ignition2 = Jsoup.parse(ignition2Html)
//
//  "ignition with no pilot skill" should "parse ignition1 html" in {
//
//    Parsers.extractIgnition(ignition1, false) match {
//      case Right(ig) =>
//        val basic = ig.basic
//        assertResult("DW1-075")(basic.cardNo)
//        assertResult("../images/cardlist/dammy/DW1-075.png")(basic.image)
//        assertResult("∀ガンダム")(basic.name)
//        assertResult(Some("C"))(basic.rarity)
//        assertResult("DW1")(basic.set)
//
//
//        assertResult(1200)(ig.special)
//        assertResult("ロラン・セアック,ソシエ・ハイム")(ig.pilotName)
//        assertResult("相手から先攻を奪って必殺技で攻撃する。更に相手の攻撃を封印する。発動条件：【1ラウンド】【1回限り】")(ig.effectText)
//        assertResult(None)(ig.pilotSkillText)
//      case Left(e) => throw e
//    }
//
//
//  }
//
//  "ignition with pilot skill" should "parse ignition2 html" in {
//    Parsers.extractIgnition(ignition2, true) match {
//      case Right(ig) =>
//        assertResult(Some("攻撃時、ずっと相手の必殺技を30%減少させる。【1回限り】"))(ig.pilotSkillText)
//      case Left(e) => throw e
//    }
//  }

  val newIgnitionHtml =
    """<div class="carddateCol ignCol">
      |                  <div class="dateCol sidewaysCol">
      |                    <div class="col_r">
      |                      <div class="info1col">
      |                        <dl class="date1col">
      |                          <dt>カードリスト</dt>
      |                          <dd class="cardNumber">OA1-076</dd>
      |                          <dd class="charaName">クロスボーン・ガンダムX1フルクロス</dd>
      |                          <dd class="wazaName">クロスボーン・ブラストスマッシャー</dd>
      |                          <dd class="spPower">5200</dd>
      |                        </dl>
      |                      </div>
      |
      |                      <div class="info2Col">
      |                        <dl>
      |                          <dt><img src="../images/cardlist/eb/ms/tit_pl.png" width="85" height="20" alt="パイロット"></dt>
      |                          <dd>トビア・アロナクス</dd>
      |                        </dl>
      |                      </div>
      |
      |                      <div class="info3Col MsAbiCol">
      |                        <dl>
      |                          <dt>
      |                            <p>必殺反撃</p>
      |                          </dt>
      |                          <dd>
      |                            <p>相手の必殺技に反撃する。発動条件：【1ラウンド】【1回限り】</p>
      |                          </dd>
      |                        </dl>
      |                        <ul class="MsAbi abiIcon">
      |                          <li class="icon"><span class="hissatsu-hangeki"></span></li>
      |                        </ul>
      |                      </div>
      |
      |                      <div class="info3Col PlAbiCol">
      |                        <dl>
      |                          <dt class="pl">
      |                            <p>不殺の意思</p>
      |                          </dt>
      |                          <dd>
      |                            <p>攻撃時、ずっと相手のアタックを30%減少させる。【1回限り】</p>
      |                          </dd>
      |                        </dl>
      |                      </div>
      |                                          </div>
      |
      |
      |                    <div class="col_l">
      |                      <div class="cardCol">
      |                        <img src="../images/cardlist/dammy/OA1-076.png" width="235" height="161" alt="クロスボーン・ガンダムX1フルクロス">
      |                      </div>
      |                      <div class="reaCol">
      |                        <dl>
      |                          <dt><img src="../images/cardlist/eb/boost/tit_rea.png" width="235" height="28" alt="レアリティ"></dt>
      |                          <dd>P</dd>
      |                        </dl>
      |                      </div>
      |                                                                  <div class="logoCol">
      |                        <img src="../images/cardlist/tekketsu/parts/logo/logo_x-bone_seven-of-steel.png">
      |                      </div>
      |                                          </div>
      |                  </div>
      |</div>
      |""".stripMargin

  val newIgnition = Jsoup.parse(newIgnitionHtml)
  "new ignition with pilot" should "parse" in {
    Parsers.extractIgnition(newIgnition) match {
      case Right(ig) =>
        assertResult(5200)(ig.special)
        assertResult("クロスボーン・ガンダムX1フルクロス")(ig.basic.name)
        assertResult("トビア・アロナクス")(ig.pilotName)
        assertResult("相手の必殺技に反撃する。発動条件：【1ラウンド】【1回限り】")(ig.effectText)
        assertResult(Some("不殺の意思"))(ig.pilotSkill)
        assertResult(Some("攻撃時、ずっと相手のアタックを30%減少させる。【1回限り】"))(ig.pilotSkillText)
      case Left(e) => throw e
    }

  }
  val newIgnitionHtml2 =
    """<div class="carddateCol ignCol">
      |                  <div class="dateCol sidewaysCol">
      |                    <div class="col_r">
      |                      <div class="info1col">
      |                        <dl class="date1col">
      |                          <dt>カードリスト</dt>
      |                          <dd class="cardNumber">OA1-077</dd>
      |                          <dd class="charaName">ゴトラタン</dd>
      |                          <dd class="wazaName">インサニティ・アンハーネス</dd>
      |                          <dd class="spPower">4600</dd>
      |                        </dl>
      |                      </div>
      |
      |                      <div class="info2Col">
      |                        <dl>
      |                          <dt><img src="../images/cardlist/eb/ms/tit_pl.png" width="85" height="20" alt="パイロット"></dt>
      |                          <dd>カテジナ・ルース</dd>
      |                        </dl>
      |                      </div>
      |
      |                      <div class="info3Col MsAbiCol">
      |                        <dl>
      |                          <dt>
      |                            <p>必殺追撃</p>
      |                          </dt>
      |                          <dd>
      |                            <p>必殺技で倒せなかった相手を追撃する。発動条件：【3ラウンド】【1回限り】</p>
      |                          </dd>
      |                        </dl>
      |                        <ul class="MsAbi abiIcon">
      |                          <li class="icon"><span class="hissatsu-tuigeki"></span></li>
      |                        </ul>
      |                      </div>
      |
      |                                          </div>
      |                    <div class="col_l">
      |                      <div class="cardCol">
      |                        <img src="../images/cardlist/dammy/OA1-077.png" width="235" height="161" alt="ゴトラタン">
      |                      </div>
      |                      <div class="reaCol">
      |                        <dl>
      |                          <dt><img src="../images/cardlist/eb/boost/tit_rea.png" width="235" height="28" alt="レアリティ"></dt>
      |                          <dd>C</dd>
      |                        </dl>
      |                      </div>
      |                                                                  <div class="logoCol">
      |                        <img src="../images/cardlist/tekketsu/parts/logo/logo_v-gundam.png">
      |                      </div>
      |                                          </div>
      |                  </div>
      |</div>
      |""".stripMargin

  val newIgnition2 = Jsoup.parse(newIgnitionHtml2)
  "new ignition" should "parse" in {
    Parsers.extractIgnition(newIgnition2) match {
      case Right(ig) =>
        assertResult("カテジナ・ルース")(ig.pilotName)
        assertResult("必殺技で倒せなかった相手を追撃する。発動条件：【3ラウンド】【1回限り】")(ig.effectText)
        assertResult(None)(ig.pilotSkill)
      case Left(e) => throw e
    }

  }
}
