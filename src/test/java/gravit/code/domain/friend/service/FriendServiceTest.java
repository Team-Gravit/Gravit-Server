package gravit.code.domain.friend.service;

import gravit.code.domain.friend.domain.Friend;
import gravit.code.domain.friend.domain.FriendRepository;
import gravit.code.domain.friend.dto.response.FollowerResponse;
import gravit.code.domain.friend.dto.response.FollowingResponse;
import gravit.code.domain.friend.dto.response.FriendResponse;
import gravit.code.domain.user.domain.User;
import gravit.code.domain.user.domain.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
class FriendServiceTest {

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Test
    void 상호_팔로우_가능한지_검증() {
        // given
        User user1 = User.create("user1@example.com", "kakao1231513141231", "User1", "@user1", 1, LocalDateTime.now());
        userRepository.save(user1);
        User user2 = User.create("user2@example.com", "google123677438112", "User2", "@user2", 2, LocalDateTime.now());
        userRepository.save(user2);

        // when
        FriendResponse response2 = friendService.following(user1.getId(), user2.getId()); // user2 -> user1
        FriendResponse response1 = friendService.following(user2.getId(), user1.getId()); // user1 -> user2


        // then
        assertEquals(response1.followerId(), user1.getId());
        assertEquals(response1.followeeId(), user2.getId());

        assertEquals(response2.followerId(), user2.getId());
        assertEquals(response2.followeeId(), user1.getId());

        // 추가 검증
        assertTrue(friendRepository.existsByFollowerIdAndFolloweeId(user1.getId(), user2.getId()));
        assertTrue(friendRepository.existsByFollowerIdAndFolloweeId(user2.getId(), user1.getId()));

    }

    @Test
    void unFollowing() {
    }

    @Test
    @DisplayName("나를 팔로우 한 사람들을 조회합니다.")
    void getFollowers() {
        // given
        User user1 = User.create("user1@example.com", "kakao1231513141231", "User1", "@user1", 1, LocalDateTime.now());
        userRepository.save(user1);
        User user2 = User.create("user2@example.com", "kakao1258853439443", "User2", "@user2", 1, LocalDateTime.now());
        userRepository.save(user2);
        User user3 = User.create("user3@example.com", "kakao6438123471324", "User3", "@user3", 1, LocalDateTime.now());
        userRepository.save(user3);

        Friend friend1 = Friend.create(user1.getId(), user2.getId());
        friendRepository.save(friend1);
        Friend friend2 = Friend.create(user1.getId(), user3.getId());
        friendRepository.save(friend2);
        Friend friend3 = Friend.create(user2.getId(), user1.getId());
        friendRepository.save(friend3);
        Friend friend4 = Friend.create(user3.getId(), user1.getId());
        friendRepository.save(friend4);

        // when
        List<FollowerResponse> followerResponses = friendService.getFollowers(user1.getId());

        // then
        assertEquals(2, followerResponses.size());
        for (FollowerResponse followerResponse : followerResponses) {
            assertNotNull(followerResponse.nickname());
            assertNotNull(followerResponse.handle());
            assertTrue(followerResponse.profileImgNumber() > 0);
        }
    }

    @Test
    @DisplayName("내가 팔로우 한 사람들을 조회합니다.")
    void getFollowings() {
        // given
        User user1 = User.create("user1@example.com", "kakao1231513141231", "User1", "@user1", 1, LocalDateTime.now());
        userRepository.save(user1);
        User user2 = User.create("user2@example.com", "kakao1258853439443", "User2", "@user2", 1, LocalDateTime.now());
        userRepository.save(user2);
        User user3 = User.create("user3@example.com", "kakao6438123471324", "User3", "@user3", 1, LocalDateTime.now());
        userRepository.save(user3);

        Friend friend1 = Friend.create(user1.getId(), user2.getId());
        friendRepository.save(friend1);
        Friend friend2 = Friend.create(user1.getId(), user3.getId());
        friendRepository.save(friend2);
        Friend friend3 = Friend.create(user2.getId(), user1.getId());
        friendRepository.save(friend3);
        Friend friend4 = Friend.create(user3.getId(), user1.getId());
        friendRepository.save(friend4);

        // when
        List<FollowingResponse> followingResponses = friendService.getFollowings(user1.getId());

        // then
        assertEquals(2, followingResponses.size());
        for (FollowingResponse followingResponse : followingResponses) {
            assertNotNull(followingResponse.nickname());
            assertNotNull(followingResponse.handle());
            assertTrue(followingResponse.profileImgNumber() > 0);
        }
    }
}