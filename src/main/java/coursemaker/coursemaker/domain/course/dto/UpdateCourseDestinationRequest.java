package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "코스 여행지 수정 DTO")
@Data
public class UpdateCourseDestinationRequest {

    @Schema(description = "여행 순서", example = "1")
    private Short visitOrder;

    @Schema(description = "여행 일차 ex: 1일차, 2일차", example = "1")
    private Short date;

    @Schema(description = "여행지")
    private Destination destination;

    public CourseDestination toEntity() {
        return CourseDestination.builder()
                .visitOrder(visitOrder)
                .date(date)
                .destination(destination)
                .build();
    }
}