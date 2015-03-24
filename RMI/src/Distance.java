
public class Distance {
	public static double distanceInKilometers(double lat1, double lon1, double lat2, double lon2) {

		double lat1r = Math.toRadians(lat1);
		double lon1r = Math.toRadians(lon1);
		double lat2r = Math.toRadians(lat2);
		double lon2r = Math.toRadians(lon2);

		double sum1 = Math.sin(lat1r) * Math.sin(lat2r);

		double deltaLon = Math.abs(lon1r - lon2r);
		double sum2 = Math.cos(lat1r) * Math.cos(lat2r) * Math.cos(deltaLon);

		double centralAngle = Math.acos(sum1 + sum2);

		// Radius of Earth: 6378.1 kilometers
		double distance = centralAngle * 6378.1;

		return distance;
	}
}
