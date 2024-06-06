package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "여행 코스 수정 DTO")
@Data
public class UpdateTravelCourseRequest {

    @Schema(description = "코스 타이틀", example = "Course Title2")
    private String title;

    @Schema(description = "코스 내용", example = "Course Content2")
    private String content;

    @Schema(description = "여행 기간", example = "2")
    @Min(value = 1, message = "Duration should not be less than 1")
    @Max(value = 3)
    private Integer duration;

    @Schema(description = "여행 인원", example = "4")
    @Min(value = 1, message = "Traveler count should not be less than 1")
    private Integer travelerCount;

    @Schema(description = "여행 타입", example = "1")
    @Min(value = 0, message = "Travel type should not be negative")
    private Integer travelType;

    @Schema(description = "코스 대표 이미지 주소", example = "http://example.com/course2.jpg")
    @NotBlank(message = "Picture link is mandatory")
    private String pictureLink;

    @Schema(description = "코스 여행지 목록")
    private List<UpdateCourseDestinationRequest> courseDestinations;

    @Schema(description = "코스 태그 목록")
    private List<TagResponseDto> tags;

//    public TravelCourse toEntity() {
//        TravelCourse travelCourse = TravelCourse.builder()
//                .title(title)
//                .content(content)
//                .duration(duration)
//                .travelerCount(travelerCount)
//                .travelType(travelType)
//                .pictureLink(pictureLink)
//                .build();
//
//        for (UpdateCourseDestinationRequest courseDestination : courseDestinations) {
//            travelCourse.updateCourseDestination(courseDestination.toEntity());
//        }
//        return travelCourse;
//    }
}