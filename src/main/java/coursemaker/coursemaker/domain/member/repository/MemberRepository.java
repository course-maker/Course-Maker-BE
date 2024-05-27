package coursemaker.coursemaker.domain.member.repository;

import coursemaker.coursemaker.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username);
}
