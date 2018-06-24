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
                        wazaName: String, mecName: Option[String], aceEffect: Option[String], abilities: NonEmptyList[String], text: String) extends Card
  case class Pilot(basic: Basic,
                   attribute: Attribute,
                   burstName: String, burstLevel: Int, burstType: String, aceEffect: Option[String],
                   skill: String, text: String) extends Card
  case class Ignition(basic: Basic,
                      wazaName: String, special: Int, pilotName: String,
                      effectSkill: String, effectText: String, pilotSkill: Option[String], pilotSkillText: Option[String]) extends Card

  case class UnknownType(basic: Basic, typeClasses: Set[String]) extends Card


  val oaSets = ListMap("168011" -> "OPERATION ACE 1弾")
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

  val combinedSets = oaSets ++ vsSets ++ tkrSets ++ tkSets ++ bgSets ++ bSets ++ zSets ++ origSets ++ promoSets

  val setGroups = "oa" -> oaSets :: "vs" -> vsSets :: "tkr" -> tkrSets :: "tk" -> tkSets :: "bg" -> bgSets :: "b" -> bSets :: "z" -> zSets :: "orig" -> origSets :: "promo" -> promoSets :: Nil



}
