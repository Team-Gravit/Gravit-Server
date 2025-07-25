package gravit.code.domain.friend.service;


import gravit.code.domain.friend.dto.response.FollowerResponse;
import gravit.code.domain.friend.dto.response.FollowingResponse;
import gravit.code.domain.friend.dto.response.FriendResponse;
import gravit.code.domain.friend.domain.Friend;
import gravit.code.domain.friend.domain.FriendRepository;
import gravit.code.domain.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Transactional
    public FriendResponse following(Long followeeId, Long followerId) {
        log.info("팔로우 요청 순서 : follower : {}, followee : {}", followerId, followeeId);

        // 자기 자신에게 팔로잉 불가능
        if(followeeId.equals(followerId)){
            throw new RuntimeException("자기 자신에게 팔로잉은 불가능합니다.");
        }

        // 팔로잉 대상 유저가 존재하는지 확인
        userRepository.findById(followeeId)
                .orElseThrow(()-> new RuntimeException("팔로잉 대상 유저가 존재하지 않습니다."));

        // 이미 팔로우 중인지 중복 체크
        if(friendRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)){
            throw new RuntimeException("이미 팔로우 중입니다.");
        }

        Friend friend = Friend.create(followerId, followeeId);

        friendRepository.save(friend);

        return FriendResponse.from(friend);
    }

    @Transactional
    public void unFollowing(Long followeeId, Long followerId) {
        Optional<Friend> friend = friendRepository.findByFolloweeIdAndFollowerId(followeeId, followerId);

        // 만약 팔로우 한 내역이 존재하지 않는다면
        if(friend.isEmpty()){
            throw new RuntimeException();
        }

        friendRepository.delete(friend.get());
    }

    @Transactional(readOnly = true)
    public List<FollowerResponse> getFollowers(Long followeeId) {
        return friendRepository.findByFollowersByFolloweeId(followeeId);
    }

    @Transactional(readOnly = true)
    public List<FollowingResponse> getFollowings(Long followerId) {
        return friendRepository.findByFollowingsByFollowerId(followerId);
    }
}
