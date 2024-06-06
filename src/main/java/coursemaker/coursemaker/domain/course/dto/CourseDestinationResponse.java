package coursemaker.coursemaker.domain.course.dto;

import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "코스 여행지 응답 DTO")
@Data
public class CourseDestinationResponse {

    @Schema(description = "방문 순서", example = "1")
    private final Short visitOrder;

    @Schema(description = "여행 일차 ex: 1일차, 2일차", example = "1")
    private final Short date;

    @Schema(description = "여행지")
    private final DestinationDto destination;

    public CourseDestinationResponse(CourseDestination courseDestination, DestinationDto destinationDto) {
        this.visitOrder = courseDestination.getVisitOrder();
        this.date = courseDestination.getDate();
        this.destination = destinationDto;
    }
}