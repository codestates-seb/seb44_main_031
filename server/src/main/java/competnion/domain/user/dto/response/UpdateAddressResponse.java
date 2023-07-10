package competnion.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class UpdateAddressResponse {
    private Double latitude;
    private Double longitude;
    private String address;

    public static UpdateAddressResponse of(Double latitude, Double longitude, String address) {
        return new UpdateAddressResponse(
                latitude, longitude, address
        );
    }
}
