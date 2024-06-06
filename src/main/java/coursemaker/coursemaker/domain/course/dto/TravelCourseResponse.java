package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "여행 코스 응답 DTO")
@Data
public class TravelCourseResponse {

    @Schema(description = "코스 id")
    private Long id;

    @Schema(description = "코스 타이틀", example = "Course Title1")
    private final String title;

    @Schema(description = "코스 내용", example = "Course Content1")
    private final String content;

    @Schema(description = "코스 조회 수", example = "777")
    private final Integer views;

    @Schema(description = "여행 기간", example = "3")
    private final Integer duration;

    @Schema(description = "여행 인원", example = "5")
    private final Integer travelerCount;

    @Schema(description = "여행 타입", example = "0")
    private final Integer travelType;

    @Schema(description = "코스 대표 이미지 주소", example = "http://example.com/course1.jpg")
    private final String pictureLink;

    @Schema(description = "코스 여행지 목록")
    private final List<CourseDestinationResponse> courseDestinations;

    @Schema(description = "코스 태그 목록")
    private final List<TagResponseDto> courseTags;

    @Schema(description = "코스 만든 멤버")
    private final CourseMemberResponse member;

    public TravelCourseResponse(TravelCourse travelCourse, List<CourseDestinationResponse> courseDestinationResponses) {
        this.id = travelCourse.getId();
        this.title = travelCourse.getTitle();
        this.content = travelCourse.getContent();
        this.views = travelCourse.getViews();
        this.duration = travelCourse.getDuration();
        this.travelerCount = travelCourse.getTravelerCount();
        this.travelType = travelCourse.getTravelType();
        this.pictureLink = travelCourse.getPictureLink();
        this.member = new CourseMemberResponse(travelCourse.getMember());
        this.courseDestinations = courseDestinationResponses;
        this.courseTags = travelCourse.getCourseTags().stream()
                .map(courseTag -> {
                    TagResponseDto tagResponseDto = new TagResponseDto();
                    tagResponseDto.setId(courseTag.getTag().getId());
                    tagResponseDto.setName(courseTag.getTag().getName());
                    tagResponseDto.setDescription(courseTag.getTag().getDescription());
                    return tagResponseDto;
                })
                .collect(Collectors.toList());
    }
}