package competnion.domain.user.dto.response;

import competnion.domain.user.dto.request.AddressRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class UpdateAddressResponse {
    private Double latitude;
    private Double longitude;
    private String address;

    public static UpdateAddressResponse of(AddressRequest request) {
        return new UpdateAddressResponse(
                request.getLatitude(),
                request.getLongitude(),
                request.getAddress()
        );
    }
}
