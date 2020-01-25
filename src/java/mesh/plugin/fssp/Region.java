package mesh.plugin.fssp;

/**
 *
 * @author tsowa
 */
public enum Region {
  ALL(0, "Все регионы"),
  Алтайский_край(22, "Алтайский край"),
  Амурская_область(28, "Амурская область"),
  Архангельская_область(29, "Архангельская область"),
  //regions.add(new Region(30, "Астраханская область"),
  //regions.add(new Region(31, "Белгородская область"),
  //regions.add(new Region(32, "Брянская область"),
  //regions.add(new Region(33, "Владимирская область"),
  //regions.add(new Region(34, "Волгоградская область"),
  //regions.add(new Region(35, "Вологодская область"),
  //regions.add(new Region(36, "Воронежская область"),
  //regions.add(new Region(79, "Еврейская АО"),
  //regions.add(new Region(75, "Забайкальский край"),
  //regions.add(new Region(37, "Ивановская область"),
  //regions.add(new Region(38, "Иркутская область"),
  //regions.add(new Region(7, "Кабардино-Балкария"),
  //regions.add(new Region(39, "Калининградская область"),
  //regions.add(new Region(40, "Калужская область"),
  //regions.add(new Region(41, "Камчатский край"),
  //regions.add(new Region(9, "Карачаево-Черкессия"),
  //regions.add(new Region(42, "Кемеровская область"),
  //regions.add(new Region(43, "Кировская область"),
  //regions.add(new Region(44, "Костромская область"),
  //regions.add(new Region(23, "Краснодарский край"),
  //regions.add(new Region(24, "Красноярский край"),
  //regions.add(new Region(45, "Курганская область"),
  KURSK(46, "Курская область"),
  LENINGRAD(47, "Ленинградская область"),
  LIPETSK(48, "Липецкая область"),
  MAGADAN(49, "Магаданская область"),
  MOSKWA(77, "Москва"),
  MOSCOW(50, "Московская область"),
  MURMANSK(51, "Мурманская область"),
  //regions.add(new Region(83, "Ненецкий АО"),
  //regions.add(new Region(52, "Нижегородская область"),
  NOVGOROD(53, "Новгородская область"),
  NOVOSIBIRSK(54, "Новосибирская область"),
  OMSK(55, "Омская область"),
  ORENBURG(56, "Оренбургская область"),
  OREL(57, "Орловская область"),
  PENZA(58, "Пензенская область"),
  PERM(59, "Пермский край"),
  PROMORYE(25, "Приморский край"),
  PSKOV(60, "Псковская область"),
  ADYGEYA(1, "Республика Адыгея"),
  //regions.add(new Region(4, "Республика Алтай"),
  //regions.add(new Region(2, "Республика Башкортостан"),
  //regions.add(new Region(3, "Республика Бурятия"),
  //regions.add(new Region(5, "Республика Дагестан"),
  //regions.add(new Region(6, "Республика Ингушетия"),
  //regions.add(new Region(8, "Республика Калмыкия"),
  //regions.add(new Region(10, "Республика Карелия"),
  //regions.add(new Region(11, "Республика Коми"),
  //regions.add(new Region(82, "Республика Крым"),
  //regions.add(new Region(12, "Республика Марий-Эл"),
  //regions.add(new Region(13, "Республика Мордовия"),
  //regions.add(new Region(14, "Республика Саха (Якутия)"),
  //regions.add(new Region(16, "Республика Татарстан"),
  //regions.add(new Region(17, "Республика Тыва"),
  //regions.add(new Region(19, "Республика Хакасия"),
  //regions.add(new Region(61, "Ростовская область"),
  //regions.add(new Region(62, "Рязанская область"),
  //regions.add(new Region(63, "Самарская область"),
  //regions.add(new Region(78, "Санкт-Петербург"),
  //regions.add(new Region(64, "Саратовская область"),
  //regions.add(new Region(65, "Сахалинская область"),
  //regions.add(new Region(66, "Свердловская область"),
  //regions.add(new Region(92, "Севастополь"),
  //regions.add(new Region(15, "Северная Осетия-Алания"),
  //regions.add(new Region(67, "Смоленская область"),
  //regions.add(new Region(26, "Ставропольский край"),
  //regions.add(new Region(68, "Тамбовская область"),
  //regions.add(new Region(69, "Тверская область"),
  //regions.add(new Region(70, "Томская область"),
  //regions.add(new Region(71, "Тульская область"),
  //regions.add(new Region(72, "Тюменская область"),
  //regions.add(new Region(18, "Удмуртская Республика"),
  //regions.add(new Region(73, "Ульяновская область"),
  //regions.add(new Region(27, "Хабаровский край"),
  //regions.add(new Region(86, "Ханты-Мансийский АО"),
  //regions.add(new Region(74, "Челябинская область"),
  //regions.add(new Region(20, "Чеченская Республика"),
  //regions.add(new Region(21, "Чувашская Республика"),
  //regions.add(new Region(87, "Чукотский АО"),
  //regions.add(new Region(89, "Ямало-Ненецкий АО"),
  YAROSLAVL(76, "Ярославская область");
  
  private Integer id;
  private String name;
  private Region(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
  
  public Integer getId() {
    return id;
  }
}