package competnion.domain.user.dto.request;

import lombok.Getter;

@Getter
public class AddressRequest {
    private Double latitude;
    private Double longitude;
    private String address;
}
