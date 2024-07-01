package coursemaker.coursemaker.api.tourApi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TourApiResponse {

    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private Items items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }

    @Data
    public static class Items {
        private List<Item> item;
    }

    @Data
    public static class Item {

        @Schema(description = "이름", example = "SEA LIFE 부산아쿠아리움")
        private String title;
        @Schema(description = "콘텐츠 ID", example = "229912")
        private long contentid;
        @Schema(description = "전화번호")
        private String tel;
        @Schema(description = "주소", example = "부산광역시 해운대구 해운대해변로 266")
        private String addr1;
        @Schema(description = "상세주소", example = "(중동)")
        private String addr2;
        @Schema(description = "sigungucode", example = "16")
        private int sigungucode;
        @Schema(description = "대표이미지(원본)", example = "http://tong.visitkorea.or.kr/cms/resource/09/3020609_image2_1.jpg")
        private String firstimage;
        @Schema(description = "대표이미지(썸네일)", example = "http://tong.visitkorea.or.kr/cms/resource/09/3020609_image3_1.jpg")
        private String firstimage2;
        @Schema(description = "X좌표, Longitude, 경도", example = "129.1610328343")
        private BigDecimal mapx;
        @Schema(description = "Y좌표, Latitude, 위도", example = "35.1591965750")
        private BigDecimal mapy;
        @Schema(description = "우편번호", example = "48100")
        private String zipcode;
        @Schema(description = "등록일", example = "20070716090000")
        private String createdtime;
        @Schema(description = "수정일", example = "20240509134635")
        private String modifiedtime;
        @Schema(description = "대분류", example = "A02")
        private String cat1;
        @Schema(description = "중분류", example = "A0202")
        private String cat2;
        @Schema(description = "소분류", example = "A02020600")
        private String cat3;
        @Schema(description = "콘텐츠타입 ID", example = "14")
        private int contenttypeid;

        @Schema(description = "무장애 여행지", example = "1")
        private int disabled;

        @Schema(description = "홈페이지")
        private String homepage;
        @Schema(description = "개요")
        private String overview;

        // 필드 추가: contentTypeId = 12 (관광지)
        private Integer accomcount;
        private String chkbabycarriage;
        private String chkcreditcard;
        private String chkpet;
        private String expagerange;
        private String expguide;
        private String heritage1;
        private String heritage2;
        private String heritage3;
        private String infocenter;
        private String opendate;
        private String parking;
        private String restdate;
        private String useseason;
        private String usetime;

        // 필드 추가: contentTypeId = 14 (문화시설)
        private Integer accomcountculture;
        private String chkbabycarriageculture;
        private String chkcreditcardculture;
        private String chkpetculture;
        private String discountinfo;
        private String infocenterculture;
        private String parkingculture;
        private String parkingfee;
        private String restdateculture;
        private String usefee;
        private String usetimeculture;
        private String scale;
        private String spendtime;

        // 필드 추가: contentTypeId = 15 (행사/공연/축제)
        private String agelimit;
        private String bookingplace;
        private String discountinfofestival;
        private String eventenddate;
        private String eventhomepage;
        private String eventplace;
        private String eventstartdate;
        private String festivalgrade;
        private String placeinfo;
        private String playtime;
        private String program;
        private String spendtimefestival;
        private String sponsor1;
        private String sponsor1tel;
        private String sponsor2;
        private String sponsor2tel;
        private String subevent;
        private String usetimefestival;

        // 필드 추가: contentTypeId = 25 (여행코스)
        private String distance;
        private String infocentertourcourse;
        private String schedule;
        private String taketime;
        private String theme;

        // 필드 추가: contentTypeId = 28 (레포츠)
        private Integer accomcountleports;
        private String chkbabycarriageleports;
        private String chkcreditcardleports;
        private String chkpetleports;
        private String expagerangeleports;
        private String infocenterleports;
        private String openperiod;
        private String parkingfeeleports;
        private String parkingleports;
        private String reservation;
        private String restdateleports;
        private String scaleleports;
        private String usefeeleports;
        private String usetimeleports;

        // 필드 추가: contentTypeId = 32 (숙박)
        private Integer accomcountlodging;
        private String benikia;
        private String checkintime;
        private String checkouttime;
        private String chkcooking;
        private String foodplace;
        private String goodstay;
        private String hanok;
        private String infocenterlodging;
        private String parkinglodging;
        private String pickup;
        private Integer roomcount;
        private String reservationlodging;
        private String reservationurl;
        private String roomtype;
        private String scalelodging;
        private String subfacility;
        private String barbecue;
        private String beauty;
        private String beverage;
        private String bicycle;
        private String campfire;
        private String fitness;
        private String karaoke;
        private String publicbath;
        private String publicpc;
        private String sauna;
        private String seminar;
        private String sports;
        private String refundregulation;

        // 필드 추가: contentTypeId = 38 (쇼핑)
        private String chkbabycarriageshopping;
        private String chkcreditcardshopping;
        private String chkpetshopping;
        private String culturecenter;
        private String fairday;
        private String infocentershopping;
        private String opendateshopping;
        private String opentime;
        private String parkingshopping;
        private String restdateshopping;
        private String restroom;
        private String saleitem;
        private String saleitemcost;
        private String scaleshopping;
        private String shopguide;

        // 필드 추가: contentTypeId = 39 (음식점)
        private String chkcreditcardfood;
        private String discountinfofood;
        private String firstmenu;
        private String infocenterfood;
        private String kidsfacility;
        private String opendatefood;
        private String opentimefood;
        private String packing;
        private String parkingfood;
        private String reservationfood;
        private String restdatefood;
        private String scalefood;
        private Integer seat;
        private String smoking;
        private String treatmenu;
        private String lcnsno;
    }

    // 호진님이 작업하셨었던 코드
//    private Response response;
//
//    @Getter
//    @Setter
//    public static class Response {
//        private Header header;
//        private Body body;
//
//        @Getter
//        @Setter
//        public static class Header {
//            private String resultCode;
//            private String resultMsg;
//        }
//
//        @Getter
//        @Setter
//        public static class Body {
//            private Items items;
//
//            @Getter
//            @Setter
//            public static class Items {
//                private List<AreaTour> item;
//            }
//        }
//    }
//
//    @Getter
//    @Setter
//    public static class AreaTour {
//        private String addr1;
//        private String addr2;
//        private int areacode;
//        private boolean booktour;
//        private String cat1;
//        private String cat2;
//        private String cat3;
//        private long contentid;
//        private int contenttypeid;
//        private String createdtime;
//        private String firstimage;
//        private String firstimage2;
//        private String cpyrhtDivCd;
//        private double mapx;
//        private double mapy;
//        private int mlevel;
//        private String modifiedtime;
//        private int sigungucode;
//        private String tel;
//        private String title;
//        private String zipcode;
//    }
}

