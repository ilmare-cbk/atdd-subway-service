package nextstep.subway.path.domain;

import nextstep.subway.exception.PathNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.message.ExceptionMessage;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PathFinderTest {

    private Line 신분당선;
    private Line 분당선;
    private Line 삼호선;
    private Line 일호선;
    private Station 정자역;
    private Station 양재역;
    private Station 수서역;
    private Station 서현역;
    private Station 소요산역;
    private Station 병점역;

    /**
     * 양재역 ------*3호선(5)*------ 수서역
     * ㅣ                          ㅣ
     * ㅣ                          ㅣ
     * *신분당선(10)*             *분당선(5)*
     * ㅣ                          ㅣ
     * ㅣ                          ㅣ
     * 정쟈역 ------*분당선(5)*------ 서현역
     * <p>
     * 소요산역 ------*일호선(20)*------ 병점역
     */
    @BeforeEach
    void setUp() {
        정자역 = new Station("정자역");
        양재역 = new Station("양재역");
        수서역 = new Station("수서역");
        서현역 = new Station("서현역");
        소요산역 = new Station("소요산역");
        병점역 = new Station("병점역");


        신분당선 = new Line("신분당선", "red", 양재역, 정자역, 10);
        분당선 = new Line("분당선", "yellow", 수서역, 정자역, 10);
        삼호선 = new Line("삼호선", "orange", 양재역, 수서역, 5);
        일호선 = new Line("일호선", "blue", 소요산역, 병점역, 20);

        분당선.addSection(new Section(분당선, 서현역, 정자역, 5));
    }

    @DisplayName("출발역과 도착역 사이의 최단 경로를 조회한다.")
    @Test
    void findShortestPath() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(신분당선, 분당선, 삼호선, 일호선));

        Path path = pathFinder.findShortestPath(양재역, 서현역);

        assertAll(
                () -> assertThat(path.getStations()).containsExactly(양재역, 수서역, 서현역),
                () -> assertThat(path.getDistance()).isEqualTo(10)
        );
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외가 발생한다.")
    @Test
    void findShortestPathException1() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(신분당선, 분당선, 삼호선, 일호선));

        Assertions.assertThatThrownBy(() -> pathFinder.findShortestPath(양재역, 양재역))
                .isInstanceOf(PathNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.SOURCE_AND_TARGET_EQUAL);
    }
}
