package org.lunary

import cats.data.NonEmptyList
import org.jsoup.Jsoup
import org.scalatest.flatspec.AnyFlatSpec

class ParsersMobileSuitTest extends AnyFlatSpec {

  val basicMsHtml =
    """<div>
    |  <div class="carddateCol mscardCol">
    |    <div class="dateCol">
    |      <div class="col_r">
    |        <div class="info1col">
    |          <dl class="date1col">
    |            <dt>カードリスト</dt>
    |            <dd class="cardNumber">DW1-001</dd>
    |            <dd class="charaName">ザクⅡ（量産型）</dd>
    |            <dd class="wazaName">ジオニック・タクティクス</dd>
    |            <dd class="statusCol">
    |              <ul class="status statusdateCol">
    |                <li class="hpPoint">2400</li>
    |                <li class="powerPoint">2800</li>
    |                <li class="spPoint">3100</li>
    |              </ul>
    |            </dd>
    |            <dd>
    |              <ul class="atack">
    |                <li class="spPower">4500</li>
    |                <li class="Cost">4</li>
    |              </ul>
    |            </dd>
    |            <dd class="placeCol">
    |              <ul class="place">
    |                <li class="pSpace">○</li>
    |                <li class="pGrand">○</li>
    |                <li class="p3">×</li>
    |                <li class="p4">○</li>
    |                <li class="p5">×</li>
    |              </ul>
    |            </dd>
    |            <dd class="MecName">ジオニック社</dd>
    |          </dl>
    |        </div>
    |        <div class="info2Col">
    |          <dl>
    |            <dt><img src="../images/cardlist/dw/ms/tit_pl.png" width="85" height="20" alt="パイロット"></dt>
    |            <dd>-</dd>
    |          </dl>
    |                                </div>
    |        <div class="info3Col MsAbiCol">
    |          <dl>
    |            <dt>
    |              <p>逆境</p>
    |            </dt>
    |            <dd>
    |              <p>【HPが相手以下】相手の残りHP量に応じてダメージがアップする。                              </p>
    |            </dd>
    |          </dl>
    |          <ul class="MsAbi abiIcon">
    |          <li class="icon">                        <span class="gyakkyou"></span>
    |          </li>
    |          </ul>
    |        </div>
    |                            </div>
    |      <div class="col_l">
    |        <div class="cardCol">
    |          <img src="../images/cardlist/dammy/DW1-001.png" width="161" height="235" alt="ザクⅡ（量産型）">
    |        </div>
    |        <div class="reaCol">
    |          <dl>
    |            <dt><img src="../images/cardlist/dw/ms/tit_rea.png" width="160" height="28" alt="レアリティ"></dt>
    |            <dd>R</dd>
    |          </dl>
    |        </div>
    |                                                    <div class="logoCol">
    |        <img src="../images/cardlist/tekketsu/parts/logo/logo_gundam.png">
    |        </div>
    |                            </div>
    |    </div>
    |                                      </div>
    |</div>
  """.stripMargin

  final val basicMs = Jsoup.parse(basicMsHtml)

  val anniMsHtml =
    """<div>
     |   <div class="carddateCol mscardCol">
     |     <div class="dateCol">
     |       <div class="col_r">
     |         <div class="info1col">
     |           <dl class="date1col">
     |             <dt>カードリスト</dt>
     |             <dd class="cardNumber">DW1-095</dd>
     |             <dd class="charaName">ガンダムエクシア</dd>
     |             <dd class="wazaName">トランザム・セブンソード</dd>
     |             <dd class="statusCol">
     |               <ul class="status statusdateCol">
     |                 <li class="hpPoint">2600</li>
     |                 <li class="powerPoint">3300</li>
     |                 <li class="spPoint">3000</li>
     |               </ul>
     |             </dd>
     |             <dd>
     |               <ul class="atack">
     |                 <li class="spPower">6300</li>
     |                 <li class="Cost">6</li>
     |               </ul>
     |             </dd>
     |             <dd class="placeCol">
     |               <ul class="place">
     |                 <li class="pSpace">○</li>
     |                 <li class="pGrand">◎</li>
     |                 <li class="p3">▲</li>
     |                 <li class="p4">○</li>
     |                 <li class="p5">▲</li>
     |               </ul>
     |             </dd>
     |             <dd class="MecName">ソレスタルビーイング</dd>
     |           </dl>
     |         </div>
     |         <div class="info2Col">
     |           <dl>
     |             <dt><img src="../images/cardlist/dw/ms/tit_pl.png" width="85" height="20" alt="パイロット"></dt>
     |             <dd>刹那・F・セイエイ</dd>
     |           </dl>
     |                                 </div>
     |         <div class="info3Col MsAbiCol">
     |           <dl>
     |             <dt>
     |               <p>共撃/トランザム</p>
     |             </dt>
     |             <dd>
     |               <p>【一定確率】1対1のとき、味方がアシストとしてバトルに参加する。【一定確率】カードをこすってトランザム! 相手の攻撃を回避した後、反撃する。                              </p>
     |             </dd>
     |           </dl>
     |           <ul class="MsAbi abiIcon">
     |           <li class="icon">                        <span class="kyougeki-transam"></span>
     |           </li>
     |           </ul>
     |         </div>
     |                             </div>
     |       <div class="col_l">
     |         <div class="cardCol">
     |           <img src="../images/cardlist/dammy/DW1-095.png" width="161" height="235" alt="ガンダムエクシア">
     |         </div>
     |         <div class="reaCol">
     |           <dl>
     |             <dt><img src="../images/cardlist/dw/ms/tit_rea.png" width="160" height="28" alt="レアリティ"></dt>
     |             <dd>ANNIV.</dd>
     |           </dl>
     |         </div>
     |                                                     <div class="logoCol">
     |         <img src="../images/cardlist/tekketsu/parts/logo/logo_gundam-oo.png">
     |         </div>
     |                             </div>
     |     </div>
     |                                       </div>
     |</div> """

  final val anniMs = Jsoup.parse(anniMsHtml)

  val dualMsHtml =
    """<div>
      |  <div class="carddateCol mscardCol">
      |    <div class="dateCol">
      |      <div class="col_r">
      |        <div class="info1col">
      |          <dl class="date1col">
      |            <dt>カードリスト</dt>
      |            <dd class="cardNumber">DW1-002</dd>
      |            <dd class="charaName">陸戦型ガンダム</dd>
      |            <dd class="wazaName">ワンエイティ・ブレイズ</dd>
      |            <dd class="statusCol">
      |              <ul class="status statusdateCol">
      |                <li class="hpPoint">3900</li>
      |                <li class="powerPoint">2300</li>
      |                <li class="spPoint">2200</li>
      |              </ul>
      |            </dd>
      |            <dd>
      |              <ul class="atack">
      |                <li class="spPower">4500</li>
      |                <li class="Cost">4</li>
      |              </ul>
      |            </dd>
      |            <dd class="placeCol">
      |              <ul class="place">
      |                <li class="pSpace">×</li>
      |                <li class="pGrand">★</li>
      |                <li class="p3">▲</li>
      |                <li class="p4">◎</li>
      |                <li class="p5">○</li>
      |              </ul>
      |            </dd>
      |            <dd class="MecName">RXシリーズ</dd>
      |          </dl>
      |        </div>
      |        <div class="info2Col">
      |          <dl>
      |            <dt><img src="../images/cardlist/dw/ms/tit_pl.png" width="85" height="20" alt="パイロット"></dt>
      |            <dd>シロー・アマダ,カレン・ジョシュワ,テリー・サンダースJr.</dd>
      |          </dl>
      |        </div>
      |        <div class="info3Col MsAbiCol">
      |          <dl>
      |            <dt>
      |              <p>鉄壁</p>
      |            </dt>
      |            <dd>
      |              <p>【一定確率,1回限り】味方のダメージを代わりに受け止める。受けるダメージは少なくなる。                              </p>
      |            </dd>
      |          </dl>
      |          <ul class="MsAbi abiIcon">
      |          <li class="icon">                        <span class="teppeki"></span>
      |          </li>
      |          </ul>
      |        </div>
      |                              <img src="../images/cardlist/dw/ms/v_arw.png" width="580" height="44" alt="">
      |        <div class="info1col_v">
      |          <dl class="date1col">
      |            <dt>カードリスト</dt>
      |            <dd class="cardNumber">DW1-002</dd>
      |            <dd class="charaName">ガンダムEz8</dd>
      |            <dd class="wazaName">ダブル・リターンファイア</dd>
      |            <dd class="statusCol statusdateCol">
      |              <ul class="status">
      |                <li class="hpPoint">3900</li>
      |                <li class="powerPoint">2700</li>
      |                <li class="spPoint">1800</li>
      |              </ul>
      |            </dd>
      |            <dd>
      |              <ul class="atack">
      |                <li class="spPower">5300</li>
      |                <li class="Cost">5</li>
      |              </ul>
      |            </dd>
      |            <dd class="placeCol">
      |              <ul class="place">
      |                <li class="pSpace">×</li>
      |                <li class="pGrand">★</li>
      |                <li class="p3">▲</li>
      |                <li class="p4">◎</li>
      |                <li class="p5">◎</li>
      |              </ul>
      |            </dd>
      |            <dd class="MecName">RXシリーズ</dd>
      |          </dl>
      |        </div>
      |        <div class="info2Col">
      |          <dl>
      |            <dt><img src="../images/cardlist/dw/ms/tit_pl.png" width="85" height="20" alt="パイロット"></dt>
      |            <dd>シロー・アマダ</dd>
      |          </dl>
      |        </div>
      |        <div class="info3Col MsAbiCol">
      |          <dl>
      |            <dt>
      |              <p>反撃</p>
      |            </dt>
      |            <dd>
      |              <p>【一定確率】相手の攻撃を回避した後、反撃する。</p>
      |            </dd>
      |          </dl>
      |          <ul class="MsAbi abiIcon">
      |            <li class="icon">                        <span class="hangeki"></span>
      |          </li>
      |          </ul>
      |        </div>
      |      </div>
      |      <div class="col_l">
      |        <div class="cardCol">
      |          <img src="../images/cardlist/dammy/DW1-002.png" width="161" height="235" alt="陸戦型ガンダム">
      |        </div>
      |        <div class="reaCol">
      |          <dl>
      |            <dt><img src="../images/cardlist/dw/ms/tit_rea.png" width="160" height="28" alt="レアリティ"></dt>
      |            <dd>R</dd>
      |          </dl>
      |        </div>
      |                                                    <div class="logoCol">
      |        <img src="../images/cardlist/tekketsu/parts/logo/logo_08ms.png">
      |        </div>
      |                            </div>
      |    </div>
      |                                      </div>
      |</div>
    """.stripMargin

  final val dualMs = Jsoup.parse(dualMsHtml)

  val aceMsHtml = """<div>
                    |  <div class="carddateCol mscardCol">
                    |    <div class="dateCol">
                    |      <div class="col_r">
                    |        <div class="info1col">
                    |          <dl class="date1col">
                    |            <dt>カードリスト</dt>
                    |            <dd class="cardNumber">DW1-004</dd>
                    |            <dd class="charaName">ガンダムNT-1</dd>
                    |            <dd class="wazaName">アレックス・ブレイヴ</dd>
                    |            <dd class="statusCol">
                    |              <ul class="status statusdateCol">
                    |                <li class="hpPoint">1300</li>
                    |                <li class="powerPoint">3500</li>
                    |                <li class="spPoint">2500</li>
                    |              </ul>
                    |            </dd>
                    |            <dd>
                    |              <ul class="atack">
                    |                <li class="spPower">3400</li>
                    |                <li class="Cost">3</li>
                    |              </ul>
                    |            </dd>
                    |            <dd class="placeCol">
                    |              <ul class="place">
                    |                <li class="pSpace">○</li>
                    |                <li class="pGrand">○</li>
                    |                <li class="p3">▲</li>
                    |                <li class="p4">○</li>
                    |                <li class="p5">▲</li>
                    |              </ul>
                    |            </dd>
                    |            <dd class="MecName">RXシリーズ</dd>
                    |          </dl>
                    |        </div>
                    |        <div class="info2Col">
                    |          <dl>
                    |            <dt><img src="../images/cardlist/dw/ms/tit_pl.png" width="85" height="20" alt="パイロット"></dt>
                    |            <dd>クリスチーナ・マッケンジー</dd>
                    |          </dl>
                    |                                  <dl>
                    |            <dt><img src="../images/cardlist/dw/common/tit_ace.png" width="85" height="20" alt="エース効果"></dt>
                    |            <dd>ラウンド2からずっと受けるダメージ-500。</dd>
                    |          </dl>
                    |                                </div>
                    |        <div class="info3Col MsAbiCol">
                    |          <dl>
                    |            <dt>
                    |              <p>捨身</p>
                    |            </dt>
                    |            <dd>
                    |              <p>【HP50%以下,一定確率,1回限り】強力な攻撃を行う。攻撃後、自機のHPが10になる。                              </p>
                    |            </dd>
                    |          </dl>
                    |          <ul class="MsAbi abiIcon">
                    |          <li class="icon">                        <span class="desperation"></span>
                    |          </li>
                    |          </ul>
                    |        </div>
                    |                            </div>
                    |      <div class="col_l">
                    |        <div class="cardCol">
                    |          <img src="../images/cardlist/dammy/DW1-004.png" width="161" height="235" alt="ガンダムNT-1">
                    |        </div>
                    |        <div class="reaCol">
                    |          <dl>
                    |            <dt><img src="../images/cardlist/dw/ms/tit_rea.png" width="160" height="28" alt="レアリティ"></dt>
                    |            <dd>C</dd>
                    |          </dl>
                    |        </div>
                    |                                                    <div class="logoCol">
                    |        <img src="../images/cardlist/tekketsu/parts/logo/logo_pocket.png">
                    |        </div>
                    |                            </div>
                    |    </div>
                    |                                      </div>
                    |</div>"""
  final val aceMs = Jsoup.parse(aceMsHtml)


  "basic mobile suite information" should "be parsed from MS html" in {

    Parsers.extractMobileSuit(basicMs) match {
      case Right(ms) =>
        val basic = ms.basic
        assertResult("DW1-001")(basic.cardNo)
        assertResult("../images/cardlist/dammy/DW1-001.png")(basic.image)
        assertResult("ザクⅡ（量産型）")(basic.name)
        assertResult(Some("R"))(basic.rarity)
        assertResult("DW1")(basic.set)


        assertResult(None)(ms.aceEffect)

        assertResult(2400)(ms.attribute.hp)
        assertResult(4)(ms.cost)
        assertResult(Some("ジオニック社"))(ms.mecName)
        assertResult(Some("-"))(ms.pilotName)
        assertResult("○")(ms.powerRank.space)
        assertResult("○")(ms.powerRank.ground)
        assertResult(Some("×"))(ms.powerRank.desert)
        assertResult(Some("○"))(ms.powerRank.forest)
        assertResult(Some("×"))(ms.powerRank.water)

        assertResult(4500)(ms.special)
        assertResult(NonEmptyList("逆境", Nil))(ms.abilities)
        assertResult("【HPが相手以下】相手の残りHP量に応じてダメージがアップする。")(ms.text)
        assertResult(None)(ms.transformed)
      case Left(e) => throw e
    }
  }

  "anniversary mobile suite information" should "be parsed from MS html" in {

    Parsers.extractMobileSuit(anniMs) match {
      case Right(ms) =>
        assertResult(Some("ANNIV."))(ms.basic.rarity)
        assertResult(NonEmptyList("共撃", List("トランザム")))(ms.abilities)
        assertResult("【一定確率】1対1のとき、味方がアシストとしてバトルに参加する。【一定確率】カードをこすってトランザム! 相手の攻撃を回避した後、反撃する。")(ms.text)
      case Left(e) => throw e
    }
  }

  "dual mobile suite information" should "be parsed from MS html" in {

    Parsers.extractMobileSuit(dualMs) match {
      case Right(ms) =>
        assertResult(None)(ms.aceEffect)
        assertResult("陸戦型ガンダム")(ms.basic.name)
        assertResult(true)(ms.transformed.isDefined)
        val t = ms.transformed.get

        assertResult("ガンダムEz8")(t.basic.name)
        assertResult(None)(t.transformed)
        assertResult("../images/cardlist/dammy/DW1-002.png")(t.basic.image)
//        println(ms)
      case Left(e) => throw e
    }
  }

  "ace mobile suite information" should "be parsed from MS html" in {
    Parsers.extractMobileSuit(aceMs) match {
      case Right(ms) =>
        assertResult(Some("ラウンド2からずっと受けるダメージ-500。"))(ms.aceEffect)
//        println(ms)
      case Left(e) => throw e
    }
  }

  val newMsHtml =
    """
      |<div class="carddateCol mscardCol">
      |                  <div class="dateCol">
      |                    <div class="col_r">
      |                      <div class="info1col">
      |                        <dl class="date1col">
      |                          <dt>カードリスト</dt>
      |                          <dd class="cardNumber">EB1-038</dd>
      |                          <dd class="charaName">モビルドールメイ</dd>
      |                          <dd class="wazaName">モビルアーツ・メイルストロム</dd>
      |                          <dd class="statusCol">
      |                            <ul class="status statusdateCol">
      |                              <li class="hpPoint">3100</li>
      |                              <li class="powerPoint">2900</li>
      |                              <li class="spPoint">4800</li>
      |                            </ul>
      |                          </dd>
      |                          <dd>
      |                            <ul class="atack">
      |                              <li class="spPower">8300</li>
      |                              <li class="Cost">8</li>
      |                            </ul>
      |                          </dd>
      |                          <dd class="placeCol">
      |                            <ul class="place">
      |                              <li class="pSpace">○</li>
      |                              <li class="pGrand">◎</li>
      |                              <li class="p3">▲</li>
      |                              <li class="p4">○</li>
      |                              <li class="p5">×</li>
      |                            </ul>
      |                          </dd>
      |                          <dd class="MecName">ビルドダイバーズ</dd>
      |                        </dl>
      |                      </div>
      |                      <div class="info2Col">
      |                        <dl>
      |                          <dt><img src="../images/cardlist/eb/ms/tit_pl.png" width="85" height="20" alt="パイロット"></dt>
      |                          <dd>メイ</dd>
      |                        </dl>
      |                                                <dl>
      |                          <dt><img src="../images/cardlist/eb/common/tit_ace.png" width="85" height="20" alt="エース効果"></dt>
      |                          <dd>ラウンド2からずっと仲間全員のスピード、必殺技+1500。</dd>
      |                        </dl>
      |                                              </div>
      |                      <div class="info3Col MsAbiCol">
      |                        <dl>
      |                          <dt>
      |                            <p>激戦</p>
      |                          </dt>
      |                          <dd>
      |                            <p>攻撃するたびにクリティカルダメージとクリティカル発生率がアップする。更に相手の復活系と防御系アビリティを封じる。                              </p>
      |                          </dd>
      |                        </dl>
      |                        <ul class="MsAbi abiIcon">
      |                        <li class="icon">                        <span class="gekisen"></span>
      |                        </li>
      |                        </ul>
      |                      </div>
      |                                          </div>
      |                    <div class="col_l">
      |                      <div class="cardCol">
      |                        <img src="../images/cardlist/dammy/EB1-038.png" width="161" height="235" alt="モビルドールメイ">
      |                      </div>
      |                      <div class="reaCol">
      |                        <dl>
      |                          <dt><img src="../images/cardlist/eb/ms/tit_rea.png" width="160" height="28" alt="レアリティ"></dt>
      |                          <dd>P</dd>
      |                        </dl>
      |                      </div>
      |                                                                  <div class="logoCol">
      |                      <img src="../images/cardlist/tekketsu/parts/logo/logo_bdre.png">
      |                      </div>
      |                                          </div>
      |                  </div>
      |                                                    </div>
      |""".stripMargin
  final val newMs = Jsoup.parse(newMsHtml)

  "new mobile suite information" should "be parsed from MS html" in {
    Parsers.extractMobileSuit(newMs) match {
      case Right(ms) =>
        assertResult(Some("ラウンド2からずっと仲間全員のスピード、必殺技+1500。"))(ms.aceEffect)
//        println(ms)
      case Left(e) => throw e
    }
  }

  val promoMsHtml = """ <div class="carddateCol mscardCol">
                      | <div class="dateCol">
                      |  <div class="col_r">
                      |   <div class="info1col">
                      |    <dl class="date1col">
                      |     <dt>
                      |      カードリスト
                      |     </dt>
                      |     <dd class="cardNumber">
                      |      DPR-001
                      |     </dd>
                      |     <dd class="charaName">
                      |      ガンダムエクシア
                      |     </dd>
                      |     <dd class="wazaName">
                      |      トランザム・セブンソード
                      |     </dd>
                      |     <dd class="statusCol">
                      |      <ul class="status statusdateCol">
                      |       <li class="hpPoint">3900</li>
                      |       <li class="powerPoint">3200</li>
                      |       <li class="spPoint">1400</li>
                      |      </ul>
                      |     </dd>
                      |     <dd>
                      |      <ul class="atack">
                      |       <li class="spPower">5300</li>
                      |       <li class="Cost">5</li>
                      |      </ul>
                      |     </dd>
                      |     <dd class="placeCol">
                      |      <ul class="place">
                      |       <li class="pSpace">○</li>
                      |       <li class="pGrand">◎</li>
                      |       <li class="p3">▲</li>
                      |       <li class="p4">○</li>
                      |       <li class="p5">▲</li>
                      |      </ul>
                      |     </dd>
                      |     <dd class="MecName">
                      |      ソレスタルビーイング
                      |     </dd>
                      |    </dl>
                      |   </div>
                      |   <div class="info2Col">
                      |    <dl>
                      |     <dt>
                      |      <img src="../images/cardlist/eb/ms/tit_pl.png" width="85" height="20" alt="パイロット">
                      |     </dt>
                      |     <dd>
                      |      刹那・F・セイエイ
                      |     </dd>
                      |    </dl>
                      |   </div>
                      |   <div class="info3Col MsAbiCol">
                      |    <dl>
                      |     <dt>
                      |      <p>トランザム</p>
                      |     </dt>
                      |     <dd>
                      |      <p>【一定確率】カードをこすってトランザム! 相手の攻撃を回避した後、反撃する。 </p>
                      |     </dd>
                      |    </dl>
                      |    <ul class="MsAbi abiIcon">
                      |     <li class="icon"> <span class="transam"></span> </li>
                      |    </ul>
                      |   </div>
                      |   <img src="../images/cardlist/eb/ms/v_arw.png" width="580" height="44" alt="">
                      |   <div class="info1col_v">
                      |    <dl class="date1col">
                      |     <dt>
                      |      カードリスト
                      |     </dt>
                      |     <dd class="cardNumber">
                      |      DPR-001
                      |     </dd>
                      |     <dd class="charaName">
                      |      ガンダムエクシアリペア
                      |     </dd>
                      |     <dd class="wazaName">
                      |      ヴァルネッド・ソードダンサー
                      |     </dd>
                      |     <dd class="statusCol statusdateCol">
                      |      <ul class="status">
                      |       <li class="hpPoint">3900</li>
                      |       <li class="powerPoint">2600</li>
                      |       <li class="spPoint">2000</li>
                      |      </ul>
                      |     </dd>
                      |     <dd>
                      |      <ul class="atack">
                      |       <li class="spPower">5800</li>
                      |       <li class="Cost">6</li>
                      |      </ul>
                      |     </dd>
                      |     <dd class="placeCol">
                      |      <ul class="place">
                      |       <li class="pSpace">○</li>
                      |       <li class="pGrand">◎</li>
                      |       <li class="p3">▲</li>
                      |       <li class="p4">○</li>
                      |       <li class="p5">▲</li>
                      |      </ul>
                      |     </dd>
                      |     <dd class="MecName">
                      |      ソレスタルビーイング
                      |     </dd>
                      |    </dl>
                      |   </div>
                      |   <div class="info2Col">
                      |    <dl>
                      |     <dt>
                      |      <img src="../images/cardlist/eb/ms/tit_pl.png" width="85" height="20" alt="パイロット">
                      |     </dt>
                      |     <dd>
                      |      刹那・F・セイエイ
                      |     </dd>
                      |    </dl>
                      |   </div>
                      |   <div class="info3Col MsAbiCol">
                      |    <dl>
                      |     <dt>
                      |      <p>奮迅</p>
                      |     </dt>
                      |     <dd>
                      |      <p>【HP0,一定確率,1回限り】機能停止を回避して、必殺技で反撃する。更に相手が必殺技の場合は発動率アップ。</p>
                      |     </dd>
                      |    </dl>
                      |    <ul class="MsAbi abiIcon">
                      |     <li class="icon"> <span class="hunjin"></span> </li>
                      |    </ul>
                      |   </div>
                      |  </div>
                      |  <div class="col_l">
                      |   <div class="cardCol">
                      |    <img src="../images/cardlist/dammy/DPR-001.png" width="161" height="235" alt="ガンダムエクシア">
                      |   </div>
                      |   <div class="reaCol">
                      |    <dl>
                      |     <dt>
                      |      <img src="../images/cardlist/eb/ms/tit_rea.png" width="160" height="28" alt="レアリティ">
                      |     </dt>
                      |     <dd>
                      |      -
                      |     </dd>
                      |    </dl>
                      |   </div>
                      |   <div class="logoCol">
                      |    <img src="../images/cardlist/tekketsu/parts/logo/logo_gundam-oo.png">
                      |   </div>
                      |  </div>
                      | </div>
                      | <div class="promCol">
                      |  DELTA WARS 01稼働記念 ヴァリアブルカード出撃キャンペーン
                      | </div>
                      |</div>
                      |""".stripMargin
  final val promoMs = Jsoup.parse(promoMsHtml)
  "promo mobile suite information" should "be parsed from MS html" in {
    Parsers.extractMobileSuit(promoMs) match {
      case Right(ms) =>
        assertResult(None)(ms.basic.rarity)
        assertResult(Some("DELTA WARS 01稼働記念 ヴァリアブルカード出撃キャンペーン"))(ms.promoInfo)
      case Left(e) => throw e
    }
  }

}
