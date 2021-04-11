package org.lunary

import cats.data.NonEmptyList

import scala.collection.immutable.ListMap


object Models {

  sealed trait Card {
    def basic: Basic
  }

  case class Basic(set: String, cardNo: String, name: String, rarity: Option[String], image: String)

  case class Attribute(hp: Int, power: Int, speed: Int)

  case class PowerRank(space: String, ground: String, water: Option[String], forest: Option[String], desert: Option[String])

  case class MobileSuit(basic: Basic,
                        pilotName: Option[String], attribute: Attribute, special: Int, cost: Int,
                        powerRank: PowerRank,
                        wazaName: String, mecName: Option[String], aceEffect: Option[String], abilities: NonEmptyList[String], text: String,
                        transformed: Option[MobileSuit] = None,
                        promoInfo: Option[String] = None) extends Card

  case class Pilot(basic: Basic,
                   attribute: Attribute,
                   burstName: String, burstLevel: Int, burstType: String, aceEffect: Option[String],
                   skill: String, text: String, exAwaken: Option[ExAwaken] = None) extends Card

  case class ExAwaken(name: String, requirement: String)

  case class Ignition(basic: Basic,
                      wazaName: String, special: Int, pilotName: String,
                      effectSkill: String, effectText: String, pilotSkill: Option[String], pilotSkillText: Option[String]) extends Card

  case class Boost(basic: Basic,
                   effect: String, requirement: String,
                   lvl1Requirement: String, lvl1Effect: String,
                   lvl2Requirement: String, lvl2Effect: String,
                   lvl3Requirement: String, lvl3Effect: String) extends Card

  case class UnknownType(basic: Basic, typeClasses: Set[String]) extends Card

  val ebSets: ListMap[String, String] = ListMap(
    "168031" -> "EVOL BOOST!! 1弾",
    "168032" -> "EVOL BOOST!! 2弾",
    "168033" -> "EVOL BOOST!! 3弾",
    "168034" -> "EVOL BOOST!! 4弾",
    "168035" -> "EVOL BOOST!! 5弾"
  )

  val dwSets: ListMap[String, String] = ListMap(
    "168026" -> "DELTA WARS 6弾",
    "168025" -> "DELTA WARS 5弾",
    "168024" -> "DELTA WARS 4弾",
    "168023" -> "DELTA WARS 3弾",
    "168022" -> "DELTA WARS 2弾",
    "168021" -> "DELTA WARS 1弾"
  )

  val oaSets: ListMap[String, String] = ListMap(
    "168016" -> "OPERATION ACE 6弾",
    "168015" -> "OPERATION ACE 5弾",
    "168014" -> "OPERATION ACE 4弾",
    "168013" -> "OPERATION ACE 3弾",
    "168012" -> "OPERATION ACE 2弾",
    "168011" -> "OPERATION ACE 1弾")

  val vsSets: ListMap[String, String] = ListMap(
    "168605" -> "VS IGNITION 5弾",
    "168604" -> "VS IGNITION 4弾",
    "168603" -> "VS IGNITION 3弾",
    "168602" -> "VS IGNITION 2弾",
    "168601" -> "VS IGNITION 1弾")

  val tkrSets: ListMap[String, String] = ListMap(
    "168505" -> "鉄華繚乱5弾",
    "168504" -> "鉄華繚乱4弾",
    "168503" -> "鉄華繚乱3弾",
    "168502" -> "鉄華繚乱2弾",
    "168501" -> "鉄華繚乱1弾"
  )
  val tkSets: ListMap[String, String] = ListMap(
    "168406" -> "鉄血の6弾",
    "168405" -> "鉄血の5弾",
    "168404" -> "鉄血の4弾",
    "168403" -> "鉄血の3弾",
    "168402" -> "鉄血の2弾",
    "168401" -> "鉄血の1弾"
  )
  val bgSets: ListMap[String, String] = ListMap(
    "168306" -> "BUILD G 6弾",
    "168305" -> "BUILD G 5弾",
    "168304" -> "BUILD G 4弾",
    "168303" -> "BUILD G 3弾",
    "168302" -> "BUILD G 2弾",
    "168301" -> "BUILD G 1弾"
  )

  val bSets: ListMap[String, String] = ListMap(
    "168208" -> "BUILD MS 8弾",
    "168207" -> "BUILD MS 7弾",
    "168206" -> "BUILD MS 6弾",
    "168205" -> "BUILD MS 5弾",
    "168204" -> "BUILD MS 4弾",
    "168203" -> "BUILD MS 3弾",
    "168202" -> "BUILD MS 2弾",
    "168201" -> "BUILD MS 1弾"
  )

  val zSets: ListMap[String, String] = ListMap(
    "168104" -> "ジオンの興亡 4弾",
    "168103" -> "ジオンの興亡 3弾",
    "168102" -> "ジオンの興亡 2弾",
    "168101" -> "ジオンの興亡 1弾"
  )

  val origSets: ListMap[String, String] = ListMap(
    "168006" -> "第6弾",
    "168005" -> "第5弾",
    "168004" -> "第4弾",
    "168003" -> "第3弾",
    "168002" -> "第2弾",
    "168001" -> "第1弾",
    "168000" -> "第0弾"
  )

  val promoSets: ListMap[String, String] = ListMap(
    "168701" -> "鉄血のオルフェンズ ブースターパック",
    "168908" -> "EVOL BOOST プロモーションカード",
    "168907" -> "DELTA WARS プロモーションカード",
    "168906" -> "OPERATION ACE プロモーションカード",
    "168905" -> "VS IGNITION プロモーションカード",
    "168904" -> "鉄血弾 プロモーションカード",
    "168903" -> "BUILD G・BUILD MS プロモーションカード",
    "168902" -> "ジオンの興亡 プロモーションカード",
    "168901" -> "ガンダムトライエイジ プロモーションカード"
  )

  //private val combinedSets = oaSets ++ vsSets ++ tkrSets ++ tkSets ++ bgSets ++ bSets ++ zSets ++ origSets ++ promoSets

  //  private val setGroups = "oa" -> oaSets :: "vs" -> vsSets :: "tkr" -> tkrSets :: "tk" -> tkSets :: "bg" -> bgSets :: "b" -> bSets :: "z" -> zSets :: "orig" -> origSets :: "promo" -> promoSets :: Nil

  //  val origSetsCht: ListMap[String, String] = ListMap(
  //    "448004" -> "第4弾",
  //    "448003" -> "第3弾",
  //    "448002" -> "第2弾",
  //    "448001" -> "第1弾"
  //  )

  //  private val combinedSetsAsia = origSetsCht
  //
  //  private val setGroupsAsia = "orig" -> origSetsCht :: Nil


  //  val JAPAN_SETS = AreaSets(combinedSets, setGroups)
  //  val ASIA_SETS = AreaSets(combinedSetsAsia, setGroupsAsia)
  //
  //  case class AreaSets(combinedSets: ListMap[String, String], setGroups: List[(String, ListMap[String, String])])

  case class AreaConfig(domain: String, urlBase: String, searchUrl: String, filePrefix: String, area: Area)

  object AreaConfig {

    import com.typesafe.config.Config

    def apply(config: Config, area: Area): AreaConfig = AreaConfig(
      config.getString("domain"),
      config.getString("urlBase"),
      config.getString("searchUrl"),
      config.getString("filePrefix"),
      area)
  }

  sealed trait Area {

    val configName: String

    val combinedSets: ListMap[String, String]
    val setGroups: List[(String, ListMap[String, String])]

    val sheetNameMobileSuit: String
    val sheetNamePilot: String
    val sheetNameIgnition: String
    val sheetNameBoost: String

    val baseTitles: List[String]
    val mobileSuitTitles: List[String]
    val pilotTitles: List[String]
    val ignitionTitles: List[String]
    val boostTitles: List[String]

    val burstAttack: String
    val burstDefence: String
    val burstSpeed: String
  }

  case object Japan extends Area {

    override val configName = "japan"
    override val combinedSets: ListMap[String, String] = ebSets ++ dwSets ++ oaSets ++ vsSets ++ tkrSets ++ tkSets ++ bgSets ++ bSets ++ zSets ++ origSets ++ promoSets
    //    override val setGroups = "eb" -> ebSets :: "dw" -> dwSets :: "oa" -> oaSets :: "vs" -> vsSets :: "tkr" -> tkrSets :: "tk" -> tkSets :: "bg" -> bgSets :: "b" -> bSets :: "z" -> zSets :: "orig" -> origSets :: "promo" -> promoSets :: Nil
    override val setGroups: List[(String, ListMap[String, String])] = "eb" -> ebSets :: "promo" -> promoSets :: Nil

    override val sheetNameMobileSuit = "モビルスーツ"
    override val sheetNamePilot = "パイロット"
    override val sheetNameIgnition = "イグニッション"
    override val sheetNameBoost = "ブースト"

    override val baseTitles: List[String] = List("所有する", "弾", "カード番号", "レアリティ", "カード名", "画像")

    override val mobileSuitTitles = List("パイロット名",
      "ＨＰ", "アタック", "スピード", "必殺技", "必殺威力", "必殺コスト",
      "宇宙適性", "地上適性", "水中適性", "森林適性", "砂漠適性",
      "アビリティ名", "アビリティ", "ACE", "開発系統", "プロモ")

    override val pilotTitles: List[String] = List(
      "ＨＰ", "アタック", "スピード", "バースト", "バーストの種類", "バーストレベル",
      "スキル名", "スキル", "ACE", "EX覚醒", "EX覚醒条件")

    override val ignitionTitles: List[String] = List("パイロット名",
      "必殺技", "必殺威力",
      "効果名", "効果", "パイロットスキル名", "パイロットスキル")

    override val boostTitles: List[String] = List("ブースト効果", "ポイント獲得条件",
      "Lv1条件", "Lv1効果", "Lv2条件", "Lv2効果", "Lv3条件", "Lv3効果")

    override val burstAttack: String = "アタック"
    override val burstDefence: String = "ディフェンス"
    override val burstSpeed: String = "スピード"
  }


}
