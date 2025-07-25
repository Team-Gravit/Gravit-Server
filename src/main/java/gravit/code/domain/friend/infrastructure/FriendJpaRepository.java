package gravit.code.domain.friend.infrastructure;

import gravit.code.domain.friend.domain.Friend;
import gravit.code.domain.friend.dto.response.FollowerResponse;
import gravit.code.domain.friend.dto.response.FollowingResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendJpaRepository extends JpaRepository<Friend, Long> {
    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
    Optional<Friend> findByFolloweeIdAndFollowerId(Long followeeId, Long followerId);

    @Query("""
        select new gravit.code.domain.friend.dto.response.FollowerResponse(
            u.nickname,
            u.profileImgNumber,
            u.handle
        )
        from Friend f
        join User u on u.id = f.followerId
        where f.followeeId = :followeeId
""")
    List<FollowerResponse> findFollowersByFolloweeId(@Param("followeeId") Long followeeId);

    @Query("""
        select new gravit.code.domain.friend.dto.response.FollowingResponse(
            u.nickname,
            u.profileImgNumber,
            u.handle
        )
        from Friend f
        join User u on u.id = f.followeeId
        where f.followerId = :followerId
""")
    List<FollowingResponse> findByFollowingsByFollowerId(@Param("followerId") Long followerId);


}
