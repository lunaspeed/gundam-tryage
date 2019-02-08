package org.lunary.sink
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  import slick.collection.heterogeneous._
  import slick.collection.heterogeneous.syntax._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Ignitions.schema ++ MobileSuits.schema ++ Pilots.schema ++ UnknownTypes.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Ignitions
   *  @param area Database column area SqlType(VARCHAR), Length(10,true)
   *  @param setCode Database column set_code SqlType(VARCHAR), Length(5,true)
   *  @param cardNo Database column card_no SqlType(VARCHAR), Length(10,true)
   *  @param name Database column name SqlType(VARCHAR), Length(100,true)
   *  @param rarity Database column rarity SqlType(VARCHAR), Length(5,true), Default(None)
   *  @param image Database column image SqlType(VARCHAR), Length(100,true)
   *  @param wazaName Database column waza_name SqlType(VARCHAR), Length(100,true)
   *  @param special Database column special SqlType(INT)
   *  @param pilotName Database column pilot_name SqlType(VARCHAR), Length(100,true)
   *  @param effectSkill Database column effect_skill SqlType(VARCHAR), Length(200,true)
   *  @param effectText Database column effect_text SqlType(VARCHAR), Length(200,true)
   *  @param pilotSkill Database column pilot_skill SqlType(VARCHAR), Length(200,true), Default(None)
   *  @param pilotSkillText Database column pilot_skill_text SqlType(VARCHAR), Length(500,true), Default(None) */
  case class IgnitionsRow(area: String, setCode: String, cardNo: String, name: String, rarity: Option[String] = None, image: String, wazaName: String, special: Int, pilotName: String, effectSkill: String, effectText: String, pilotSkill: Option[String] = None, pilotSkillText: Option[String] = None)
  /** GetResult implicit for fetching IgnitionsRow objects using plain SQL queries */
  implicit def GetResultIgnitionsRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Int]): GR[IgnitionsRow] = GR{
    prs => import prs._
    IgnitionsRow.tupled((<<[String], <<[String], <<[String], <<[String], <<?[String], <<[String], <<[String], <<[Int], <<[String], <<[String], <<[String], <<?[String], <<?[String]))
  }
  /** Table description of table ignitions. Objects of this class serve as prototypes for rows in queries. */
  class Ignitions(_tableTag: Tag) extends profile.api.Table[IgnitionsRow](_tableTag, Some("gundam"), "ignitions") {
    def * = (area, setCode, cardNo, name, rarity, image, wazaName, special, pilotName, effectSkill, effectText, pilotSkill, pilotSkillText) <> (IgnitionsRow.tupled, IgnitionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(area), Rep.Some(setCode), Rep.Some(cardNo), Rep.Some(name), rarity, Rep.Some(image), Rep.Some(wazaName), Rep.Some(special), Rep.Some(pilotName), Rep.Some(effectSkill), Rep.Some(effectText), pilotSkill, pilotSkillText)).shaped.<>({r=>import r._; _1.map(_=> IgnitionsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12, _13)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column area SqlType(VARCHAR), Length(10,true) */
    val area: Rep[String] = column[String]("area", O.Length(10,varying=true))
    /** Database column set_code SqlType(VARCHAR), Length(5,true) */
    val setCode: Rep[String] = column[String]("set_code", O.Length(5,varying=true))
    /** Database column card_no SqlType(VARCHAR), Length(10,true) */
    val cardNo: Rep[String] = column[String]("card_no", O.Length(10,varying=true))
    /** Database column name SqlType(VARCHAR), Length(100,true) */
    val name: Rep[String] = column[String]("name", O.Length(100,varying=true))
    /** Database column rarity SqlType(VARCHAR), Length(5,true), Default(None) */
    val rarity: Rep[Option[String]] = column[Option[String]]("rarity", O.Length(5,varying=true), O.Default(None))
    /** Database column image SqlType(VARCHAR), Length(100,true) */
    val image: Rep[String] = column[String]("image", O.Length(100,varying=true))
    /** Database column waza_name SqlType(VARCHAR), Length(100,true) */
    val wazaName: Rep[String] = column[String]("waza_name", O.Length(100,varying=true))
    /** Database column special SqlType(INT) */
    val special: Rep[Int] = column[Int]("special")
    /** Database column pilot_name SqlType(VARCHAR), Length(100,true) */
    val pilotName: Rep[String] = column[String]("pilot_name", O.Length(100,varying=true))
    /** Database column effect_skill SqlType(VARCHAR), Length(200,true) */
    val effectSkill: Rep[String] = column[String]("effect_skill", O.Length(200,varying=true))
    /** Database column effect_text SqlType(VARCHAR), Length(200,true) */
    val effectText: Rep[String] = column[String]("effect_text", O.Length(200,varying=true))
    /** Database column pilot_skill SqlType(VARCHAR), Length(200,true), Default(None) */
    val pilotSkill: Rep[Option[String]] = column[Option[String]]("pilot_skill", O.Length(200,varying=true), O.Default(None))
    /** Database column pilot_skill_text SqlType(VARCHAR), Length(500,true), Default(None) */
    val pilotSkillText: Rep[Option[String]] = column[Option[String]]("pilot_skill_text", O.Length(500,varying=true), O.Default(None))

    /** Primary key of Ignitions (database name ignitions_PK) */
    val pk = primaryKey("ignitions_PK", (area, cardNo))
  }
  /** Collection-like TableQuery object for table Ignitions */
  lazy val Ignitions = new TableQuery(tag => new Ignitions(tag))

  /** Entity class storing rows of table MobileSuits
   *  @param area Database column area SqlType(VARCHAR), Length(10,true)
   *  @param setCode Database column set_code SqlType(VARCHAR), Length(5,true)
   *  @param cardNo Database column card_no SqlType(VARCHAR), Length(10,true)
   *  @param name Database column name SqlType(VARCHAR), Length(100,true)
   *  @param rarity Database column rarity SqlType(VARCHAR), Length(5,true), Default(None)
   *  @param image Database column image SqlType(VARCHAR), Length(100,true)
   *  @param pilotName Database column pilot_name SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param attrHp Database column attr_hp SqlType(SMALLINT)
   *  @param attrPower Database column attr_power SqlType(SMALLINT)
   *  @param attrSpeed Database column attr_speed SqlType(SMALLINT)
   *  @param special Database column special SqlType(INT)
   *  @param cost Database column cost SqlType(SMALLINT)
   *  @param prSpace Database column pr_space SqlType(VARCHAR), Length(5,true)
   *  @param prGround Database column pr_ground SqlType(VARCHAR), Length(5,true)
   *  @param prWater Database column pr_water SqlType(VARCHAR), Length(5,true), Default(None)
   *  @param prForest Database column pr_forest SqlType(VARCHAR), Length(5,true), Default(None)
   *  @param prDesert Database column pr_desert SqlType(VARCHAR), Length(5,true), Default(None)
   *  @param wazaName Database column waza_name SqlType(VARCHAR), Length(100,true)
   *  @param mecName Database column mec_name SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param aceEffect Database column ace_effect SqlType(VARCHAR), Length(200,true), Default(None)
   *  @param ability1 Database column ability1 SqlType(VARCHAR), Length(50,true)
   *  @param ability2 Database column ability2 SqlType(VARCHAR), Length(50,true), Default(None)
   *  @param text Database column text SqlType(VARCHAR), Length(500,true) */
  case class MobileSuitsRow(area: String, setCode: String, cardNo: String, name: String, rarity: Option[String] = None, image: String, pilotName: Option[String] = None, attrHp: Int, attrPower: Int, attrSpeed: Int, special: Int, cost: Int, prSpace: String, prGround: String, prWater: Option[String] = None, prForest: Option[String] = None, prDesert: Option[String] = None, wazaName: String, mecName: Option[String] = None, aceEffect: Option[String] = None, ability1: String, ability2: Option[String] = None, text: String)
  /** GetResult implicit for fetching MobileSuitsRow objects using plain SQL queries */
  implicit def GetResultMobileSuitsRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Int]): GR[MobileSuitsRow] = GR{
    prs => import prs._
    MobileSuitsRow(<<[String], <<[String], <<[String], <<[String], <<?[String], <<[String], <<?[String], <<[Int], <<[Int], <<[Int], <<[Int], <<[Int], <<[String], <<[String], <<?[String], <<?[String], <<?[String], <<[String], <<?[String], <<?[String], <<[String], <<?[String], <<[String])
  }
  /** Table description of table mobile_suits. Objects of this class serve as prototypes for rows in queries. */
  class MobileSuits(_tableTag: Tag) extends profile.api.Table[MobileSuitsRow](_tableTag, Some("gundam"), "mobile_suits") {
    def * = (area :: setCode :: cardNo :: name :: rarity :: image :: pilotName :: attrHp :: attrPower :: attrSpeed :: special :: cost :: prSpace :: prGround :: prWater :: prForest :: prDesert :: wazaName :: mecName :: aceEffect :: ability1 :: ability2 :: text :: HNil).mapTo[MobileSuitsRow]
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(area) :: Rep.Some(setCode) :: Rep.Some(cardNo) :: Rep.Some(name) :: rarity :: Rep.Some(image) :: pilotName :: Rep.Some(attrHp) :: Rep.Some(attrPower) :: Rep.Some(attrSpeed) :: Rep.Some(special) :: Rep.Some(cost) :: Rep.Some(prSpace) :: Rep.Some(prGround) :: prWater :: prForest :: prDesert :: Rep.Some(wazaName) :: mecName :: aceEffect :: Rep.Some(ability1) :: ability2 :: Rep.Some(text) :: HNil).shaped.<>(r => MobileSuitsRow(r(0).asInstanceOf[Option[String]].get, r(1).asInstanceOf[Option[String]].get, r(2).asInstanceOf[Option[String]].get, r(3).asInstanceOf[Option[String]].get, r(4).asInstanceOf[Option[String]], r(5).asInstanceOf[Option[String]].get, r(6).asInstanceOf[Option[String]], r(7).asInstanceOf[Option[Int]].get, r(8).asInstanceOf[Option[Int]].get, r(9).asInstanceOf[Option[Int]].get, r(10).asInstanceOf[Option[Int]].get, r(11).asInstanceOf[Option[Int]].get, r(12).asInstanceOf[Option[String]].get, r(13).asInstanceOf[Option[String]].get, r(14).asInstanceOf[Option[String]], r(15).asInstanceOf[Option[String]], r(16).asInstanceOf[Option[String]], r(17).asInstanceOf[Option[String]].get, r(18).asInstanceOf[Option[String]], r(19).asInstanceOf[Option[String]], r(20).asInstanceOf[Option[String]].get, r(21).asInstanceOf[Option[String]], r(22).asInstanceOf[Option[String]].get), (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column area SqlType(VARCHAR), Length(10,true) */
    val area: Rep[String] = column[String]("area", O.Length(10,varying=true))
    /** Database column set_code SqlType(VARCHAR), Length(5,true) */
    val setCode: Rep[String] = column[String]("set_code", O.Length(5,varying=true))
    /** Database column card_no SqlType(VARCHAR), Length(10,true) */
    val cardNo: Rep[String] = column[String]("card_no", O.Length(10,varying=true))
    /** Database column name SqlType(VARCHAR), Length(100,true) */
    val name: Rep[String] = column[String]("name", O.Length(100,varying=true))
    /** Database column rarity SqlType(VARCHAR), Length(5,true), Default(None) */
    val rarity: Rep[Option[String]] = column[Option[String]]("rarity", O.Length(5,varying=true), O.Default(None))
    /** Database column image SqlType(VARCHAR), Length(100,true) */
    val image: Rep[String] = column[String]("image", O.Length(100,varying=true))
    /** Database column pilot_name SqlType(VARCHAR), Length(100,true), Default(None) */
    val pilotName: Rep[Option[String]] = column[Option[String]]("pilot_name", O.Length(100,varying=true), O.Default(None))
    /** Database column attr_hp SqlType(SMALLINT) */
    val attrHp: Rep[Int] = column[Int]("attr_hp")
    /** Database column attr_power SqlType(SMALLINT) */
    val attrPower: Rep[Int] = column[Int]("attr_power")
    /** Database column attr_speed SqlType(SMALLINT) */
    val attrSpeed: Rep[Int] = column[Int]("attr_speed")
    /** Database column special SqlType(INT) */
    val special: Rep[Int] = column[Int]("special")
    /** Database column cost SqlType(SMALLINT) */
    val cost: Rep[Int] = column[Int]("cost")
    /** Database column pr_space SqlType(VARCHAR), Length(5,true) */
    val prSpace: Rep[String] = column[String]("pr_space", O.Length(5,varying=true))
    /** Database column pr_ground SqlType(VARCHAR), Length(5,true) */
    val prGround: Rep[String] = column[String]("pr_ground", O.Length(5,varying=true))
    /** Database column pr_water SqlType(VARCHAR), Length(5,true), Default(None) */
    val prWater: Rep[Option[String]] = column[Option[String]]("pr_water", O.Length(5,varying=true), O.Default(None))
    /** Database column pr_forest SqlType(VARCHAR), Length(5,true), Default(None) */
    val prForest: Rep[Option[String]] = column[Option[String]]("pr_forest", O.Length(5,varying=true), O.Default(None))
    /** Database column pr_desert SqlType(VARCHAR), Length(5,true), Default(None) */
    val prDesert: Rep[Option[String]] = column[Option[String]]("pr_desert", O.Length(5,varying=true), O.Default(None))
    /** Database column waza_name SqlType(VARCHAR), Length(100,true) */
    val wazaName: Rep[String] = column[String]("waza_name", O.Length(100,varying=true))
    /** Database column mec_name SqlType(VARCHAR), Length(100,true), Default(None) */
    val mecName: Rep[Option[String]] = column[Option[String]]("mec_name", O.Length(100,varying=true), O.Default(None))
    /** Database column ace_effect SqlType(VARCHAR), Length(200,true), Default(None) */
    val aceEffect: Rep[Option[String]] = column[Option[String]]("ace_effect", O.Length(200,varying=true), O.Default(None))
    /** Database column ability1 SqlType(VARCHAR), Length(50,true) */
    val ability1: Rep[String] = column[String]("ability1", O.Length(50,varying=true))
    /** Database column ability2 SqlType(VARCHAR), Length(50,true), Default(None) */
    val ability2: Rep[Option[String]] = column[Option[String]]("ability2", O.Length(50,varying=true), O.Default(None))
    /** Database column text SqlType(VARCHAR), Length(500,true) */
    val text: Rep[String] = column[String]("text", O.Length(500,varying=true))

    /** Primary key of MobileSuits (database name mobile_suits_PK) */
    val pk = primaryKey("mobile_suits_PK", area :: cardNo :: HNil)
  }
  /** Collection-like TableQuery object for table MobileSuits */
  lazy val MobileSuits = new TableQuery(tag => new MobileSuits(tag))

  /** Entity class storing rows of table Pilots
   *  @param area Database column area SqlType(VARCHAR), Length(10,true)
   *  @param setCode Database column set_code SqlType(VARCHAR), Length(5,true)
   *  @param cardNo Database column card_no SqlType(VARCHAR), Length(10,true)
   *  @param name Database column name SqlType(VARCHAR), Length(100,true)
   *  @param rarity Database column rarity SqlType(VARCHAR), Length(5,true), Default(None)
   *  @param image Database column image SqlType(VARCHAR), Length(100,true)
   *  @param attrHp Database column attr_hp SqlType(SMALLINT)
   *  @param attrPower Database column attr_power SqlType(SMALLINT)
   *  @param attrSpeed Database column attr_speed SqlType(SMALLINT)
   *  @param burstName Database column burst_name SqlType(VARCHAR), Length(50,true)
   *  @param burstlevel Database column burstLevel SqlType(SMALLINT)
   *  @param burstType Database column burst_type SqlType(VARCHAR), Length(50,true)
   *  @param aceEffect Database column ace_effect SqlType(VARCHAR), Length(200,true), Default(None)
   *  @param skill Database column skill SqlType(VARCHAR), Length(200,true)
   *  @param text Database column text SqlType(VARCHAR), Length(500,true) */
  case class PilotsRow(area: String, setCode: String, cardNo: String, name: String, rarity: Option[String] = None, image: String, attrHp: Int, attrPower: Int, attrSpeed: Int, burstName: String, burstlevel: Int, burstType: String, aceEffect: Option[String] = None, skill: String, text: String)
  /** GetResult implicit for fetching PilotsRow objects using plain SQL queries */
  implicit def GetResultPilotsRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Int]): GR[PilotsRow] = GR{
    prs => import prs._
    PilotsRow.tupled((<<[String], <<[String], <<[String], <<[String], <<?[String], <<[String], <<[Int], <<[Int], <<[Int], <<[String], <<[Int], <<[String], <<?[String], <<[String], <<[String]))
  }
  /** Table description of table pilots. Objects of this class serve as prototypes for rows in queries. */
  class Pilots(_tableTag: Tag) extends profile.api.Table[PilotsRow](_tableTag, Some("gundam"), "pilots") {
    def * = (area, setCode, cardNo, name, rarity, image, attrHp, attrPower, attrSpeed, burstName, burstlevel, burstType, aceEffect, skill, text) <> (PilotsRow.tupled, PilotsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(area), Rep.Some(setCode), Rep.Some(cardNo), Rep.Some(name), rarity, Rep.Some(image), Rep.Some(attrHp), Rep.Some(attrPower), Rep.Some(attrSpeed), Rep.Some(burstName), Rep.Some(burstlevel), Rep.Some(burstType), aceEffect, Rep.Some(skill), Rep.Some(text))).shaped.<>({r=>import r._; _1.map(_=> PilotsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13, _14.get, _15.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column area SqlType(VARCHAR), Length(10,true) */
    val area: Rep[String] = column[String]("area", O.Length(10,varying=true))
    /** Database column set_code SqlType(VARCHAR), Length(5,true) */
    val setCode: Rep[String] = column[String]("set_code", O.Length(5,varying=true))
    /** Database column card_no SqlType(VARCHAR), Length(10,true) */
    val cardNo: Rep[String] = column[String]("card_no", O.Length(10,varying=true))
    /** Database column name SqlType(VARCHAR), Length(100,true) */
    val name: Rep[String] = column[String]("name", O.Length(100,varying=true))
    /** Database column rarity SqlType(VARCHAR), Length(5,true), Default(None) */
    val rarity: Rep[Option[String]] = column[Option[String]]("rarity", O.Length(5,varying=true), O.Default(None))
    /** Database column image SqlType(VARCHAR), Length(100,true) */
    val image: Rep[String] = column[String]("image", O.Length(100,varying=true))
    /** Database column attr_hp SqlType(SMALLINT) */
    val attrHp: Rep[Int] = column[Int]("attr_hp")
    /** Database column attr_power SqlType(SMALLINT) */
    val attrPower: Rep[Int] = column[Int]("attr_power")
    /** Database column attr_speed SqlType(SMALLINT) */
    val attrSpeed: Rep[Int] = column[Int]("attr_speed")
    /** Database column burst_name SqlType(VARCHAR), Length(50,true) */
    val burstName: Rep[String] = column[String]("burst_name", O.Length(50,varying=true))
    /** Database column burstLevel SqlType(SMALLINT) */
    val burstlevel: Rep[Int] = column[Int]("burstLevel")
    /** Database column burst_type SqlType(VARCHAR), Length(50,true) */
    val burstType: Rep[String] = column[String]("burst_type", O.Length(50,varying=true))
    /** Database column ace_effect SqlType(VARCHAR), Length(200,true), Default(None) */
    val aceEffect: Rep[Option[String]] = column[Option[String]]("ace_effect", O.Length(200,varying=true), O.Default(None))
    /** Database column skill SqlType(VARCHAR), Length(200,true) */
    val skill: Rep[String] = column[String]("skill", O.Length(200,varying=true))
    /** Database column text SqlType(VARCHAR), Length(500,true) */
    val text: Rep[String] = column[String]("text", O.Length(500,varying=true))

    /** Primary key of Pilots (database name pilots_PK) */
    val pk = primaryKey("pilots_PK", (area, cardNo))
  }
  /** Collection-like TableQuery object for table Pilots */
  lazy val Pilots = new TableQuery(tag => new Pilots(tag))

  /** Entity class storing rows of table UnknownTypes
   *  @param area Database column area SqlType(VARCHAR), Length(10,true)
   *  @param setCode Database column set_code SqlType(VARCHAR), Length(5,true)
   *  @param cardNo Database column card_no SqlType(VARCHAR), Length(10,true)
   *  @param name Database column name SqlType(VARCHAR), Length(100,true)
   *  @param rarity Database column rarity SqlType(VARCHAR), Length(5,true), Default(None)
   *  @param image Database column image SqlType(VARCHAR), Length(100,true)
   *  @param typeClasses Database column type_classes SqlType(VARCHAR), Length(500,true) */
  case class UnknownTypesRow(area: String, setCode: String, cardNo: String, name: String, rarity: Option[String] = None, image: String, typeClasses: String)
  /** GetResult implicit for fetching UnknownTypesRow objects using plain SQL queries */
  implicit def GetResultUnknownTypesRow(implicit e0: GR[String], e1: GR[Option[String]]): GR[UnknownTypesRow] = GR{
    prs => import prs._
    UnknownTypesRow.tupled((<<[String], <<[String], <<[String], <<[String], <<?[String], <<[String], <<[String]))
  }
  /** Table description of table unknown_types. Objects of this class serve as prototypes for rows in queries. */
  class UnknownTypes(_tableTag: Tag) extends profile.api.Table[UnknownTypesRow](_tableTag, Some("gundam"), "unknown_types") {
    def * = (area, setCode, cardNo, name, rarity, image, typeClasses) <> (UnknownTypesRow.tupled, UnknownTypesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(area), Rep.Some(setCode), Rep.Some(cardNo), Rep.Some(name), rarity, Rep.Some(image), Rep.Some(typeClasses))).shaped.<>({r=>import r._; _1.map(_=> UnknownTypesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column area SqlType(VARCHAR), Length(10,true) */
    val area: Rep[String] = column[String]("area", O.Length(10,varying=true))
    /** Database column set_code SqlType(VARCHAR), Length(5,true) */
    val setCode: Rep[String] = column[String]("set_code", O.Length(5,varying=true))
    /** Database column card_no SqlType(VARCHAR), Length(10,true) */
    val cardNo: Rep[String] = column[String]("card_no", O.Length(10,varying=true))
    /** Database column name SqlType(VARCHAR), Length(100,true) */
    val name: Rep[String] = column[String]("name", O.Length(100,varying=true))
    /** Database column rarity SqlType(VARCHAR), Length(5,true), Default(None) */
    val rarity: Rep[Option[String]] = column[Option[String]]("rarity", O.Length(5,varying=true), O.Default(None))
    /** Database column image SqlType(VARCHAR), Length(100,true) */
    val image: Rep[String] = column[String]("image", O.Length(100,varying=true))
    /** Database column type_classes SqlType(VARCHAR), Length(500,true) */
    val typeClasses: Rep[String] = column[String]("type_classes", O.Length(500,varying=true))

    /** Primary key of UnknownTypes (database name unknown_types_PK) */
    val pk = primaryKey("unknown_types_PK", (area, cardNo))
  }
  /** Collection-like TableQuery object for table UnknownTypes */
  lazy val UnknownTypes = new TableQuery(tag => new UnknownTypes(tag))
}
