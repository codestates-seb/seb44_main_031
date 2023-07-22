package competnion.global.util;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class ZonedDateTimeUtil {
    public ZonedDateTime getNow() {
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        return ZonedDateTime.now(zoneId);
    }
}
