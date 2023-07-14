package competnion.global.util;

import competnion.global.exception.BusinessLogicException;
import competnion.global.exception.ExceptionCode;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Component;

import static competnion.global.exception.ExceptionCode.*;

@Component
public class CoordinateUtil {
    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public Point coordinateToPoint(
            final Double latitude,
            final Double longitude
    ) {
        if (latitude == null || longitude == null) {
            throw new BusinessLogicException(INVALID_COORDINATES);
        } else {
            Coordinate coordinate = new Coordinate(latitude, latitude);
            return geometryFactory.createPoint(coordinate);
        }
    }
}
