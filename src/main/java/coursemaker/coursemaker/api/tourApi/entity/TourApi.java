package coursemaker.coursemaker.api.tourApi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tour_api")
public class TourApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private long contentid;

    @Column(name = "tel", length = 50)
    private String tel;

    @Column(length = 100)
    private String addr1;

    @Column(length = 100)
    private String addr2;

    private int sigungucode;

    @Column(length = 300)
    private String firstimage;

    @Column(length = 300)
    private String firstimage2;
    @Column(precision = 15, scale = 12)
    private BigDecimal mapx;
    @Column(precision = 15, scale = 12)
    private BigDecimal mapy;
    @Column(length = 50)
    private String zipcode;

    @Column(length = 50)
    private String createdtime;

    @Column(length = 50)
    private String modifiedtime;

    @Column
    private String cat1;

    @Column
    private String cat2;

    @Column
    private String cat3;

    @Column
    private int contenttypeid;

    @Column
    private Long disabled;

    @Column
    private String homepage;

    @Column
    private String overview;

    // 필드 추가: contentTypeId = 12 (관광지)
    @Column
    private Integer accomcount;

    @Column
    private String chkbabycarriage;

    @Column
    private String chkcreditcard;

    @Column
    private String chkpet;

    @Column
    private String expagerange;

    @Column
    private String expguide;

    @Column
    private String heritage1;

    @Column
    private String heritage2;

    @Column
    private String heritage3;

    @Column
    private String infocenter;

    @Column
    private String opendate;

    @Column
    private String parking;

    @Column
    private String restdate;

    @Column
    private String useseason;

    @Column
    private String usetime;

    // 필드 추가: contentTypeId = 14 (문화시설)
    @Column
    private Integer accomcountculture;

    @Column
    private String chkbabycarriageculture;

    @Column
    private String chkcreditcardculture;

    @Column
    private String chkpetculture;

    @Column
    private String discountinfo;

    @Column
    private String infocenterculture;

    @Column
    private String parkingculture;

    @Column
    private String parkingfee;

    @Column
    private String restdateculture;

    @Column
    private String usefee;

    @Column
    private String usetimeculture;

    @Column
    private String scale;

    @Column
    private String spendtime;

    // 필드 추가: contentTypeId = 15 (행사/공연/축제)
    @Column
    private String agelimit;

    @Column
    private String bookingplace;

    @Column
    private String discountinfofestival;

    @Column
    private String eventenddate;

    @Column
    private String eventhomepage;

    @Column
    private String eventplace;

    @Column
    private String eventstartdate;

    @Column
    private String festivalgrade;

    @Column
    private String placeinfo;

    @Column
    private String playtime;

    @Column
    private String program;

    @Column
    private String spendtimefestival;

    @Column
    private String sponsor1;

    @Column
    private String sponsor1tel;

    @Column
    private String sponsor2;

    @Column
    private String sponsor2tel;

    @Column
    private String subevent;

    @Column
    private String usetimefestival;

    // 필드 추가: contentTypeId = 25 (여행코스)
    @Column
    private String distance;

    @Column
    private String infocentertourcourse;

    @Column
    private String schedule;

    @Column
    private String taketime;

    @Column
    private String theme;

    // 필드 추가: contentTypeId = 28 (레포츠)
    @Column
    private Integer accomcountleports;

    @Column
    private String chkbabycarriageleports;

    @Column
    private String chkcreditcardleports;

    @Column
    private String chkpetleports;

    @Column
    private String expagerangeleports;

    @Column
    private String infocenterleports;

    @Column
    private String openperiod;

    @Column
    private String parkingfeeleports;

    @Column
    private String parkingleports;

    @Column
    private String reservation;

    @Column
    private String restdateleports;

    @Column
    private String scaleleports;

    @Column
    private String usefeeleports;

    @Column
    private String usetimeleports;

    // 필드 추가: contentTypeId = 32 (숙박)
    @Column
    private Integer accomcountlodging;

    @Column
    private String benikia;

    @Column
    private String checkintime;

    @Column
    private String checkouttime;

    @Column
    private String chkcooking;

    @Column
    private String foodplace;

    @Column
    private String goodstay;

    @Column
    private String hanok;

    @Column
    private String infocenterlodging;

    @Column
    private String parkinglodging;

    @Column
    private String pickup;

    @Column
    private Integer roomcount;

    @Column
    private String reservationlodging;

    @Column
    private String reservationurl;

    @Column
    private String roomtype;

    @Column
    private String scalelodging;

    @Column
    private String subfacility;

    @Column
    private String barbecue;

    @Column
    private String beauty;

    @Column
    private String beverage;

    @Column
    private String bicycle;

    @Column
    private String campfire;

    @Column
    private String fitness;

    @Column
    private String karaoke;

    @Column
    private String publicbath;

    @Column
    private String publicpc;

    @Column
    private String sauna;

    @Column
    private String seminar;

    @Column
    private String sports;

    @Column
    private String refundregulation;

    // 필드 추가: contentTypeId = 38 (쇼핑)
    @Column
    private String chkbabycarriageshopping;

    @Column
    private String chkcreditcardshopping;

    @Column
    private String chkpetshopping;

    @Column
    private String culturecenter;

    @Column
    private String fairday;

    @Column
    private String infocentershopping;

    @Column
    private String opendateshopping;

    @Column
    private String opentime;

    @Column
    private String parkingshopping;

    @Column
    private String restdateshopping;

    @Column
    private String restroom;

    @Column
    private String saleitem;

    @Column
    private String saleitemcost;

    @Column
    private String scaleshopping;

    @Column
    private String shopguide;

    // 필드 추가: contentTypeId = 39 (음식점)
    @Column
    private String chkcreditcardfood;

    @Column
    private String discountinfofood;

    @Column
    private String firstmenu;

    @Column
    private String infocenterfood;

    @Column
    private String kidsfacility;

    @Column
    private String opendatefood;

    @Column
    private String opentimefood;

    @Column
    private String packing;

    @Column
    private String parkingfood;

    @Column
    private String reservationfood;

    @Column
    private String restdatefood;

    @Column
    private String scalefood;

    @Column
    private Integer seat;

    @Column
    private String smoking;

    @Column
    private String treatmenu;

    @Column
    private String lcnsno;

    @Builder
    public TourApi(String title, long contentid, String tel, String addr1, String addr2, int sigungucode,
                   String firstimage, String firstimage2, BigDecimal mapx, BigDecimal mapy, String zipcode,
                   String createdtime, String modifiedtime, String cat1, String cat2, String cat3,
                   int contenttypeid, Long disabled, String homepage, String overview,
                   Integer accomcount, String chkbabycarriage, String chkcreditcard, String chkpet,
                   String expagerange, String expguide, String heritage1, String heritage2,
                   String heritage3, String infocenter, String opendate, String parking, String restdate,
                   String useseason, String usetime, Integer accomcountculture, String chkbabycarriageculture,
                   String chkcreditcardculture, String chkpetculture, String discountinfo, String infocenterculture,
                   String parkingculture, String parkingfee, String restdateculture, String usefee,
                   String usetimeculture, String scale, String spendtime, String agelimit, String bookingplace,
                   String discountinfofestival, String eventenddate, String eventhomepage, String eventplace,
                   String eventstartdate, String festivalgrade, String placeinfo, String playtime, String program,
                   String spendtimefestival, String sponsor1, String sponsor1tel, String sponsor2, String sponsor2tel,
                   String subevent, String usetimefestival, String distance, String infocentertourcourse, String schedule,
                   String taketime, String theme, Integer accomcountleports, String chkbabycarriageleports,
                   String chkcreditcardleports, String chkpetleports, String expagerangeleports, String infocenterleports,
                   String openperiod, String parkingfeeleports, String parkingleports, String reservation,
                   String restdateleports, String scaleleports, String usefeeleports, String usetimeleports,
                   Integer accomcountlodging, String benikia, String checkintime, String checkouttime, String chkcooking,
                   String foodplace, String goodstay, String hanok, String infocenterlodging, String parkinglodging,
                   String pickup, Integer roomcount, String reservationlodging, String reservationurl, String roomtype,
                   String scalelodging, String subfacility, String barbecue, String beauty, String beverage,
                   String bicycle, String campfire, String fitness, String karaoke, String publicbath, String publicpc,
                   String sauna, String seminar, String sports, String refundregulation, String chkbabycarriageshopping,
                   String chkcreditcardshopping, String chkpetshopping, String culturecenter, String fairday,
                   String infocentershopping, String opendateshopping, String opentime, String parkingshopping,
                   String restdateshopping, String restroom, String saleitem, String saleitemcost, String scaleshopping,
                   String shopguide, String chkcreditcardfood, String discountinfofood, String firstmenu, String infocenterfood,
                   String kidsfacility, String opendatefood, String opentimefood, String packing, String parkingfood,
                   String reservationfood, String restdatefood, String scalefood, Integer seat, String smoking,
                   String treatmenu, String lcnsno) {
        this.title = title;
        this.contentid = contentid;
        this.tel = tel;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.sigungucode = sigungucode;
        this.firstimage = firstimage;
        this.firstimage2 = firstimage2;
        this.mapx = mapx;
        this.mapy = mapy;
        this.zipcode = zipcode;
        this.createdtime = createdtime;
        this.modifiedtime = modifiedtime;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.contenttypeid = contenttypeid;
        this.disabled = disabled;
        this.homepage = homepage;
        this.overview = overview;
        this.accomcount = accomcount;
        this.chkbabycarriage = chkbabycarriage;
        this.chkcreditcard = chkcreditcard;
        this.chkpet = chkpet;
        this.expagerange = expagerange;
        this.expguide = expguide;
        this.heritage1 = heritage1;
        this.heritage2 = heritage2;
        this.heritage3 = heritage3;
        this.infocenter = infocenter;
        this.opendate = opendate;
        this.parking = parking;
        this.restdate = restdate;
        this.useseason = useseason;
        this.usetime = usetime;
        this.accomcountculture = accomcountculture;
        this.chkbabycarriageculture = chkbabycarriageculture;
        this.chkcreditcardculture = chkcreditcardculture;
        this.chkpetculture = chkpetculture;
        this.discountinfo = discountinfo;
        this.infocenterculture = infocenterculture;
        this.parkingculture = parkingculture;
        this.parkingfee = parkingfee;
        this.restdateculture = restdateculture;
        this.usefee = usefee;
        this.usetimeculture = usetimeculture;
        this.scale = scale;
        this.spendtime = spendtime;
        this.agelimit = agelimit;
        this.bookingplace = bookingplace;
        this.discountinfofestival = discountinfofestival;
        this.eventenddate = eventenddate;
        this.eventhomepage = eventhomepage;
        this.eventplace = eventplace;
        this.eventstartdate = eventstartdate;
        this.festivalgrade = festivalgrade;
        this.placeinfo = placeinfo;
        this.playtime = playtime;
        this.program = program;
        this.spendtimefestival = spendtimefestival;
        this.sponsor1 = sponsor1;
        this.sponsor1tel = sponsor1tel;
        this.sponsor2 = sponsor2;
        this.sponsor2tel = sponsor2tel;
        this.subevent = subevent;
        this.usetimefestival = usetimefestival;
        this.distance = distance;
        this.infocentertourcourse = infocentertourcourse;
        this.schedule = schedule;
        this.taketime = taketime;
        this.theme = theme;
        this.accomcountleports = accomcountleports;
        this.chkbabycarriageleports = chkbabycarriageleports;
        this.chkcreditcardleports = chkcreditcardleports;
        this.chkpetleports = chkpetleports;
        this.expagerangeleports = expagerangeleports;
        this.infocenterleports = infocenterleports;
        this.openperiod = openperiod;
        this.parkingfeeleports = parkingfeeleports;
        this.parkingleports = parkingleports;
        this.reservation = reservation;
        this.restdateleports = restdateleports;
        this.scaleleports = scaleleports;
        this.usefeeleports = usefeeleports;
        this.usetimeleports = usetimeleports;
        this.accomcountlodging = accomcountlodging;
        this.benikia = benikia;
        this.checkintime = checkintime;
        this.checkouttime = checkouttime;
        this.chkcooking = chkcooking;
        this.foodplace = foodplace;
        this.goodstay = goodstay;
        this.hanok = hanok;
        this.infocenterlodging = infocenterlodging;
        this.parkinglodging = parkinglodging;
        this.pickup = pickup;
        this.roomcount = roomcount;
        this.reservationlodging = reservationlodging;
        this.reservationurl = reservationurl;
        this.roomtype = roomtype;
        this.scalelodging = scalelodging;
        this.subfacility = subfacility;
        this.barbecue = barbecue;
        this.beauty = beauty;
        this.beverage = beverage;
        this.bicycle = bicycle;
        this.campfire = campfire;
        this.fitness = fitness;
        this.karaoke = karaoke;
        this.publicbath = publicbath;
        this.publicpc = publicpc;
        this.sauna = sauna;
        this.seminar = seminar;
        this.sports = sports;
        this.refundregulation = refundregulation;
        this.chkbabycarriageshopping = chkbabycarriageshopping;
        this.chkcreditcardshopping = chkcreditcardshopping;
        this.chkpetshopping = chkpetshopping;
        this.culturecenter = culturecenter;
        this.fairday = fairday;
        this.infocentershopping = infocentershopping;
        this.opendateshopping = opendateshopping;
        this.opentime = opentime;
        this.parkingshopping = parkingshopping;
        this.restdateshopping = restdateshopping;
        this.restroom = restroom;
        this.saleitem = saleitem;
        this.saleitemcost = saleitemcost;
        this.scaleshopping = scaleshopping;
        this.shopguide = shopguide;
        this.chkcreditcardfood = chkcreditcardfood;
        this.discountinfofood = discountinfofood;
        this.firstmenu = firstmenu;
        this.infocenterfood = infocenterfood;
        this.kidsfacility = kidsfacility;
        this.opendatefood = opendatefood;
        this.opentimefood = opentimefood;
        this.packing = packing;
        this.parkingfood = parkingfood;
        this.reservationfood = reservationfood;
        this.restdatefood = restdatefood;
        this.scalefood = scalefood;
        this.seat = seat;
        this.smoking = smoking;
        this.treatmenu = treatmenu;
        this.lcnsno = lcnsno;
    }
}

