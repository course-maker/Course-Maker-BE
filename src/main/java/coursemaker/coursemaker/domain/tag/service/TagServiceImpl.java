package coursemaker.coursemaker.domain.tag.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.sun.jdi.request.DuplicateRequestException;
import coursemaker.coursemaker.domain.course.entity.TravelCourse;
import coursemaker.coursemaker.domain.course.service.CourseService;
import coursemaker.coursemaker.domain.destination.entity.Destination;
import coursemaker.coursemaker.domain.destination.entity.QDestination;
import coursemaker.coursemaker.domain.destination.service.DestinationService;
import coursemaker.coursemaker.domain.tag.entity.CourseTag;
import coursemaker.coursemaker.domain.tag.entity.DestinationTag;
import coursemaker.coursemaker.domain.tag.entity.QDestinationTag;
import coursemaker.coursemaker.domain.tag.entity.Tag;
import coursemaker.coursemaker.domain.tag.exception.IllegalTagArgumentException;
import coursemaker.coursemaker.domain.tag.exception.TagDuplicatedException;
import coursemaker.coursemaker.domain.tag.exception.TagNotFoundException;
import coursemaker.coursemaker.domain.tag.repository.CourseTagRepository;
import coursemaker.coursemaker.domain.tag.repository.DestinationTagRepository;
import coursemaker.coursemaker.domain.tag.repository.TagRepository;
import coursemaker.coursemaker.exception.ErrorCode;
import coursemaker.coursemaker.util.CourseMakerPagination;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import com.querydsl.jpa.impl.JPAQueryFactory;


import static coursemaker.coursemaker.domain.course.entity.QTravelCourse.travelCourse;
import static coursemaker.coursemaker.domain.destination.entity.QDestination.destination;
import static coursemaker.coursemaker.domain.tag.entity.QCourseTag.courseTag;
import static coursemaker.coursemaker.domain.tag.entity.QDestinationTag.destinationTag;
import static coursemaker.coursemaker.domain.tag.entity.QTag.tag;

// TODO: 삭제 연산 soft delete로 전환
@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{

    private final CourseTagRepository courseTagRepository;
    private final TagRepository tagRepository;
    private final DestinationTagRepository destinationTagRepository;

    private final CourseService courseService;
    private final DestinationService destinationService;

    private final JPAQueryFactory queryFactory;

    /*****태그 기본 CRUD*****/
    @Override
    public Tag createTag(Tag tag){

        /*태그의 이름은 고유해야 한다.*/
        if(tagRepository.findByname(tag.getName()).isPresent()) {
            throw new TagDuplicatedException("이미 존재하는 태그입니다.", "tag name: " + tag.getName() );
        }

        return tagRepository.save(tag);
    }

    @Override
    public Tag findById(Long id){
        return tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "tag id: " + id ));
    }

    @Override
    public Tag findByTagName(String name){
        return tagRepository.findByname(name)
                .orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "tag name: " + name ));
    }

    @Override
    public List<Tag> findAllTags(){
        return tagRepository.findAll();
    }

    @Override
    public Tag updateTag(Tag tag){

        /*변경할 타겟 태그를 못찾음.*/
        tagRepository.findById(tag.getId()).orElseThrow(() ->
                new TagNotFoundException("변경할 태그를 찾지 못했습니다.",
                        "tag id: " + tag.getId() ));

        /*태그의 이름 중복 확인*/
        Optional<Tag> tagByName = tagRepository.findByname(tag.getName());
        if(tagByName.isPresent()
                && !tagByName.get().getId().equals(tag.getId()))// 태그의 설명만 바꾸는 경우가 아닐때
        {
            throw new TagDuplicatedException("이미 존재하는 태그 이름입니다.", "tag name: " + tag.getName() );
        }


        return tagRepository.save(tag);
    }


    @Override
    public void deleteById(Long id){
        /*삭제할 태그가 존재하는지 확인*/
        tagRepository.findById(id).orElseThrow(() ->
                new TagNotFoundException("삭제할 태그가 존재하지 않습니다.", "tag id: " + id ));

        /*태그와 연결된 여행지, 코스 연관관계 삭제*/
        courseTagRepository.deleteAllByTagId(id);
        destinationTagRepository.deleteAllByTagId(id);

        /*태그 삭제*/
        tagRepository.deleteById(id);
    }

    /******태그-코스 ******/

    @Override
    public void addTagsByCourse(Long courseId, List<Long> tagIds){

        if(tagIds == null || tagIds.isEmpty()){
            throw new IllegalTagArgumentException("코스에 추가할 태그가 없습니다.", "course id: " + courseId );
        }

        // TODO: 쿼리 최적화
//        /*태그 유효성 검사*/
//        List <Tag> tags = queryFactory
//                .selectFrom(tag)
//                .where(tag.id.notIn(tagIds))
//                .fetch();
//        for(Tag tag : tags){
//            throw new TagNotFoundException("추가할 태그가 존재하지 않습니다.", "tag id: " + tag.getId() );
//        }
//
//
//        /*코스에 태그가 이미 포함되있는지 확인*/
//        tags = queryFactory
//                .select(tag)
//                .from(courseTag)
//                .where(courseTag.course.id.eq(courseId), courseTag.tag.id.in(tagIds))
//                .fetch();
//        for(Tag tag : tags){
//            throw new TagDuplicatedException("코스에 이미 추가된 태그가 있습니다.", "tag id: " + tag.getId() );
//        }
//
//        /*추가할 태그 찾아서 추가*/
//        tags = queryFactory
//                .selectFrom(tag)
//                .where(tag.id.in(tagIds))
//                .fetch();
//        TravelCourse course = courseService.findById(courseId);
//        for(Tag insertTag : tags){
//            queryFactory
//                    .insert(courseTag)
//                    .set(courseTag.course, course)
//                    .set(courseTag.tag, insertTag)
//                    .execute();
//        }



        // 중복된 태그를 제외하고 추가함
        for (Long tagId : tagIds) {
            CourseTag courseTag = new CourseTag();
            Tag tag = tagRepository.findById(tagId).orElseThrow(() ->
                    new TagNotFoundException("해당 태그가 존재하지 않습니다.",
                            "tag id: " + tagId ));

            if(courseTagRepository.findByCourseIdAndTagId(courseId, tagId).isEmpty()){
                courseTag.setTag(tag);
                courseTag.setCourse(courseService.findById(courseId));
                courseTagRepository.save(courseTag);
            }
        }
    }


    @Override
    public List<Tag> findAllByCourseId(Long courseId){
        List<CourseTag> courseTags = courseTagRepository.findAllByCourseId(courseId);

        // CourseTag에서 태그 추출
        List<Tag> tags = courseTags
                .stream()
                .map(CourseTag::getTag)
                .collect(Collectors.toList());

        return tags;
    }

    @Override
    public CourseMakerPagination<TravelCourse> findAllCourseByTagIds(List<Long> tagIds, Pageable pageable, OrderBy orderBy){

        /*검색할때 아무 태그도 선택 안했을 경우 = 모든 태그를 기준으로 검색*/
        if(tagIds == null || tagIds.isEmpty()){
            tagIds = tagRepository.findAll()
                    .stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
            System.out.println("------------------"+tagIds);
        }

        OrderSpecifier<?> orderBySpecifier = null;

        // TODO: 인기순 정렬 로직 설정, 평균별점 로직 설정(고도화)
        switch(orderBy) {
            case VIEWS:
                orderBySpecifier = new OrderSpecifier<>(Order.DESC, travelCourse.views);
                break;
            case NEWEST:
                orderBySpecifier = new OrderSpecifier<>(Order.DESC, travelCourse.createdAt);
                break;
            case POPULAR:
                orderBySpecifier = new OrderSpecifier<>(Order.DESC, travelCourse.views);
                break;
            case RATING:
                orderBySpecifier = new OrderSpecifier<>(Order.DESC, travelCourse.views);
                break;
        }

        List<TravelCourse> courses = queryFactory
                .select(courseTag, courseTag.course.count())
                .from(courseTag)// 코스태그에서 선택(코스에는 FK가 없음)
                .leftJoin(courseTag.course, travelCourse)// 코스-코스태그 조인
                .where(courseTag.tag.id.in(tagIds).and(travelCourse.deletedAt.isNull()))// 다중태그 및 삭제되지 않은 코스 필터링
                .groupBy(courseTag.course)// 코스로 묶어서
//                .having(courseTag.course.count().gt(tagIds.size()-1))// 중복된 부분만 추출함
                .orderBy(orderBySpecifier)// 정렬 조건 설정
                .offset(pageable.getOffset())// 페이지네이션
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(n -> n.get(courseTag).getCourse())
                .collect(Collectors.toList());

        // TODO: 쿼리 최적화

        long total = queryFactory
                .select(courseTag, courseTag.course.count())
                .from(courseTag)// 코스태그에서 선택(코스에는 FK가 없음)
                .leftJoin(courseTag.course, travelCourse)// 코스-코스태그 조인
                .where(courseTag.tag.id.in(tagIds).and(travelCourse.deletedAt.isNull()))// 다중태그 및 삭제되지 않은 코스 필터링
                .groupBy(courseTag.course)// 코스로 묶어서
//                .having(courseTag.course.count().gt(tagIds.size()-1))// 중복된 부분만 추출함
                .orderBy(orderBySpecifier)// 정렬 조건 설정
                .fetch()
                .stream()
                .map(n -> n.get(courseTag).getCourse())
                .toList()
                .size();

//        System.out.println("-----------------total = " + total);
//        for(TravelCourse course : courses){
//            System.out.println("course.getId() = " + course.getId());
//        }

        Page<TravelCourse> coursePage = new PageImpl<>(courses, pageable, total);

//        System.out.println("coursePage.getTotalPages() = " + coursePage.getTotalPages());
//        System.out.println("coursePage.getNumber() = " + coursePage.getNumber());
//        System.out.println("coursePage.getSize() = " + coursePage.getSize());

        CourseMakerPagination<TravelCourse> courseMakerPagination = new CourseMakerPagination<>(pageable, coursePage);

        return courseMakerPagination;
    }



    @Override
    public void deleteTagByCourse(Long courseId, List<Tag> tags){

        if(tags==null || tags.isEmpty()){
            throw new IllegalTagArgumentException("코스에서 삭제할 태그가 없습니다.", "course id: " + courseId );
        }

        // 코스에 태그들 삭제
        queryFactory
                .delete(courseTag)
                .where(courseTag.course.id.eq(courseId), courseTag.tag.in(tags))
                .execute();
    }

    @Override
    public void deleteAllTagByCourse(Long courseId){
        courseTagRepository.deleteAllByCourseId(courseId);
    }



    /******태그-여행지 ******/

    @Override
    public void addTagsByDestination(Long destinationId, List<Long> tagIds){

        if(tagIds == null || tagIds.isEmpty()){
            throw new IllegalTagArgumentException("여행지에 추가할 태그가 없습니다.", "destination id: " + destinationId );
        }

        // TODO: 쿼리 최적화

//        /*태그 유효성 검사*/
//        List <Tag> tags = queryFactory
//                .selectFrom(tag)
//                .where(tag.id.notIn(tagIds))
//                .fetch();
//        for(Tag tag : tags){
//            throw new TagNotFoundException("추가할 태그가 존재하지 않습니다.", "tag id: " + tag.getId() );
//        }
//
//        /*여행지에 태그가 이미 포함되있는지 확인*/
//        tags = queryFactory
//                .select(tag)
//                .from(destinationTag)
//                .where(destinationTag.destination.id.eq(destinationId), destinationTag.tag.id.in(tagIds))
//                .fetch();
//        for(Tag tag : tags){
//            throw new TagDuplicatedException("여행지에 이미 추가된 태그가 있습니다.", "tag id: " + tag.getId() );
//        }
//
//        /*추가할 태그 찾아서 추가*/
//        tags = queryFactory
//                .selectFrom(tag)
//                .where(tag.id.in(tagIds))
//                .fetch();
//        Destination course = destinationService.findById(destinationId);
//        for(Tag insertTag : tags){
//            queryFactory
//                    .insert(destinationTag)
//                    .set(destinationTag.destination, destination)
//                    .set(destinationTag.tag, insertTag)
//                    .execute();
//        }


        // 중복된 태그를 제외하고 추가함
        for (Long tagId : tagIds) {
            DestinationTag destinationTag = new DestinationTag();
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new TagNotFoundException("해당 태그가 존재하지 않습니다.", "tag id: " + tagId ));

            if(destinationTagRepository.findByDestinationIdAndTagId(destinationId, tagId).isEmpty()){// JOIN 걸어서 하기
                destinationTag.setTag(tag);
                destinationTag.setDestination(destinationService.findById(destinationId)); // JOIN 걸어서 하기
                destinationTagRepository.save(destinationTag);
            }
        }
    }

    @Override
    public List<Tag> findAllByDestinationId(Long destinationId){
        List<DestinationTag> destinationTags = destinationTagRepository.findAllByDestinationId(destinationId);
        List<Tag> tags = new ArrayList<>();

        // CourseTag에서 태그 추출
        tags = destinationTags
                .stream()
                .map(DestinationTag::getTag)
                .collect(Collectors.toList());

        return tags;
    }


    @Override
    public CourseMakerPagination<Destination> findAllDestinationByTagIds(List<Long> tagIds, Pageable pageable, OrderBy orderBy) {

        /*검색할때 아무 태그도 선택 안했을 경우 = 모든 태그를 기준으로 검색*/
        if(tagIds == null || tagIds.isEmpty()){
            tagIds = tagRepository.findAll()
                    .stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
        }

        OrderSpecifier<?> orderBySpecifier = null;

        // TODO: 인기순 정렬 로직 설정, 평균별점 로직 설정(고도화)
        switch(orderBy) {
            case VIEWS:
                orderBySpecifier = new OrderSpecifier<>(Order.DESC, destination.views);
                break;
            case NEWEST:
                orderBySpecifier = new OrderSpecifier<>(Order.DESC, destination.createdAt);
                break;
            case POPULAR:
                orderBySpecifier = new OrderSpecifier<>(Order.DESC, destination.views);
                break;
            case RATING:
                orderBySpecifier = new OrderSpecifier<>(Order.DESC, destination.views);
                break;
        }


        List<Destination> destinations = queryFactory
                .select(destinationTag, destinationTag.destination.count())
                .from(destinationTag)// 여행지 태그에서 선택(여행지에는 FK가 없음)
                .leftJoin(destinationTag.destination, destination)// 여행지-여행지태그 조인
                .where(destinationTag.tag.id.in(tagIds))// 다중태그 조건 검색
                .groupBy(destinationTag.destination)// 여행지로 묶어서
//                .having(destinationTag.destination.count().gt(tagIds.size()-1))// 중복된 부분만 추출
                .orderBy(orderBySpecifier)// 정렬 조건 설정
                .offset(pageable.getOffset())// 페이지네이션
                .limit(pageable.getPageSize())
                .fetch()// 쿼리 실행
                .stream()// 리스트 -> 스트림으로 처리
                .map(n -> n.get(destinationTag).getDestination())// 여행지태그 -> 여행지 변환
                .collect(Collectors.toList());

        // TODO: 쿼리 최적화
        long total = queryFactory
                .select(destinationTag, destinationTag.destination.count())
                .from(destinationTag)// 여행지 태그에서 선택(코스에는 FK가 없음)
                .leftJoin(destinationTag.destination, destination)// 여행지-여행지태그 조인
                .where(destinationTag.tag.id.in(tagIds))// 다중태그
                .groupBy(destinationTag.destination)// 여행지로 묶어서
//                .having(destinationTag.destination.count().gt(tagIds.size()-1))// 중복된 부분만 추출함
                .orderBy(orderBySpecifier)// 정렬 조건 설정
                .fetch()
                .stream()
                .map(n -> n.get(destinationTag).getDestination())
                .toList()
                .size();

        Page<Destination> destinationPage = new PageImpl<>(destinations, pageable, total);

        CourseMakerPagination<Destination> courseMakerPagination = new CourseMakerPagination<>(pageable, destinationPage);

        return courseMakerPagination;
    }


    @Override
    public void deleteTagByDestination(Long destinationId, List<Tag> tags){

        if(tags==null || tags.isEmpty()){
            throw new IllegalTagArgumentException("삭제할 태그가 없습니다.", "destination id: " + destinationId );
        }

        queryFactory
                .delete(destinationTag)
                .where(destinationTag.destination.id.eq(destinationId), destinationTag.tag.in(tags))
                .execute();

    }

    @Override
    public void deleteAllTagByDestination(Long destinationId){
        destinationTagRepository.deleteAllByDestinationId(destinationId);
    }

}
