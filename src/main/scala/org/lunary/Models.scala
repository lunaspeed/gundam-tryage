package org.lunary

import cats.data.NonEmptyList

import scala.collection.immutable.ListMap


object Models {


  implicit val cardOrdering = new Ordering[Card] {
    override def compare(x: Card, y: Card): Int = x.basic.cardNo.compareTo(y.basic.cardNo)
  }

  sealed trait Card {
    def basic: Basic
  }

  case class Basic(set: String, cardNo: String, name: String, rarity: Option[String], image: String)

  case class Attribute(hp: Int, power: Int, speed: Int)

  case class PowerRank(space: String, ground: String, water: Option[String], forest: Option[String], desert: Option[String])

  case class MobileSuit(basic: Basic,
                        pilotName: Option[String], attribute: Attribute, special: Int, cost: Int,
                        powerRank: PowerRank,
                        wazaName: String, mecName: Option[String], aceEffect: Option[String], abilities: NonEmptyList[String], text: String) extends Card

  case class Pilot(basic: Basic,
                   attribute: Attribute,
                   burstName: String, burstLevel: Int, burstType: String, aceEffect: Option[String],
                   skill: String, text: String) extends Card

  case class Ignition(basic: Basic,
                      wazaName: String, special: Int, pilotName: String,
                      effectSkill: String, effectText: String, pilotSkill: Option[String], pilotSkillText: Option[String]) extends Card

  case class UnknownType(basic: Basic, typeClasses: Set[String]) extends Card

  case class CategoryCards(mobileSuits: List[MobileSuit], pilots: List[Pilot], ignitions: List[Ignition], unknowns: List[UnknownType]) {
    def add(cc: CategoryCards): CategoryCards =
      CategoryCards(mobileSuits ++ cc.mobileSuits, pilots ++ cc.pilots, ignitions ++ cc.ignitions, unknowns ++ cc.unknowns)

    def sort(): CategoryCards = CategoryCards(mobileSuits.sorted(cardOrdering), pilots.sorted(cardOrdering), ignitions.sorted(cardOrdering), unknowns.sorted(cardOrdering))
  }
  val EmptyCategoryCards = CategoryCards(Nil, Nil, Nil, Nil)


  val oaSets = ListMap(
    "168015" -> "OPERATION ACE 5弾",
    "168014" -> "OPERATION ACE 4弾",
    "168013" -> "OPERATION ACE 3弾",
    "168012" -> "OPERATION ACE 2弾",
    "168011" -> "OPERATION ACE 1弾")

  val vsSets = ListMap(
    "168605" -> "VS IGNITION 5弾",
    "168604" -> "VS IGNITION 4弾",
    "168603" -> "VS IGNITION 3弾",
    "168602" -> "VS IGNITION 2弾",
    "168601" -> "VS IGNITION 1弾")
  val tkrSets = ListMap(
    "168505" -> "鉄華繚乱5弾",
    "168504" -> "鉄華繚乱4弾",
    "168503" -> "鉄華繚乱3弾",
    "168502" -> "鉄華繚乱2弾",
    "168501" -> "鉄華繚乱1弾"
  )
  val tkSets = ListMap(
    "168406" -> "鉄血の6弾",
    "168405" -> "鉄血の5弾",
    "168404" -> "鉄血の4弾",
    "168403" -> "鉄血の3弾",
    "168402" -> "鉄血の2弾",
    "168401" -> "鉄血の1弾"
  )
  val bgSets = ListMap(
    "168306" -> "BUILD G 6弾",
    "168305" -> "BUILD G 5弾",
    "168304" -> "BUILD G 4弾",
    "168303" -> "BUILD G 3弾",
    "168302" -> "BUILD G 2弾",
    "168301" -> "BUILD G 1弾"
  )

  val bSets = ListMap(
    "168208" -> "BUILD MS 8弾",
    "168207" -> "BUILD MS 7弾",
    "168206" -> "BUILD MS 6弾",
    "168205" -> "BUILD MS 5弾",
    "168204" -> "BUILD MS 4弾",
    "168203" -> "BUILD MS 3弾",
    "168202" -> "BUILD MS 2弾",
    "168201" -> "BUILD MS 1弾"
  )

  val zSets = ListMap(
    "168104" -> "ジオンの興亡 4弾",
    "168103" -> "ジオンの興亡 3弾",
    "168102" -> "ジオンの興亡 2弾",
    "168101" -> "ジオンの興亡 1弾"
  )

  val origSets = ListMap(
    "168006" -> "第6弾",
    "168005" -> "第5弾",
    "168004" -> "第4弾",
    "168003" -> "第3弾",
    "168002" -> "第2弾",
    "168001" -> "第1弾",
    "168000" -> "第0弾"
  )

  val promoSets = ListMap(
    "168701" -> "鉄血のオルフェンズ ブースターパック",
    "168906" -> "OPERATION ACE プロモーションカード",
    "168905" -> "VS IGNITION プロモーションカード",
    "168904" -> "鉄血弾 プロモーションカード",
    "168903" -> "BUILD G・BUILD MS プロモーションカード",
    "168902" -> "ジオンの興亡 プロモーションカード",
    "168901" -> "プロモーションカード"
  )

  //private val combinedSets = oaSets ++ vsSets ++ tkrSets ++ tkSets ++ bgSets ++ bSets ++ zSets ++ origSets ++ promoSets

  //  private val setGroups = "oa" -> oaSets :: "vs" -> vsSets :: "tkr" -> tkrSets :: "tk" -> tkSets :: "bg" -> bgSets :: "b" -> bSets :: "z" -> zSets :: "orig" -> origSets :: "promo" -> promoSets :: Nil

  val origSetsCht = ListMap(
    "448004" -> "第4弾",
    "448003" -> "第3弾",
    "448002" -> "第2弾",
    "448001" -> "第1弾"
  )

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

    val baseTitles: List[String]
    val mobileSuitTitles: List[String]
    val pilotTitles: List[String]
    val ignitionTitles: List[String]

    val burstAttack: String
    val burstDefence: String
    val burstSpeed: String
  }

  case object Japan extends Area {

    override val configName = "japan"
    override val combinedSets = oaSets ++ vsSets ++ tkrSets ++ tkSets ++ bgSets ++ bSets ++ zSets ++ origSets ++ promoSets
    override val setGroups = "oa" -> oaSets :: "vs" -> vsSets :: "tkr" -> tkrSets :: "tk" -> tkSets :: "bg" -> bgSets :: "b" -> bSets :: "z" -> zSets :: "orig" -> origSets :: "promo" -> promoSets :: Nil

    override val sheetNameMobileSuit = "モビルスーツ"
    override val sheetNamePilot = "パイロット"
    override val sheetNameIgnition = "イグニッション"

    override val baseTitles: List[String] = List("所有する", "弾", "カード番号", "レアリティ", "カード名", "画像")

    override val mobileSuitTitles = List("パイロット名",
      "ＨＰ", "アタック", "スピード", "必殺技", "必殺威力", "必殺コスト",
      "宇宙適性", "地上適性", "水中適性", "森林適性", "砂漠適性",
      "アビリティ名", "アビリティ", "ACE", "開発系統")

    override val pilotTitles: List[String] = List(
      "ＨＰ", "アタック", "スピード", "バースト", "バーストの種類", "バーストレベル",
      "スキル名", "スキル", "ACE")

    override val ignitionTitles: List[String] = List("パイロット名",
      "必殺技", "必殺威力",
      "効果名", "効果", "パイロットスキル名", "パイロットスキル")

    override val burstAttack: String = "アタック"
    override val burstDefence: String = "ディフェンス"
    override val burstSpeed: String = "スピード"
  }

  case object Asia extends Area {

    override val configName = "asia"

    override val combinedSets = origSetsCht
    override val setGroups = "orig" -> origSetsCht :: Nil

    override val sheetNameMobileSuit = "MS機體"
    override val sheetNamePilot = "駕駛員"
    override val sheetNameIgnition = "Ignition"

    override val baseTitles: List[String] = List("擁有", "弾數", "卡號", "稀有度", "卡名", "圖片")

    override val mobileSuitTitles = List("駕駛員名稱",
      "HP", "ATK", "SPD", "必殺技", "必殺威力", "必殺Cost",
      "宇宙適性", "地上適性", "水中適性", "森林適性", "砂漠適性",
      "MS機體能力名", "MS機體能力", "ACE", "開發系統")

    override val pilotTitles: List[String] = List(
      "HP", "ATK", "SPD", "爆發", "爆發種類", "爆發Lv",
      "駕駛員能力名", "駕駛員能力", "ACE")

    override val ignitionTitles: List[String] = List("駕駛員名",
      "必殺技", "必殺威力",
      "効果名", "効果", "駕駛員能力名", "駕駛員能力")

    override val burstAttack: String = "Attack"
    override val burstDefence: String = "Defence"
    override val burstSpeed: String = "Speed"
  }

}
