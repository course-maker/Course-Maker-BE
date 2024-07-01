package coursemaker.coursemaker.api.tourApi.service;

import coursemaker.coursemaker.api.tourApi.dto.TourApiResponse;
import coursemaker.coursemaker.api.tourApi.entity.TourApi;
import coursemaker.coursemaker.api.tourApi.repository.TourApiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.yaml.snakeyaml.util.UriEncoder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TourApiServiceImpl implements TourApiService {
    @Value("${tourapi.serviceKey}")
    private String serviceKey;

    @Value("${tourapi.baseUrl}")
    private String baseUrl;

    @Value("${tourapi.detailCommonUrl}")
    private String detailCommonUrl;

    @Value("${tourapi.detailIntroUrl}")
    private String detailIntroUrl;

    @Value("${tourapi.disableTourUrl}")
    private String disableTourUrl;


    private final RestTemplate restTemplate;
    private final TourApiRepository tourApiRepository;

    @Override
    public TourApiResponse updateAndGetTour() {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("numOfRows", 2300)
                .queryParam("pageNo", 1)
                .queryParam("MobileOS", "WIN")
                .queryParam("MobileApp", UriEncoder.encode("코스메이커"))
                .queryParam("_type", "json")
                .queryParam("areaCode", 6)
                .queryParam("serviceKey", serviceKey)
                .build(true)
                .toUri();

        TourApiResponse response = restTemplate.getForObject(uri, TourApiResponse.class);

        if (response != null && response.getResponse().getBody().getItems().getItem() != null) {
            List<TourApi> tourList = response.getResponse().getBody().getItems().getItem().stream()
                    .map(this::convertToEntity)
                    .toList();
            tourList.forEach(this::saveOrUpdateTour);
        }


        uri = UriComponentsBuilder.fromHttpUrl(disableTourUrl)
                .queryParam("numOfRows", 2300)
                .queryParam("pageNo", 1)
                .queryParam("MobileOS", "WIN")
                .queryParam("MobileApp", UriEncoder.encode("코스메이커"))
                .queryParam("_type", "json")
                .queryParam("areaCode", 6)
                .queryParam("serviceKey", serviceKey)
                .build(true)
                .toUri();

        response = restTemplate.getForObject(uri, TourApiResponse.class);

        if (response != null && response.getResponse().getBody().getItems().getItem() != null) {
            List<Long> disabledContentIds = response.getResponse().getBody().getItems().getItem().stream()
                    .map(TourApiResponse.Item::getContentid)
                    .toList();

            List<TourApi> allTours = tourApiRepository.findAll();
            allTours.forEach(tour -> {
                if (disabledContentIds.contains(tour.getContentid())) {
                    tour.setDisabled(1L);
                } else {
                    tour.setDisabled(null);
                }
                tourApiRepository.save(tour);
            });
        }


        return response;
    }

    @Override
    public List<TourApi> getAllTours() {
        return tourApiRepository.findAll();
    }



//    @Override
//    public void updateDisabledTours() {
//        URI uri = UriComponentsBuilder.fromHttpUrl(disableTourUrl)
//                .queryParam("numOfRows", 2300)
//                .queryParam("pageNo", 1)
//                .queryParam("MobileOS", "WIN")
//                .queryParam("MobileApp", UriEncoder.encode("코스메이커"))
//                .queryParam("_type", "json")
//                .queryParam("areaCode", 6)
//                .queryParam("serviceKey", serviceKey)
//                .build(true)
//                .toUri();
//
//        TourApiResponse response = restTemplate.getForObject(uri, TourApiResponse.class);
//
//        if (response != null && response.getResponse().getBody().getItems().getItem() != null) {
//            List<Long> disabledContentIds = response.getResponse().getBody().getItems().getItem().stream()
//                    .map(TourApiResponse.Item::getContentid)
//                    .toList();
//
//            List<TourApi> allTours = tourApiRepository.findAll();
//            allTours.forEach(tour -> {
//                if (disabledContentIds.contains(tour.getContentid())) {
//                    tour.setDisabled(1L);
//                } else {
//                    tour.setDisabled(null);
//                }
//                tourApiRepository.save(tour);
//            });
//        }
//    }

    @Override
    public Optional<TourApi> getTourById(Long id) {
        return tourApiRepository.findById(id);
    }

    @Override
    public void updateTourWithCommonData() {
        List<TourApi> tourList = tourApiRepository.findAll();
        for (long i = 1; i <= tourList.size(); i++) {
            Optional<TourApi> getTourApi = tourApiRepository.findById(i);
            long contentId = getTourApi.get().getContentid();


            URI uri = UriComponentsBuilder.fromHttpUrl(detailCommonUrl)
                    .queryParam("numOfRows", 2300)
                    .queryParam("pageNo", 1)
                    .queryParam("MobileOS", "WIN")
                    .queryParam("MobileApp", UriEncoder.encode("코스메이커"))
                    .queryParam("_type", "json")
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("defaultYN", "Y")
                    .queryParam("overviewYN", "Y")
                    .queryParam("contentId", contentId)
                    .build(true)
                    .toUri();

            TourApiResponse response = restTemplate.getForObject(uri, TourApiResponse.class);

            if (response != null && response.getResponse().getBody().getItems().getItem() != null) {
                response.getResponse().getBody().getItems().getItem().forEach(item -> {
                    Optional<TourApi> tourApiOptional = tourApiRepository.findByContentid(item.getContentid());
                    tourApiOptional.ifPresent(tourApi -> {
                        tourApi.setHomepage(item.getHomepage());
                        tourApi.setOverview(item.getOverview());
                        tourApiRepository.save(tourApi);
                    });
                });
            }
        }
    }

//    @Override
//    public void updateTourWithCommonData() {
//        List<TourApi> tourList = tourApiRepository.findAll();
//        for (long i = 1; i <= tourList.size(); i++) {
//            Optional<TourApi> getTourApi = tourApiRepository.findById(i);
//            if (getTourApi.isPresent()) {
//                long contentId = getTourApi.get().getContentid();
//                int contentTypeId = getTourApi.get().getContenttypeid();
//                // int pageNo = (int) Math.ceil(i / 100.0);
//
//                URI uri = UriComponentsBuilder.fromHttpUrl(detailIntroUrl)
//                        .queryParam("numOfRows", 2300)
//                        .queryParam("pageNo", 1)
//                        .queryParam("MobileOS", "WIN")
//                        .queryParam("MobileApp", UriEncoder.encode("코스메이커"))
//                        .queryParam("_type", "json")
//                        .queryParam("serviceKey", serviceKey)
//                        .queryParam("contentId", contentId)
//                        .queryParam("contentTypeId", contentTypeId)
//                        .build(true)
//                        .toUri();
//
//                try {
//                    // 응답을 문자열로 받아서 로그로 출력
//                    String jsonResponse = restTemplate.getForObject(uri, String.class);
//                    System.out.println("Response for contentId " + contentId + ": " + jsonResponse);
//
//                    // 문자열을 DTO로 변환
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    TourApiResponse response = objectMapper.readValue(jsonResponse, TourApiResponse.class);
//
//                    if (response != null && response.getResponse() != null && response.getResponse().getBody() != null
//                            && response.getResponse().getBody().getItems() != null
//                            && response.getResponse().getBody().getItems().getItem() != null) {
//
//                        response.getResponse().getBody().getItems().getItem().forEach(item -> {
//                            Optional<TourApi> tourApiOptional = tourApiRepository.findByContentid(item.getContentid());
//                            tourApiOptional.ifPresent(tourApi -> {
//                                // contentTypeId에 따라 다른 컬럼 업데이트
//                                if (contentTypeId == 12) {
//                                    tourApi.setAccomcount(item.getAccomcount());
//                                    tourApi.setChkbabycarriage(item.getChkbabycarriage());
//                                    tourApi.setChkcreditcard(item.getChkcreditcard());
//                                    tourApi.setChkpet(item.getChkpet());
//                                    tourApi.setExpagerange(item.getExpagerange());
//                                    tourApi.setExpguide(item.getExpguide());
//                                    tourApi.setHeritage1(item.getHeritage1());
//                                    tourApi.setHeritage2(item.getHeritage2());
//                                    tourApi.setHeritage3(item.getHeritage3());
//                                    tourApi.setInfocenter(item.getInfocenter());
//                                    tourApi.setOpendate(item.getOpendate());
//                                    tourApi.setParking(item.getParking());
//                                    tourApi.setRestdate(item.getRestdate());
//                                    tourApi.setUseseason(item.getUseseason());
//                                    tourApi.setUsetime(item.getUsetime());
//                                } else if (contentTypeId == 14) {
//                                    tourApi.setAccomcountculture(item.getAccomcountculture());
//                                    tourApi.setChkbabycarriageculture(item.getChkbabycarriageculture());
//                                    tourApi.setChkcreditcardculture(item.getChkcreditcardculture());
//                                    tourApi.setChkpetculture(item.getChkpetculture());
//                                    tourApi.setDiscountinfo(item.getDiscountinfo());
//                                    tourApi.setInfocenterculture(item.getInfocenterculture());
//                                    tourApi.setParkingculture(item.getParkingculture());
//                                    tourApi.setParkingfee(item.getParkingfee());
//                                    tourApi.setRestdateculture(item.getRestdateculture());
//                                    tourApi.setUsefee(item.getUsefee());
//                                    tourApi.setUsetimeculture(item.getUsetimeculture());
//                                    tourApi.setScale(item.getScale());
//                                    tourApi.setSpendtime(item.getSpendtime());
//                                } else if (contentTypeId == 15) {
//                                    tourApi.setAgelimit(item.getAgelimit());
//                                    tourApi.setBookingplace(item.getBookingplace());
//                                    tourApi.setDiscountinfofestival(item.getDiscountinfofestival());
//                                    tourApi.setEventenddate(item.getEventenddate());
//                                    tourApi.setEventhomepage(item.getEventhomepage());
//                                    tourApi.setEventplace(item.getEventplace());
//                                    tourApi.setEventstartdate(item.getEventstartdate());
//                                    tourApi.setFestivalgrade(item.getFestivalgrade());
//                                    tourApi.setPlaceinfo(item.getPlaceinfo());
//                                    tourApi.setPlaytime(item.getPlaytime());
//                                    tourApi.setProgram(item.getProgram());
//                                    tourApi.setSpendtimefestival(item.getSpendtimefestival());
//                                    tourApi.setSponsor1(item.getSponsor1());
//                                    tourApi.setSponsor1tel(item.getSponsor1tel());
//                                    tourApi.setSponsor2(item.getSponsor2());
//                                    tourApi.setSponsor2tel(item.getSponsor2tel());
//                                    tourApi.setSubevent(item.getSubevent());
//                                    tourApi.setUsetimefestival(item.getUsetimefestival());
//                                } else if (contentTypeId == 25) {
//                                    tourApi.setDistance(item.getDistance());
//                                    tourApi.setInfocentertourcourse(item.getInfocentertourcourse());
//                                    tourApi.setSchedule(item.getSchedule());
//                                    tourApi.setTaketime(item.getTaketime());
//                                    tourApi.setTheme(item.getTheme());
//                                } else if (contentTypeId == 28) {
//                                    tourApi.setAccomcountleports(item.getAccomcountleports());
//                                    tourApi.setChkbabycarriageleports(item.getChkbabycarriageleports());
//                                    tourApi.setChkcreditcardleports(item.getChkcreditcardleports());
//                                    tourApi.setChkpetleports(item.getChkpetleports());
//                                    tourApi.setExpagerangeleports(item.getExpagerangeleports());
//                                    tourApi.setInfocenterleports(item.getInfocenterleports());
//                                    tourApi.setOpenperiod(item.getOpenperiod());
//                                    tourApi.setParkingfeeleports(item.getParkingfeeleports());
//                                    tourApi.setParkingleports(item.getParkingleports());
//                                    tourApi.setReservation(item.getReservation());
//                                    tourApi.setRestdateleports(item.getRestdateleports());
//                                    tourApi.setScaleleports(item.getScaleleports());
//                                    tourApi.setUsefeeleports(item.getUsefeeleports());
//                                    tourApi.setUsetimeleports(item.getUsetimeleports());
//                                } else if (contentTypeId == 32) {
//                                    tourApi.setAccomcountlodging(item.getAccomcountlodging());
//                                    tourApi.setBenikia(item.getBenikia());
//                                    tourApi.setCheckintime(item.getCheckintime());
//                                    tourApi.setCheckouttime(item.getCheckouttime());
//                                    tourApi.setChkcooking(item.getChkcooking());
//                                    tourApi.setFoodplace(item.getFoodplace());
//                                    tourApi.setGoodstay(item.getGoodstay());
//                                    tourApi.setHanok(item.getHanok());
//                                    tourApi.setInfocenterlodging(item.getInfocenterlodging());
//                                    tourApi.setParkinglodging(item.getParkinglodging());
//                                    tourApi.setPickup(item.getPickup());
//                                    tourApi.setRoomcount(item.getRoomcount());
//                                    tourApi.setReservationlodging(item.getReservationlodging());
//                                    tourApi.setReservationurl(item.getReservationurl());
//                                    tourApi.setRoomtype(item.getRoomtype());
//                                    tourApi.setScalelodging(item.getScalelodging());
//                                    tourApi.setSubfacility(item.getSubfacility());
//                                    tourApi.setBarbecue(item.getBarbecue());
//                                    tourApi.setBeauty(item.getBeauty());
//                                    tourApi.setBeverage(item.getBeverage());
//                                    tourApi.setBicycle(item.getBicycle());
//                                    tourApi.setCampfire(item.getCampfire());
//                                    tourApi.setFitness(item.getFitness());
//                                    tourApi.setKaraoke(item.getKaraoke());
//                                    tourApi.setPublicbath(item.getPublicbath());
//                                    tourApi.setPublicpc(item.getPublicpc());
//                                    tourApi.setSauna(item.getSauna());
//                                    tourApi.setSeminar(item.getSeminar());
//                                    tourApi.setSports(item.getSports());
//                                    tourApi.setRefundregulation(item.getRefundregulation());
//                                } else if (contentTypeId == 38) {
//                                    tourApi.setChkbabycarriageshopping(item.getChkbabycarriageshopping());
//                                    tourApi.setChkcreditcardshopping(item.getChkcreditcardshopping());
//                                    tourApi.setChkpetshopping(item.getChkpetshopping());
//                                    tourApi.setCulturecenter(item.getCulturecenter());
//                                    tourApi.setFairday(item.getFairday());
//                                    tourApi.setInfocentershopping(item.getInfocentershopping());
//                                    tourApi.setOpendateshopping(item.getOpendateshopping());
//                                    tourApi.setOpentime(item.getOpentime());
//                                    tourApi.setParkingshopping(item.getParkingshopping());
//                                    tourApi.setRestdateshopping(item.getRestdateshopping());
//                                    tourApi.setRestroom(item.getRestroom());
//                                    tourApi.setSaleitem(item.getSaleitem());
//                                    tourApi.setSaleitemcost(item.getSaleitemcost());
//                                    tourApi.setScaleshopping(item.getScaleshopping());
//                                    tourApi.setShopguide(item.getShopguide());
//                                } else if (contentTypeId == 39) {
//                                    tourApi.setChkcreditcardfood(item.getChkcreditcardfood());
//                                    tourApi.setDiscountinfofood(item.getDiscountinfofood());
//                                    tourApi.setFirstmenu(item.getFirstmenu());
//                                    tourApi.setInfocenterfood(item.getInfocenterfood());
//                                    tourApi.setKidsfacility(item.getKidsfacility());
//                                    tourApi.setOpendatefood(item.getOpendatefood());
//                                    tourApi.setOpentimefood(item.getOpentimefood());
//                                    tourApi.setPacking(item.getPacking());
//                                    tourApi.setParkingfood(item.getParkingfood());
//                                    tourApi.setReservationfood(item.getReservationfood());
//                                    tourApi.setRestdatefood(item.getRestdatefood());
//                                    tourApi.setScalefood(item.getScalefood());
//                                    tourApi.setSeat(item.getSeat());
//                                    tourApi.setSmoking(item.getSmoking());
//                                    tourApi.setTreatmenu(item.getTreatmenu());
//                                    tourApi.setLcnsno(item.getLcnsno());
//                                }
//                                tourApiRepository.save(tourApi);
//                            });
//                        });
//                    } else {
//                        System.out.println("Response for contentId " + contentId + " is empty or malformed.");
//                    }
//                } catch (Exception e) {
//                    System.err.println("Error processing contentId " + contentId + ": " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    @Override
    public void updateTourWithIntroData() {
        List<TourApi> tourList = tourApiRepository.findAll();
        for (long i = 1; i <= tourList.size(); i++) {
            Optional<TourApi> getTourApi = tourApiRepository.findById(i);
            long contentId = getTourApi.get().getContentid();
            int contentTypeId = getTourApi.get().getContenttypeid();

            URI uri = UriComponentsBuilder.fromHttpUrl(detailIntroUrl)
                    .queryParam("numOfRows", 2300)
                    .queryParam("pageNo", 1)
                    .queryParam("MobileOS", "WIN")
                    .queryParam("MobileApp", UriEncoder.encode("코스메이커"))
                    .queryParam("_type", "json")
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("contentId", contentId)
                    .queryParam("contentTypeId", contentTypeId)
                    .build(true)
                    .toUri();

            TourApiResponse response = restTemplate.getForObject(uri, TourApiResponse.class);

            if (response != null && response.getResponse().getBody().getItems().getItem() != null) {
                response.getResponse().getBody().getItems().getItem().forEach(item -> {
                    Optional<TourApi> tourApiOptional = tourApiRepository.findByContentid(item.getContentid());
                    tourApiOptional.ifPresent(tourApi -> {
                        // contentTypeId에 따라 다른 컬럼 업데이트
                        if (contentTypeId == 12) {
                            tourApi.setAccomcount(item.getAccomcount());
                            tourApi.setChkbabycarriage(item.getChkbabycarriage());
                            tourApi.setChkcreditcard(item.getChkcreditcard());
                            tourApi.setChkpet(item.getChkpet());
                            tourApi.setExpagerange(item.getExpagerange());
                            tourApi.setExpguide(item.getExpguide());
                            tourApi.setHeritage1(item.getHeritage1());
                            tourApi.setHeritage2(item.getHeritage2());
                            tourApi.setHeritage3(item.getHeritage3());
                            tourApi.setInfocenter(item.getInfocenter());
                            tourApi.setOpendate(item.getOpendate());
                            tourApi.setParking(item.getParking());
                            tourApi.setRestdate(item.getRestdate());
                            tourApi.setUseseason(item.getUseseason());
                            tourApi.setUsetime(item.getUsetime());
                        } else if (contentTypeId == 14) {
                            tourApi.setAccomcountculture(item.getAccomcountculture());
                            tourApi.setChkbabycarriageculture(item.getChkbabycarriageculture());
                            tourApi.setChkcreditcardculture(item.getChkcreditcardculture());
                            tourApi.setChkpetculture(item.getChkpetculture());
                            tourApi.setDiscountinfo(item.getDiscountinfo());
                            tourApi.setInfocenterculture(item.getInfocenterculture());
                            tourApi.setParkingculture(item.getParkingculture());
                            tourApi.setParkingfee(item.getParkingfee());
                            tourApi.setRestdateculture(item.getRestdateculture());
                            tourApi.setUsefee(item.getUsefee());
                            tourApi.setUsetimeculture(item.getUsetimeculture());
                            tourApi.setScale(item.getScale());
                            tourApi.setSpendtime(item.getSpendtime());
                        } else if (contentTypeId == 15) {
                            tourApi.setAgelimit(item.getAgelimit());
                            tourApi.setBookingplace(item.getBookingplace());
                            tourApi.setDiscountinfofestival(item.getDiscountinfofestival());
                            tourApi.setEventenddate(item.getEventenddate());
                            tourApi.setEventhomepage(item.getEventhomepage());
                            tourApi.setEventplace(item.getEventplace());
                            tourApi.setEventstartdate(item.getEventstartdate());
                            tourApi.setFestivalgrade(item.getFestivalgrade());
                            tourApi.setPlaceinfo(item.getPlaceinfo());
                            tourApi.setPlaytime(item.getPlaytime());
                            tourApi.setProgram(item.getProgram());
                            tourApi.setSpendtimefestival(item.getSpendtimefestival());
                            tourApi.setSponsor1(item.getSponsor1());
                            tourApi.setSponsor1tel(item.getSponsor1tel());
                            tourApi.setSponsor2(item.getSponsor2());
                            tourApi.setSponsor2tel(item.getSponsor2tel());
                            tourApi.setSubevent(item.getSubevent());
                            tourApi.setUsetimefestival(item.getUsetimefestival());
                        } else if (contentTypeId == 25) {
                            tourApi.setDistance(item.getDistance());
                            tourApi.setInfocentertourcourse(item.getInfocentertourcourse());
                            tourApi.setSchedule(item.getSchedule());
                            tourApi.setTaketime(item.getTaketime());
                            tourApi.setTheme(item.getTheme());
                        } else if (contentTypeId == 28) {
                            tourApi.setAccomcountleports(item.getAccomcountleports());
                            tourApi.setChkbabycarriageleports(item.getChkbabycarriageleports());
                            tourApi.setChkcreditcardleports(item.getChkcreditcardleports());
                            tourApi.setChkpetleports(item.getChkpetleports());
                            tourApi.setExpagerangeleports(item.getExpagerangeleports());
                            tourApi.setInfocenterleports(item.getInfocenterleports());
                            tourApi.setOpenperiod(item.getOpenperiod());
                            tourApi.setParkingfeeleports(item.getParkingfeeleports());
                            tourApi.setParkingleports(item.getParkingleports());
                            tourApi.setReservation(item.getReservation());
                            tourApi.setRestdateleports(item.getRestdateleports());
                            tourApi.setScaleleports(item.getScaleleports());
                            tourApi.setUsefeeleports(item.getUsefeeleports());
                            tourApi.setUsetimeleports(item.getUsetimeleports());
                        } else if (contentTypeId == 32) {
                            tourApi.setAccomcountlodging(item.getAccomcountlodging());
                            tourApi.setBenikia(item.getBenikia());
                            tourApi.setCheckintime(item.getCheckintime());
                            tourApi.setCheckouttime(item.getCheckouttime());
                            tourApi.setChkcooking(item.getChkcooking());
                            tourApi.setFoodplace(item.getFoodplace());
                            tourApi.setGoodstay(item.getGoodstay());
                            tourApi.setHanok(item.getHanok());
                            tourApi.setInfocenterlodging(item.getInfocenterlodging());
                            tourApi.setParkinglodging(item.getParkinglodging());
                            tourApi.setPickup(item.getPickup());
                            tourApi.setRoomcount(item.getRoomcount());
                            tourApi.setReservationlodging(item.getReservationlodging());
                            tourApi.setReservationurl(item.getReservationurl());
                            tourApi.setRoomtype(item.getRoomtype());
                            tourApi.setScalelodging(item.getScalelodging());
                            tourApi.setSubfacility(item.getSubfacility());
                            tourApi.setBarbecue(item.getBarbecue());
                            tourApi.setBeauty(item.getBeauty());
                            tourApi.setBeverage(item.getBeverage());
                            tourApi.setBicycle(item.getBicycle());
                            tourApi.setCampfire(item.getCampfire());
                            tourApi.setFitness(item.getFitness());
                            tourApi.setKaraoke(item.getKaraoke());
                            tourApi.setPublicbath(item.getPublicbath());
                            tourApi.setPublicpc(item.getPublicpc());
                            tourApi.setSauna(item.getSauna());
                            tourApi.setSeminar(item.getSeminar());
                            tourApi.setSports(item.getSports());
                            tourApi.setRefundregulation(item.getRefundregulation());
                        } else if (contentTypeId == 38) {
                            tourApi.setChkbabycarriageshopping(item.getChkbabycarriageshopping());
                            tourApi.setChkcreditcardshopping(item.getChkcreditcardshopping());
                            tourApi.setChkpetshopping(item.getChkpetshopping());
                            tourApi.setCulturecenter(item.getCulturecenter());
                            tourApi.setFairday(item.getFairday());
                            tourApi.setInfocentershopping(item.getInfocentershopping());
                            tourApi.setOpendateshopping(item.getOpendateshopping());
                            tourApi.setOpentime(item.getOpentime());
                            tourApi.setParkingshopping(item.getParkingshopping());
                            tourApi.setRestdateshopping(item.getRestdateshopping());
                            tourApi.setRestroom(item.getRestroom());
                            tourApi.setSaleitem(item.getSaleitem());
                            tourApi.setSaleitemcost(item.getSaleitemcost());
                            tourApi.setScaleshopping(item.getScaleshopping());
                            tourApi.setShopguide(item.getShopguide());
                        } else if (contentTypeId == 39) {
                            tourApi.setChkcreditcardfood(item.getChkcreditcardfood());
                            tourApi.setDiscountinfofood(item.getDiscountinfofood());
                            tourApi.setFirstmenu(item.getFirstmenu());
                            tourApi.setInfocenterfood(item.getInfocenterfood());
                            tourApi.setKidsfacility(item.getKidsfacility());
                            tourApi.setOpendatefood(item.getOpendatefood());
                            tourApi.setOpentimefood(item.getOpentimefood());
                            tourApi.setPacking(item.getPacking());
                            tourApi.setParkingfood(item.getParkingfood());
                            tourApi.setReservationfood(item.getReservationfood());
                            tourApi.setRestdatefood(item.getRestdatefood());
                            tourApi.setScalefood(item.getScalefood());
                            tourApi.setSeat(item.getSeat());
                            tourApi.setSmoking(item.getSmoking());
                            tourApi.setTreatmenu(item.getTreatmenu());
                            tourApi.setLcnsno(item.getLcnsno());
                        }
                        tourApiRepository.save(tourApi);
                    });
                });
            }
        }


    }

    private TourApi convertToEntity(TourApiResponse.Item item) {
        return TourApi.builder()
                .title(item.getTitle())
                .contentid(item.getContentid())
                .tel(item.getTel())
                .addr1(item.getAddr1())
                .addr2(item.getAddr2())
                .sigungucode(item.getSigungucode())
                .firstimage(item.getFirstimage())
                .firstimage2(item.getFirstimage2())
                .mapx(item.getMapx())
                .mapy(item.getMapy())
                .zipcode(item.getZipcode())
                .createdtime(item.getCreatedtime())
                .modifiedtime(item.getModifiedtime())
                .cat1(item.getCat1())
                .cat2(item.getCat2())
                .cat3(item.getCat3())
                .contenttypeid(item.getContenttypeid())
                .homepage(item.getHomepage())
                .overview(item.getOverview())
                .build();
    }

    private void saveOrUpdateTour(TourApi tourApi) {
        Optional<TourApi> existingTour = tourApiRepository.findByContentid(tourApi.getContentid());
        if (existingTour.isPresent()) {
            TourApi existing = existingTour.get();
            existing.setTitle(tourApi.getTitle());
            existing.setTel(tourApi.getTel());
            existing.setAddr1(tourApi.getAddr1());
            existing.setAddr2(tourApi.getAddr2());
            existing.setSigungucode(tourApi.getSigungucode());
            existing.setFirstimage(tourApi.getFirstimage());
            existing.setFirstimage2(tourApi.getFirstimage2());
            existing.setMapx(tourApi.getMapx());
            existing.setMapy(tourApi.getMapy());
            existing.setZipcode(tourApi.getZipcode());
            existing.setCreatedtime(tourApi.getCreatedtime());
            existing.setModifiedtime(tourApi.getModifiedtime());
            existing.setCat1(tourApi.getCat1());
            existing.setCat2(tourApi.getCat2());
            existing.setCat3(tourApi.getCat3());
            existing.setContenttypeid(tourApi.getContenttypeid());
            existing.setHomepage(tourApi.getHomepage());
            existing.setOverview(tourApi.getOverview());

            // 업데이트된 필드들 추가
            existing.setAccomcount(tourApi.getAccomcount());
            existing.setChkbabycarriage(tourApi.getChkbabycarriage());
            existing.setChkcreditcard(tourApi.getChkcreditcard());
            existing.setChkpet(tourApi.getChkpet());
            existing.setExpagerange(tourApi.getExpagerange());
            existing.setExpguide(tourApi.getExpguide());
            existing.setHeritage1(tourApi.getHeritage1());
            existing.setHeritage2(tourApi.getHeritage2());
            existing.setHeritage3(tourApi.getHeritage3());
            existing.setInfocenter(tourApi.getInfocenter());
            existing.setOpendate(tourApi.getOpendate());
            existing.setParking(tourApi.getParking());
            existing.setRestdate(tourApi.getRestdate());
            existing.setUseseason(tourApi.getUseseason());
            existing.setUsetime(tourApi.getUsetime());

            existing.setAccomcountculture(tourApi.getAccomcountculture());
            existing.setChkbabycarriageculture(tourApi.getChkbabycarriageculture());
            existing.setChkcreditcardculture(tourApi.getChkcreditcardculture());
            existing.setChkpetculture(tourApi.getChkpetculture());
            existing.setDiscountinfo(tourApi.getDiscountinfo());
            existing.setInfocenterculture(tourApi.getInfocenterculture());
            existing.setParkingculture(tourApi.getParkingculture());
            existing.setParkingfee(tourApi.getParkingfee());
            existing.setRestdateculture(tourApi.getRestdateculture());
            existing.setUsefee(tourApi.getUsefee());
            existing.setUsetimeculture(tourApi.getUsetimeculture());
            existing.setScale(tourApi.getScale());
            existing.setSpendtime(tourApi.getSpendtime());

            existing.setAgelimit(tourApi.getAgelimit());
            existing.setBookingplace(tourApi.getBookingplace());
            existing.setDiscountinfofestival(tourApi.getDiscountinfofestival());
            existing.setEventenddate(tourApi.getEventenddate());
            existing.setEventhomepage(tourApi.getEventhomepage());
            existing.setEventplace(tourApi.getEventplace());
            existing.setEventstartdate(tourApi.getEventstartdate());
            existing.setFestivalgrade(tourApi.getFestivalgrade());
            existing.setPlaceinfo(tourApi.getPlaceinfo());
            existing.setPlaytime(tourApi.getPlaytime());
            existing.setProgram(tourApi.getProgram());
            existing.setSpendtimefestival(tourApi.getSpendtimefestival());
            existing.setSponsor1(tourApi.getSponsor1());
            existing.setSponsor1tel(tourApi.getSponsor1tel());
            existing.setSponsor2(tourApi.getSponsor2());
            existing.setSponsor2tel(tourApi.getSponsor2tel());
            existing.setSubevent(tourApi.getSubevent());
            existing.setUsetimefestival(tourApi.getUsetimefestival());

            existing.setDistance(tourApi.getDistance());
            existing.setInfocentertourcourse(tourApi.getInfocentertourcourse());
            existing.setSchedule(tourApi.getSchedule());
            existing.setTaketime(tourApi.getTaketime());
            existing.setTheme(tourApi.getTheme());

            existing.setAccomcountleports(tourApi.getAccomcountleports());
            existing.setChkbabycarriageleports(tourApi.getChkbabycarriageleports());
            existing.setChkcreditcardleports(tourApi.getChkcreditcardleports());
            existing.setChkpetleports(tourApi.getChkpetleports());
            existing.setExpagerangeleports(tourApi.getExpagerangeleports());
            existing.setInfocenterleports(tourApi.getInfocenterleports());
            existing.setOpenperiod(tourApi.getOpenperiod());
            existing.setParkingfeeleports(tourApi.getParkingfeeleports());
            existing.setParkingleports(tourApi.getParkingleports());
            existing.setReservation(tourApi.getReservation());
            existing.setRestdateleports(tourApi.getRestdateleports());
            existing.setScaleleports(tourApi.getScaleleports());
            existing.setUsefeeleports(tourApi.getUsefeeleports());
            existing.setUsetimeleports(tourApi.getUsetimeleports());

            existing.setAccomcountlodging(tourApi.getAccomcountlodging());
            existing.setBenikia(tourApi.getBenikia());
            existing.setCheckintime(tourApi.getCheckintime());
            existing.setCheckouttime(tourApi.getCheckouttime());
            existing.setChkcooking(tourApi.getChkcooking());
            existing.setFoodplace(tourApi.getFoodplace());
            existing.setGoodstay(tourApi.getGoodstay());
            existing.setHanok(tourApi.getHanok());
            existing.setInfocenterlodging(tourApi.getInfocenterlodging());
            existing.setParkinglodging(tourApi.getParkinglodging());
            existing.setPickup(tourApi.getPickup());
            existing.setRoomcount(tourApi.getRoomcount());
            existing.setReservationlodging(tourApi.getReservationlodging());
            existing.setReservationurl(tourApi.getReservationurl());
            existing.setRoomtype(tourApi.getRoomtype());
            existing.setScalelodging(tourApi.getScalelodging());
            existing.setSubfacility(tourApi.getSubfacility());
            existing.setBarbecue(tourApi.getBarbecue());
            existing.setBeauty(tourApi.getBeauty());
            existing.setBeverage(tourApi.getBeverage());
            existing.setBicycle(tourApi.getBicycle());
            existing.setCampfire(tourApi.getCampfire());
            existing.setFitness(tourApi.getFitness());
            existing.setKaraoke(tourApi.getKaraoke());
            existing.setPublicbath(tourApi.getPublicbath());
            existing.setPublicpc(tourApi.getPublicpc());
            existing.setSauna(tourApi.getSauna());
            existing.setSeminar(tourApi.getSeminar());
            existing.setSports(tourApi.getSports());
            existing.setRefundregulation(tourApi.getRefundregulation());

            existing.setChkbabycarriageshopping(tourApi.getChkbabycarriageshopping());
            existing.setChkcreditcardshopping(tourApi.getChkcreditcardshopping());
            existing.setChkpetshopping(tourApi.getChkpetshopping());
            existing.setCulturecenter(tourApi.getCulturecenter());
            existing.setFairday(tourApi.getFairday());
            existing.setInfocentershopping(tourApi.getInfocentershopping());
            existing.setOpendateshopping(tourApi.getOpendateshopping());
            existing.setOpentime(tourApi.getOpentime());
            existing.setParkingshopping(tourApi.getParkingshopping());
            existing.setRestdateshopping(tourApi.getRestdateshopping());
            existing.setRestroom(tourApi.getRestroom());
            existing.setSaleitem(tourApi.getSaleitem());
            existing.setSaleitemcost(tourApi.getSaleitemcost());
            existing.setScaleshopping(tourApi.getScaleshopping());
            existing.setShopguide(tourApi.getShopguide());

            existing.setChkcreditcardfood(tourApi.getChkcreditcardfood());
            existing.setDiscountinfofood(tourApi.getDiscountinfofood());
            existing.setFirstmenu(tourApi.getFirstmenu());
            existing.setInfocenterfood(tourApi.getInfocenterfood());
            existing.setKidsfacility(tourApi.getKidsfacility());
            existing.setOpendatefood(tourApi.getOpendatefood());
            existing.setOpentimefood(tourApi.getOpentimefood());
            existing.setPacking(tourApi.getPacking());
            existing.setParkingfood(tourApi.getParkingfood());
            existing.setReservationfood(tourApi.getReservationfood());
            existing.setRestdatefood(tourApi.getRestdatefood());
            existing.setScalefood(tourApi.getScalefood());
            existing.setSeat(tourApi.getSeat());
            existing.setSmoking(tourApi.getSmoking());
            existing.setTreatmenu(tourApi.getTreatmenu());
            existing.setLcnsno(tourApi.getLcnsno());
            tourApiRepository.save(existing);
        } else {
            tourApiRepository.save(tourApi);
        }
    }
}

