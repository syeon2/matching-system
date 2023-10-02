package project.matchingsystem.member.web.request;

import org.springframework.data.geo.Point;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GeoLocation {

	private Long memberId;
	private double latitude;
	private double longitude;

	@Builder
	private GeoLocation(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Point toPoint() {
		return new Point(this.getLongitude(), this.getLatitude());
	}

	public static GeoLocation toGeoLocation(Point point) {
		return GeoLocation.builder()
			.latitude(point.getY())
			.longitude(point.getX())
			.build();
	}
}
