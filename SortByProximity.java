import oop.ex3.searchengine.Hotel;
import java.util.Comparator;

/**
 * This class is a comparator to define sorted hotels by their proximity to an input point
 */
class SortByProximity implements Comparator<Hotel> {
	// class attributes
	private final double sortLatitude;
	private final double sortLongitude;

	/**
	 * This constructor gets the point coordinates to compare with
	 * @param latitude latitude coordinate
	 * @param longitude longitude coordinate
	 */
	SortByProximity(double latitude, double longitude) {
		sortLatitude = latitude;
		sortLongitude = longitude;
	}

	/*
	This method calculates the distance between two points
	 */
	private double distanceFromPoint(double latitude, double longitude) {
		double ans = Math.pow(latitude - sortLatitude, 2) + Math.pow(longitude - sortLongitude, 2);
		if (ans == 0) {
			return 0;
		}
		return Math.sqrt(ans);
	}

	/**
	 * This method defines the sort logics according to the proximity of hotels to the input point.
	 * @param h1 first hotel object
	 * @param h2 second hotel object to compare with
	 * @return value that represents who is "bigger" - how to sort.
	 */
	@Override
	public int compare(Hotel h1, Hotel h2) {
		double h1Distance = distanceFromPoint(h1.getLatitude(), h1.getLongitude());
		double h2Distance = distanceFromPoint(h2.getLatitude(), h2.getLongitude());
		if (h1Distance < h2Distance) {
			return -1;
		} else if (h1Distance == h2Distance && h1.getNumPOI() > h2.getNumPOI()) {
			return -1;
		} else {
			return 1;
		}
	}
}
