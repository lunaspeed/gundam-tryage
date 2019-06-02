package org.lunary

import org.jsoup.Jsoup
import org.scalatest.FlatSpec

class ParserIgnitionTest extends FlatSpec {

  val ignition1Html = """ <div>
                        |<div class="frame BgIgnlist"><!--イグニッションカード-->
                        |  <dl>
                        |    <dt>カードリスト</dt>
                        |    <dd class="cardImg"><img src="../images/cardlist/dammy/DW1-075.png" width="235" height="161" class="iepngfix"></dd>
                        |      <dd class="cardNumber">DW1-075</dd>
                        |      <dd class="charaName">∀ガンダム</dd>
                        |      <dd class="wazaName">月光蝶</dd>
                        |      <dd class="spPower">1200</dd>
                        |      <dd class="PlName">ロラン・セアック,ソシエ・ハイム</dd>
                        |      <dd>
                        |        <ul class="ignEffect">
                        |          <li class="Skill">必殺制圧</li>
                        |          <li class="txt">相手から先攻を奪って必殺技で攻撃する。更に相手の攻撃を封印する。発動条件：【1ラウンド】【1回限り】</li>
                        |
                        |          <li class="icon"><span class="hissatsu-seiatu"></span></li>
                        |
                        |        </ul>
                        |      </dd>
                        |      <dd class="PreaP">C</dd>
                        |      <dd class="logo"><img src="../images/cardlist/tekketsu/parts/logo/logo_turn-a.png"></dd>
                        |      </dl>
                        |      <!--イグニッションEnd--></div>
                        |    <!--プロモ-->
                        |
                        |  </div>""".stripMargin
  val ignition1 = Jsoup.parse(ignition1Html)

  val ignition2Html = """<div>
                        |<div class="frame BgIgnlist02"><!--イグニッションカード-->
                        | <dl>
                        |   <dt>カードリスト</dt>
                        |   <dd class="cardImg"><img src="../images/cardlist/dammy/DW1-076.png" width="235" height="161" class="iepngfix"></dd>
                        |     <dd class="cardNumber">DW1-076</dd>
                        |     <dd class="charaName">0ガンダム（ロールアウトカラー）</dd>
                        |     <dd class="wazaName">ゼロ・オー</dd>
                        |     <dd class="spPower">5300</dd>
                        |     <dd class="PlName">リボンズ・アルマーク</dd>
                        |     <dd>
                        |       <ul class="ignEffect">
                        |         <li class="Skill">必殺制圧</li>
                        |         <li class="txt">相手から先攻を奪って必殺技で攻撃する。更に相手の攻撃を封印する。発動条件：【3ラウンド】【1回限り】</li>
                        |
                        |         <li class="icon"><span class="hissatsu-seiatu"></span></li>
                        |
                        |       </ul>
                        |     </dd>
                        |     <dd>
                        |       <ul class="ignPlSkill">
                        |         <li class="Skill">イノベイドの指導者</li>
                        |         <li class="txt">攻撃時、ずっと相手の必殺技を30%減少させる。【1回限り】</li>
                        |       </ul>
                        |     </dd>
                        |     <dd class="PreaP">P</dd>
                        |     <dd class="logo"><img src="../images/cardlist/tekketsu/parts/logo/logo_gundam-oo.png"></dd>
                        |     </dl>
                        |     <!--イグニッションEnd--></div>
                        |   <!--プロモ-->
                        |
                        | </div>""".stripMargin

  val ignition2 = Jsoup.parse(ignition2Html)

  "ignition with no pilot skill" should "parse ignition1 html" in {

    val ig = Parsers.extractIgnition(ignition1, false)
    val basic = ig.basic
    assertResult("DW1-075")(basic.cardNo)
    assertResult("../images/cardlist/dammy/DW1-075.png")(basic.image)
    assertResult("∀ガンダム")(basic.name)
    assertResult(Some("C"))(basic.rarity)
    assertResult("DW1")(basic.set)


    assertResult(1200)(ig.special)
    assertResult("ロラン・セアック,ソシエ・ハイム")(ig.pilotName)
    assertResult("相手から先攻を奪って必殺技で攻撃する。更に相手の攻撃を封印する。発動条件：【1ラウンド】【1回限り】")(ig.effectText)
    assertResult(None)(ig.pilotSkillText)

  }

  "ignition with pilot skill" should "parse ignition2 html" in {

    val ig = Parsers.extractIgnition(ignition2, true)
    assertResult(Some("攻撃時、ずっと相手の必殺技を30%減少させる。【1回限り】"))(ig.pilotSkillText)

  }

}
