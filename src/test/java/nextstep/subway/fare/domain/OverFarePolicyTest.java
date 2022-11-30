package nextstep.subway.fare.domain;

import nextstep.subway.exception.NotFoundOverFarePolicyException;
import nextstep.subway.message.ExceptionMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.fare.domain.OverFarePolicy.*;

class OverFarePolicyTest {

    @DisplayName("추가요금 정책이 없는 거리가 입력되면 예외가 발생한다.")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @ValueSource(ints = {-1, -10})
    void exception(int input) {
        Assertions.assertThatThrownBy(() -> OverFarePolicy.findPolicyByDistance(input))
                .isInstanceOf(NotFoundOverFarePolicyException.class)
                .hasMessageStartingWith(ExceptionMessage.OVER_FARE_POLICY_NOT_EXIST);
    }

    @DisplayName("10km 이하일 때 추가 요금 계산")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @CsvSource(value = {"0:0", "5:0", "10:0"}, delimiter = ':')
    void basicFare(int input, int expected) {
        int result = TO_TEN.calculateOverFare(input);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @DisplayName("10km 초과 50km 미만 거리에 대한 추가요금 계산")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @CsvSource(value = {"11:100", "25:300", "50:800"}, delimiter = ':')
    void tenToFiftyOverFare(int input, int expected) {
        int result = TO_FIFTY.calculateOverFare(input);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @DisplayName("50km 초과 거리에 대한 추가요금 계산")
    @ParameterizedTest(name = "{index} | {displayName} | {argumentsWithNames}")
    @CsvSource(value = {"51:100", "58:100", "100:700"}, delimiter = ':')
    void overFiftyOverFare(int input, int expected) {
        int result = OVER_FIFTY.calculateOverFare(input);

        Assertions.assertThat(result).isEqualTo(expected);
    }
}