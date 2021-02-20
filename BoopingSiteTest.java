import org.junit.*;
import static org.junit.Assert.*;
import oop.ex3.searchengine.Hotel;

/**
 * This class is a test class fot BoopingSite class
 */
public class BoopingSiteTest {
	/*
	constants
	 */
	private static final int SITES_AMOUNT = 3;
	private static final String FULL_NAME = "hotels_dataset.txt";
	private static final String FIRST_NAME = "hotels_tst1.txt";
	private static final String SECOND_NAME = "hotels_tst2.txt";
	private static final String CITY_WRONG_NAME = "Raz";
	private static final String CITY_NAME = "manali";
	private static final int CITY_COUNT = 70;
	private static final double LATITUDE_TO_PROXIMATE = 0;
	private static final double LONGITUDE_TO_PROXIMATE = 0;
	private static final double VALID_COORDINATE = 50;
	private static final double VALID_NEGATIVE_COORDINATE = -20;
	private static final double UNVALID_NEGATIVE_COORDINATE = -200;
	private static final double UNVALID_COORDINATE = 200;
	private static final String CITY_MSG = "city doesn't exist";
	private static final String COUNT_MSG = "city counts wrong";
	private static final String RATING_MSG = "sort by rating failed";
	private static final String NAME_SORT_MSG = "sort by name when rating is equal failed";
	private static final String WRONG_LATITUDE_MSG = "unvalid latitude";
	private static final String WRONG_LONGITUDE_MSG = "unvalid longitude";
	private static final String WRONG_COORDINATES_MSG = "both coordinates wrong";
	private static final String PROXIMITY_MSG = "sort by proximity failed";
	private static final String POI_MSG = "sort by poi when proximity is equal failed";

	/*
	class attributes
	 */
	private static BoopingSite[] sites; // all datasets

	/**
	 * create class attributes
	 */
	@BeforeClass
	public static void createTestObjects(){
		sites = new BoopingSite[SITES_AMOUNT];
		sites[0] = new BoopingSite(FULL_NAME);
		sites[1] = new BoopingSite(FIRST_NAME);
		sites[2] = new BoopingSite(SECOND_NAME);
	}

	/*
	 getHotelsInCityByRating tests
	 */

	/**
	 * This test checks unvalid city - should be empty array
	 */
	@Test
	public void testGetHotelsInCityByRatingUnvalidCity(){
		for (int i = 0; i < SITES_AMOUNT; i++){
			assertEquals(CITY_MSG, 0, sites[i].getHotelsInCityByRating(CITY_WRONG_NAME).length);
		}
	}

	/**
	 * This test checks normal city filtering
	 */
	@Test
	public void testGetHotelsInCityByRatingFilteredCity(){
		assertEquals(COUNT_MSG, CITY_COUNT, sites[1].getHotelsInCityByRating(CITY_NAME).length);
		assertEquals(COUNT_MSG, 0, sites[2].getHotelsInCityByRating(CITY_NAME).length);
	}

	/**
	 * This test checks normal behavior - array is sorting by rating
	 */
	@Test
	public void testGetHotelsInCityByRatingSorted1(){
		for (int j = 0; j < SITES_AMOUNT; j++){
			Hotel[] hotels = sites[j].getHotelsInCityByRating(CITY_NAME);
			boolean flag = true;
			for	(int i = 0; i < hotels.length - 1; i++){
				if (!flag) break;
				if (hotels[i].getStarRating() < hotels[i+1].getStarRating()){
					flag = false;
				}
			}
			assertTrue(RATING_MSG, flag);
		}
	}

	/**
	 * This test checks normal behavior - when rating is equal array is sorting by name
	 */
	@Test
	public void testGetHotelsInCityByRatingSorted2(){
		for (int j = 0; j < SITES_AMOUNT; j++){
			Hotel[] hotels = sites[j].getHotelsInCityByRating(CITY_NAME);
			boolean flag = true;
			for	(int i = 0; i < hotels.length - 1; i++){
				if (!flag) break;
				if (hotels[i].getStarRating() == hotels[i+1].getStarRating()){
					if (hotels[i].getPropertyName().compareTo(hotels[i+1].getPropertyName()) > 0){
						flag = false;
					}
				}
			}
			assertTrue(NAME_SORT_MSG, flag);
		}
	}

	/*
	 getHotelsByProximity tests
	 */

	/**
	 * This test checks unvalid coordinates - should be empty array
	 */
	@Test
	public void testGetHotelsByProximityCoordinatesValidation(){
		for (int i = 0; i < SITES_AMOUNT; i++){
			assertEquals(WRONG_LATITUDE_MSG, 0, sites[i].getHotelsByProximity(UNVALID_COORDINATE,
																			  VALID_COORDINATE).length);
			assertEquals(WRONG_LONGITUDE_MSG, 0, sites[i].getHotelsByProximity(VALID_NEGATIVE_COORDINATE,
																			   UNVALID_COORDINATE).length);
			assertEquals(WRONG_COORDINATES_MSG, 0, sites[i].getHotelsByProximity(UNVALID_NEGATIVE_COORDINATE,
																				 UNVALID_COORDINATE).length);
		}
	}

	/*
	this method calculates the distance between two points
	 */
	private double distance(double latitude, double longitude){
		double ans =
				Math.pow(latitude-LATITUDE_TO_PROXIMATE, 2) + Math.pow(longitude-LONGITUDE_TO_PROXIMATE, 2);
		if (ans == 0){
			return 0;
		}
		return Math.sqrt(ans);
	}

	/*
	this method checks if an array sorted by proximity
	 */
	private boolean proximityCheck(Hotel[] hotels){
		boolean flag = true;
		for	(int i = 0; i < hotels.length - 1; i++) {
			if (!flag) break;
			if (distance(hotels[i].getLatitude(), hotels[i].getLongitude()
						) >
				distance(hotels[i + 1].getLatitude(), hotels[i + 1].getLongitude()
						)) {
				flag = false;
			}
		}
		return flag;
	}

	/*
	this method checks if an array sorted by POI when proximity is equal
	 */
	private boolean proximityAndPoiCheck(Hotel[] hotels){
		boolean flag = true;
		for	(int i = 0; i < hotels.length - 1; i++){
			if (!flag) break;
			if (distance(hotels[i].getLatitude(), hotels[i].getLongitude()
						) ==
				distance(hotels[i+1].getLatitude(), hotels[i+1].getLongitude()
						)){
				if (hotels[i].getNumPOI() < hotels[i+1].getNumPOI()){
					flag = false;
				}
			}
		}
		return flag;
	}

	/**
	 * This test checks normal behavior - array is sorting by proximity to input point
	 */
	@Test
	public void testGetHotelsByProximitySorted1(){
		for (int i = 0; i < SITES_AMOUNT; i++){
			Hotel[] hotels = sites[i].getHotelsByProximity(LATITUDE_TO_PROXIMATE,LONGITUDE_TO_PROXIMATE);
			assertTrue(PROXIMITY_MSG, proximityCheck(hotels));
		}
	}

	/**
	 * This test checks normal behavior - when distance is equal array is sorting by points of interest
	 */
	@Test
	public void testGetHotelsByProximitySorted2(){
		for (int i = 0; i < SITES_AMOUNT; i++){
			Hotel[] hotels = sites[i].getHotelsByProximity(LATITUDE_TO_PROXIMATE,LONGITUDE_TO_PROXIMATE);
			assertTrue(POI_MSG, proximityAndPoiCheck(hotels));
		}
	}

	/*
	 getHotelsInCityByProximity tests
	 */

	/**
	 * This test checks unvalid coordination - should be empty array
	 */
	@Test
	public void testgetHotelsInCityByProximityCoordinatesValidation(){
		for (int i = 0; i < SITES_AMOUNT; i++){
			assertEquals(WRONG_LATITUDE_MSG, 0, sites[i].getHotelsInCityByProximity(CITY_NAME,
																					UNVALID_COORDINATE,
																					VALID_COORDINATE).length);
			assertEquals(WRONG_LONGITUDE_MSG, 0,
						 sites[i].getHotelsInCityByProximity(CITY_NAME, VALID_COORDINATE,
															 UNVALID_NEGATIVE_COORDINATE).length);
			assertEquals(WRONG_COORDINATES_MSG, 0,
						 sites[i].getHotelsInCityByProximity(CITY_NAME, UNVALID_COORDINATE,
															 UNVALID_NEGATIVE_COORDINATE).length);
		}

	}

	/**
	 * This test checks unvalid city - should be empty array
	 */
	@Test
	public void testgetHotelsInCityByProximityUnvalidCity(){
		for (int i = 0; i < SITES_AMOUNT; i++){
			assertEquals(CITY_MSG, 0, sites[i].getHotelsInCityByProximity(CITY_WRONG_NAME, LATITUDE_TO_PROXIMATE,
																		  LONGITUDE_TO_PROXIMATE).length);
		}
	}

	/**
	 * This test checks normal city filtering
	 */
	@Test
	public void testgetHotelsInCityByProximityFilteredCity(){
		assertEquals(COUNT_MSG, CITY_COUNT, sites[1].getHotelsInCityByProximity(CITY_NAME,
																				LATITUDE_TO_PROXIMATE,
																				LONGITUDE_TO_PROXIMATE).length);
		assertEquals(COUNT_MSG, 0, sites[2].getHotelsInCityByProximity(CITY_NAME, LATITUDE_TO_PROXIMATE,
																	   LONGITUDE_TO_PROXIMATE).length);
	}

	/**
	 * This test checks normal behavior - array is sorting by proximity to input point
	 */
	@Test
	public void testgetHotelsInCityByProximitySorted1(){
		for (int i = 0; i < SITES_AMOUNT; i++){
			Hotel[] hotels = sites[i].getHotelsInCityByProximity(CITY_NAME, LATITUDE_TO_PROXIMATE,
																 LONGITUDE_TO_PROXIMATE);
			assertTrue(PROXIMITY_MSG, proximityCheck(hotels));
		}
	}

	/**
	 * This test checks normal behavior - when distance is equal array is sorting by points of interest
	 */
	@Test
	public void testgetHotelsInCityByProximitySorted2(){
		for (int i = 0; i < SITES_AMOUNT; i++){
			Hotel[] hotels = sites[i].getHotelsInCityByProximity(CITY_NAME, LONGITUDE_TO_PROXIMATE,
																 LONGITUDE_TO_PROXIMATE);
			assertTrue(POI_MSG, proximityAndPoiCheck(hotels));
		}
	}
}

