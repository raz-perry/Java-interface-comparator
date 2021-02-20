import oop.ex3.searchengine.Hotel;
import java.util.Comparator;

/**
 * This class is a comparator to define sorted hotels by their rating stars
 */
class SortByRating implements Comparator<Hotel> {
	/**
	 * This method defines the sort logics according to the star ratings of hotels.
	 * @param h1 first hotel object
	 * @param h2 second hotel object to compare with
	 * @return value that represents who is "bigger" - how to sort.
	 */
	@Override
	public int compare(Hotel h1, Hotel h2) {
		if (h1.getStarRating() < h2.getStarRating()) {
			return 1;
		} else if (h1.getStarRating() == h2.getStarRating()) {
			return h1.getPropertyName().compareTo(h2.getPropertyName());
		} else {
			return -1;
		}
	}
}
