package gravit.code.domain.chapterProgress.domain;

import gravit.code.domain.chapterProgress.domain.ChapterProgress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChapterProgressTest {

    @DisplayName("유효한 데이터로 유저-챕터 중간 테이블 앤티티를 생성할 수 있다")
    @Test
    void createUserChapterProgressWithAvailableData() {

        // given
        Long totalUnits = 20L;
        Long userId = 1L;
        Long chapterId = 1L;

        // when
        ChapterProgress chapterProgress = ChapterProgress.create(totalUnits, userId, chapterId);

        // then
        assertThat(chapterProgress.getTotalUnits()).isEqualTo(totalUnits);
        assertThat(chapterProgress.getCompletedUnits()).isEqualTo(0L);
        assertThat(chapterProgress.getUserId()).isEqualTo(userId);
        assertThat(chapterProgress.getChapterId()).isEqualTo(chapterId);

    }
}