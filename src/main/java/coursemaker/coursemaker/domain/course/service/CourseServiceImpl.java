package coursemaker.coursemaker.domain.course.service;

import coursemaker.coursemaker.domain.course.dto.*;
import coursemaker.coursemaker.domain.course.entity.CourseDestination;
import coursemaker.coursemaker.domain.course.exception.IllegalTravelCourseArgumentException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseDuplicatedException;
import coursemaker.coursemaker.domain.course.exception.TravelCourseNotFoundException;
import coursemaker.coursemaker.domain.course.repository.CourseDestinationRepository;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.service.CourseDestinationService;
import coursemaker.coursemaker.domain.course.repository.TravelCourseRepository;
import coursemaker.coursemaker.domain.destination.dto.DestinationDto;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.member.entity.Member;
import coursemaker.coursemaker.domain.member.service.MemberService;
import coursemaker.coursemaker.domain.tag.dto.TagResponseDto;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.service.TagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceImpl implements CourseService{

    private final CourseDestinationRepository courseDestinationRepository;

    private final TravelCourseRepository travelCourseRepository;


    private final TagService tagService;

    private final DestinationService destinationService;

    private final MemberService memberService;

    public CourseServiceImpl(CourseDestinationRepository courseDestinationRepository,
                             TravelCourseRepository travelCourseRepository,
                             @Lazy TagService tagService,
                             DestinationService destinationService, 
                             MemberService memberService) {
        this.courseDestinationRepository = courseDestinationRepository;
        this.travelCourseRepository = travelCourseRepository;
        this.tagService = tagService;
        this.destinationService = destinationService;
        this.memberService = memberService;
    }

    @Override
    public TravelCourse save(AddTravelCourseRequest request) {
        Optional<TravelCourse> existingCourse = travelCourseRepository.findByTitle(request.getTitle());
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 이름이 존재하지 않습니다.", "title is empty");
        }

        if (existingCourse.isPresent()) {
            throw new TravelCourseDuplicatedException("이미 존재하는 코스입니다. ", "코스 이름: " + request.getTitle());
        }

        if (request.getContent() == null || request.getContent().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 내용이 존재하지 않습니다.", "course is empty");
        }

        if (request.getDuration() == null ) {
            throw new IllegalTravelCourseArgumentException("여행 기간이 존재하지 않습니다.", "duration is null");
        }

        if (request.getDuration() > 3 || request.getDuration() < 1) {
            throw new IllegalTravelCourseArgumentException("여행 기간은 1~3일 사이 입니다.", "duration: " + request.getDuration());
        }

        if (request.getTravelerCount() == null || request.getTravelerCount() < 1) {
            throw new IllegalTravelCourseArgumentException("여행 인원이 존재하지 않습니다.", "traveler count: " + request.getTravelerCount());
        }

        if (request.getTravelType() == null) {
            throw new IllegalTravelCourseArgumentException("여행 타입이 존재하지 않습니다.", "traveler type is null");
        }

        if (request.getPictureLink() == null || request.getPictureLink().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("이미지 링크가 존재하지 않습니다.", "image link is empty");
        }

        if (request.getCourseDestinations() == null || request.getCourseDestinations().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 여행지가 존재하지 않습니다.", "course destination is empty");
        }

        if(request.getTags() == null || request.getTags().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 태그가 존재하지 않습니다.", "tag is empty");
        }


        // TODO: ROW MAPPER로 엔티티 - DTO 매핑
        /***************DTO - entity 변환**************/

        /*travel course 설정*/
        Member member = memberService.findByNickname(request.getNickname());
        TravelCourse travelCourse = TravelCourse.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .duration(request.getDuration())
                .travelerCount(request.getTravelerCount())
                .travelType(request.getTravelType())
                .pictureLink(request.getPictureLink())
                .member(member)
                .build();
        travelCourse = travelCourseRepository.save(travelCourse);

        /*destination 설정*/
        for (AddCourseDestinationRequest courseDestination : request.getCourseDestinations()) {

            CourseDestination courseDestinationEntity = CourseDestination.builder()
                    .date(courseDestination.getDate())
                    .visitOrder(courseDestination.getVisitOrder())
                    .build();
            courseDestinationEntity.setTravelCourse(travelCourse);

            Destination destination = destinationService.findById(courseDestination.getDestination().getId());

            courseDestinationEntity.setDestination(destination);
            courseDestinationRepository.save(courseDestinationEntity);
        }


        /*태그 설정*/
        List<Long> tagIds = request.getTags().stream()
                .map(TagResponseDto::getId)
                .collect(Collectors.toList());
        tagService.addTagsByCourse(travelCourse.getId(), tagIds);


        return travelCourse;
    }

    @Override
    public List<TravelCourse> findAll() {
        return travelCourseRepository.findAll();
    }

    @Override
    public List<TravelCourse> getAllOrderByViewsDesc(Pageable pageable) {
        return travelCourseRepository.findAllByOrderByViewsDesc(pageable);
    }

    @Override
    public TravelCourse findById(Long id) {
        return travelCourseRepository.findById(id)
                .orElseThrow(() -> new TravelCourseNotFoundException("존재하지 않는 코스입니다.", "Course ID: " + id));
    }

    @Override
    public TravelCourse update(Long id, AddTravelCourseRequest request) {

        Optional<TravelCourse> existingCourse = travelCourseRepository.findByTitle(request.getTitle());

        travelCourseRepository.findById(id).orElseThrow(() -> new TravelCourseNotFoundException("수정할 코스가 존재하지 않습니다..", "course ID: " + id));

        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 이름이 존재하지 않습니다.", "title is empty");
        }


        if (request.getContent() == null || request.getContent().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 내용이 존재하지 않습니다.", "course is empty");
        }

        if (request.getDuration() == null ) {
            throw new IllegalTravelCourseArgumentException("여행 기간이 존재하지 않습니다.", "duration is null");
        }

        if (request.getDuration() > 3 || request.getDuration() < 1) {
            throw new IllegalTravelCourseArgumentException("여행 기간은 1~3일 사이 입니다.", "duration: " + request.getDuration());
        }

        if (request.getTravelerCount() == null || request.getTravelerCount() < 1) {
            throw new IllegalTravelCourseArgumentException("여행 인원이 존재하지 않습니다.", "traveler count: " + request.getTravelerCount());
        }

        if (request.getTravelType() == null) {
            throw new IllegalTravelCourseArgumentException("여행 타입이 존재하지 않습니다.", "traveler type is null");
        }

        if (request.getPictureLink() == null || request.getPictureLink().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("이미지 링크가 존재하지 않습니다.", "image link is empty");
        }

        if (request.getCourseDestinations() == null || request.getCourseDestinations().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 여행지가 존재하지 않습니다.", "course destination is empty");
        }

        if(request.getTags() == null || request.getTags().isEmpty()) {
            throw new IllegalTravelCourseArgumentException("코스 태그가 존재하지 않습니다.", "tag is empty");
        }


        // TODO: ROW MAPPER로 엔티티 - DTO 매핑
        /***************DTO - entity 변환**************/

        /*travel course 설정*/
        /*TODO: 멤버 닉네임을 기반으로 객체 가져오는부분 연결하기!*/
        Member member = memberService.findByNickname(request.getNickname());
        TravelCourse travelCourse = TravelCourse.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .duration(request.getDuration())
                .travelerCount(request.getTravelerCount())
                .travelType(request.getTravelType())
                .pictureLink(request.getPictureLink())
                .member(member)
                .build();
        travelCourse.setId(id);// id가 있으면 update함
        travelCourse = travelCourseRepository.save(travelCourse);

        /*destination 설정*/
        courseDestinationRepository.deleteAllByTravelCourseId(id);// 여행지 초기화
        for (AddCourseDestinationRequest courseDestination : request.getCourseDestinations()) {

            CourseDestination courseDestinationEntity = CourseDestination.builder()
                    .date(courseDestination.getDate())
                    .visitOrder(courseDestination.getVisitOrder())
                    .build();
            courseDestinationEntity.setTravelCourse(travelCourse);

            Destination destination = destinationService.findById(courseDestination.getDestination().getId());

            courseDestinationEntity.setDestination(destination);
            courseDestinationRepository.save(courseDestinationEntity);
        }


        /*태그 설정*/
        tagService.deleteAllTagByCourse(id);// 태그 초기화
        List<Long> tagIds = request.getTags().stream()
                .map(TagResponseDto::getId)
                .collect(Collectors.toList());
        tagService.addTagsByCourse(id, tagIds);

        return travelCourseRepository.save(travelCourse);
    }

    @Override
    public void delete(Long id) {
        if (!travelCourseRepository.existsById(id)) {
            throw new TravelCourseNotFoundException("삭제할 코스가 존재하지 않습니다.", "Course ID: " + id);
        }
        travelCourseRepository.deleteById(id);
    }

    @Override
    public TravelCourse incrementViews(Long id) {
        TravelCourse travelCourse = travelCourseRepository.findById(id)
                .orElseThrow(() -> new TravelCourseNotFoundException("코스가 존재하지 않습니다.", "Course ID: " + id));
        travelCourse.incrementViews();
        return travelCourseRepository.save(travelCourse);
    }
}