drop table mobile_suits;
create table mobile_suits (
  area varchar(10) not null,
  set_code varchar(5) not null,
  card_no varchar(10) not null,
  name varchar(100) not null,
  rarity varchar(5) null,
  image varchar(100) not null,
  pilot_name varchar(100) null,
  attr_hp smallint not null,
  attr_power smallint not null,
  attr_speed smallint not null,
  special int not null,
  cost smallint not null,
  pr_space varchar(5) not null,
  pr_ground varchar(5) not null,
  pr_water varchar(5) null,
  pr_forest varchar(5) null,
  pr_desert varchar(5) null,
  waza_name varchar(100) not null,
  mec_name varchar(100) null,
  ace_effect varchar(200) null,
  ability1 varchar(50) not null,
  ability2 varchar(50) null,
  text varchar(500) not null,
  primary key (area, card_no)
)
CHARACTER SET utf8;

drop table pilots;
create table pilots (
  area varchar(10) not null,
  set_code varchar(5) not null,
  card_no varchar(10) not null,
  name varchar(100) not null,
  rarity varchar(5) null,
  image varchar(100) not null,
  attr_hp smallint not null,
  attr_power smallint not null,
  attr_speed smallint not null,
  burst_name varchar(50) not null,
  burstLevel smallint not null,
  burst_type varchar(50) not null,
  ace_effect varchar(200) null,
  skill varchar(200) not null,
  text varchar(500) not null,
  primary key (area, card_no)
)
CHARACTER SET utf8;

drop table ignitions;
create table ignitions (
  area varchar(10) not null,
  set_code varchar(5) not null,
  card_no varchar(10) not null,
  name varchar(100) not null,
  rarity varchar(5) null,
  image varchar(100) not null,
  waza_name varchar(100) not null,
  special int not null,
  pilot_name varchar(100) not null,
  effect_skill varchar(200) not null,
  effect_text varchar(200) not null,
  pilot_skill varchar(200) null,
  pilot_skill_text varchar(500) null,
  primary key (area, card_no)
)
CHARACTER SET utf8;

drop table unknown_types;
create table unknown_types (
  area varchar(10) not null,
  set_code varchar(5) not null,
  card_no varchar(10) not null,
  name varchar(100) not null,
  rarity varchar(5) null,
  image varchar(100) not null,
  type_classes varchar(500) not null,
  primary key (area, card_no)
)
CHARACTER SET utf8;