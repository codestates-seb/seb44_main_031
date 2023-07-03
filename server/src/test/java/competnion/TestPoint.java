package competnion;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class TestPoint {

    @Test
    void 좌표_테스트() throws ParseException {
        System.out.println("안녕하세요");

        Double latitude = 31.6623434;
        Double longitude = 12.553421;

        Point point = (Point) new WKTReader()
                .read(String.format("POINT(%s %s)", latitude, longitude));

        System.out.println(point);

    }
}
