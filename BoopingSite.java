import oop.ex3.searchengine.Hotel;
import oop.ex3.searchengine.HotelDataset;
import java.util.*;

/**
 * This class is providing the users of the site the ability to get a list of hotels based on different
 * parameters.
 */
public class BoopingSite {
	/*
	class attributes
	 */
	private final Hotel[] hotelsDataSet; // the hotels dataset

	/**
	 * This constructor receives as parameter a string, which is the name of the dataset.
	 * @param name name of the dataset file
	 */
	public BoopingSite(String name){
		hotelsDataSet = HotelDataset.getHotels(name);
	}

	/*
	This method returns a list of hotels in the given city.
	 */
	private LinkedList<Hotel> getHotelsInCity(String city){
		LinkedList<Hotel> hotelsInCity = new LinkedList<Hotel>();
		for (Hotel hotel:hotelsDataSet) {
			if (hotel.getCity().equals(city)) {
				hotelsInCity.add(hotel);
			}
		}
		return hotelsInCity;
	}

	/**
	 *  This method returns an array of hotels located in the given city, sorted from the highest
	 *  star-rating to the lowest. Hotels that have the same rating will be organized according to the
	 *  alphabet order of the hotel’s (property) name. In case there are no hotels in the given city, this
	 *  method returns an empty array
	 * @param city a city to search hotel in it
	 * @return array of hotels in that city
	 */
	public Hotel[] getHotelsInCityByRating(String city){
		LinkedList<Hotel> hotelsInCity = getHotelsInCity(city);
		if (hotelsInCity.isEmpty()){
			return new Hotel[0];
		}
		else {
			Collections.sort(hotelsInCity, new SortByRating());
			return hotelsInCity.toArray(new Hotel[hotelsInCity.size()]);
		}
	}

	/**
	 * This method returns an array of hotels, sorted according to their Euclidean distance from the given
	 * geographic location, in ascending order. Hotels that are at the same distance from the given
	 * location are organized according to the number of points-of-interest for which they are close to (in
	 * a decreasing order). In case of illegal input, this method returns an empty array
	 * @param latitude latitude coordinate to compare with
	 * @param longitude longitude coordinate to compare with
	 * @return array of hotels sorted by the closer to the input point
	 */
	public Hotel[] getHotelsByProximity(double latitude, double longitude){
		if (illegalInput(latitude, longitude)){
			return new Hotel[0];
		}
		else {
			List<Hotel> hotels = Arrays.asList(hotelsDataSet);
			Collections.sort(hotels, new SortByProximity(latitude, longitude));
			return hotels.toArray(new Hotel[hotels.size()]);
		}
	}

	/*
	This method checks if coordinates are valid
	 */
	private boolean illegalInput(double latitude, double longitude){
		return latitude > 90 || latitude < -90 || longitude > 180 || longitude < -180;
	}

	/**
	 * This method returns an array of hotels in the given city, sorted according to their Euclidean distance
	 * from the given geographic location, in ascending order. Hotels that are at the same distance from the
	 * given location are organized according to the number of points-of-interest for which they are close to
	 * (in a decreasing order). In case of illegal input, this method returns an empty array.
	 * @param city city of wanted hotels
	 * @param latitude latitude coordinate to compare with
	 * @param longitude longitude coordinate to compare with
	 * @return array of hotels in the wanted city sorted by closer to the input point
	 */
	public Hotel[] getHotelsInCityByProximity(String city, double latitude, double longitude){
		if (illegalInput(latitude, longitude)){
			return new Hotel[0];
		}
		else {
			LinkedList<Hotel> hotelsInCity = getHotelsInCity(city);
			Collections.sort(hotelsInCity, new SortByProximity(latitude, longitude));
			return hotelsInCity.toArray(new Hotel[hotelsInCity.size()]);
		}
	}
}

