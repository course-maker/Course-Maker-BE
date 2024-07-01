package coursemaker.coursemaker.api.tourApi.controller;

import coursemaker.coursemaker.api.tourApi.dto.TourApiResponse;
import coursemaker.coursemaker.api.tourApi.entity.TourApi;
import coursemaker.coursemaker.api.tourApi.service.TourApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/tours")
@RequiredArgsConstructor
public class TourApiController {

    private final TourApiService tourApiService;


    @GetMapping("/update")
    public TourApiResponse updateAndGetTourData() {
        return tourApiService.updateAndGetTour();
    }

    @GetMapping("/{id}")
    public Optional<TourApi> getTourById(@PathVariable Long id) {
        return tourApiService.getTourById(id);
    }

    @GetMapping
    public List<TourApi> getTours() {
        return tourApiService.getAllTours();
    }

//    @GetMapping("/updateDisabledTours")
//    public void updateDisabledTours() {
//        tourApiService.updateDisabledTours();
//    }

    @GetMapping("/updateCommonData")
    public void updateCommonData() {
        tourApiService.updateTourWithCommonData();
    }

    @GetMapping("/updateIntroData")
    public void updateIntroData() {
        tourApiService.updateTourWithIntroData();
    }

    // 호진님이 작업하셨었던 코드
//    @GetMapping
//    public String test(Model model) {
//        String response = tourApiService.getByArea();
//        model.addAttribute("response", response);
//        return "tour-api-test";
//    }
//
//    @GetMapping
//    public String test2(Model model) {
//        AreaTourResponse response = tourApiService.getAreaTourList();
//        model.addAttribute("response", response);
//        return "tour-api-test";
//    }
}

