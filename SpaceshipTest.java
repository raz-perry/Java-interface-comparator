import oop.ex3.spaceship.Item;
import oop.ex3.spaceship.ItemFactory;
import org . junit .*;
import static org . junit . Assert .*;
import java.util.Random;

/**
 * This class is a test class fot Spaceship class
 */
public class SpaceshipTest {

	// constants
	private static final int SUCCESS = 0;
	private static final int WRONG_ID = -1;
	private static final int WRONG_CAPACITY = -2;
	private static final int FULL = -3;
	private static final int DEFAULT_LTS = 1000;
	private static final int DEFAULT_CAPACITY = 10;
	private static final int NEGATIVE_AMOUNT = -2;
	private static final String SPACESHIP_NAME = "Raz";
	private static final int DEFAULT_LOCKERS = 5;
	private static final String LTS_MSG = "long term storage changed";
	private static final String WRONG_ID_MSG = "crew id doesn't exist";
	private static final String VALID_ID_MSG = "crew id exist";
	private static final String NEGATIVE_CAPACITY_MSG = "capacity is negative";
	private static final String VALID_CAPACITY_MSG = "capacity is fine";
	private static final String FULL_MSG = "there isn't anymore place for a new locker";
	private static final String UNFULL_MSG = "there is a place for a new locker";
	private static final String IDS_MSG  = "wrong id's";
	private static final String NULL_MSG  = "should be null value";
	private static final String COUNT_NULL_MSG  = "wrong amount of null values";

	// class attributes
	private static Item[][] cons;
	private Spaceship spaceship;
	private Random rnd = new Random();
	int[] ids;

	/**
	 * create class attributes
	 */
	@BeforeClass
	public static void createSpaceship() {
		ItemFactory factory = new ItemFactory();
		cons = factory.getConstraintPairs();
	}

	/**
	 * this method default class attributes that change at most tests
	 */
	@Before
	public void defaultAttributes(){
		ids = new int[]{1, 3, 5, 7, 9};
		spaceship = new Spaceship(SPACESHIP_NAME, ids, DEFAULT_LOCKERS, cons);
	}

	/*
	getLongTermStorage tests
	 */

	/**
	 * This test checks that long term storage is same as default
	 */
	@Test
	public void testGetLongTermStorage() {
		assertEquals(LTS_MSG, DEFAULT_LTS, spaceship.getLongTermStorage().getCapacity());
		assertEquals(LTS_MSG, DEFAULT_LTS, spaceship.getLongTermStorage().getAvailableCapacity());
	}

	/*
	createLocker tests
	 */

	/**
	 * This test checks the id validation
	 */
	@Test
	public void testCreateLockerIdExistence() {
		assertEquals(WRONG_ID_MSG, WRONG_ID, spaceship.createLocker(2,DEFAULT_CAPACITY));
		assertEquals(VALID_ID_MSG, SUCCESS, spaceship.createLocker(3,DEFAULT_CAPACITY));
	}

	/**
	 * This test checks capacity values validation (zero is valid)
	 */
	@Test
	public void testCreateLockerCapacityValues() {
		assertEquals(NEGATIVE_CAPACITY_MSG, WRONG_CAPACITY, spaceship.createLocker(1,NEGATIVE_AMOUNT));
		assertEquals(VALID_CAPACITY_MSG, SUCCESS, spaceship.createLocker(1,0));
		assertEquals(VALID_CAPACITY_MSG, SUCCESS, spaceship.createLocker(3,DEFAULT_CAPACITY));
	}

	/**
	 * This test checks the normal behavior (should be 0) and full lockers validation including adding many
	 * 	lockers for one crew member.
	 */
	@Test
	public void testCreateLockerManyLockers() {
		assertEquals(UNFULL_MSG, SUCCESS, spaceship.createLocker(1,DEFAULT_CAPACITY));
		assertEquals(UNFULL_MSG, SUCCESS, spaceship.createLocker(1,DEFAULT_CAPACITY*2));
		assertEquals(UNFULL_MSG, SUCCESS, spaceship.createLocker(3,DEFAULT_CAPACITY*3));
		assertEquals(UNFULL_MSG, SUCCESS, spaceship.createLocker(3,DEFAULT_CAPACITY*4));
		assertEquals(UNFULL_MSG, SUCCESS, spaceship.createLocker(5,DEFAULT_CAPACITY*5));
		assertEquals(FULL_MSG, FULL, spaceship.createLocker(7,DEFAULT_CAPACITY*6));
	}

	/*
	getCrewIDs tests
	 */

	/**
	 * This test compare between the values from the array (that gets from the method) and the input id's -
	 * true even if the order is different.
	 */
	@Test
	public void testGetCrewIDs() {
		ids = new int[]{rnd.nextInt(1000), rnd.nextInt(1000), rnd.nextInt(1000)};
		spaceship = new Spaceship(SPACESHIP_NAME, ids, rnd.nextInt(10), cons);
		boolean flag = false;
		if (ids.length == spaceship.getCrewIDs().length){
			flag = true;
			boolean subFlag;
			for (int id:ids) {
				if (!flag){
					break;
				}
				subFlag = false;
				for (int crewID: spaceship.getCrewIDs()) {
					if (subFlag){
						break;
					}
					if (id == crewID){
						subFlag = true;
					}
				}
				if (!subFlag){
					flag = false;
				}
			}
		}
		assertTrue(IDS_MSG,flag);
	}

	/*
	getLockers tests
	 */

	/**
	 * This test checks that from the first null in the array all values are null
	 */
	@Test
	public void testGetLockersNullAtEnd() {
		spaceship.createLocker(1,DEFAULT_CAPACITY+5);
		spaceship.createLocker(3,DEFAULT_CAPACITY+2);
		Locker[] lockers = spaceship.getLockers();
		int i = 0;
		while (i < lockers.length && lockers[i] != null){
			i++;
		}
		for (int j = i; j < lockers.length; j++){
			assertNull(NULL_MSG,lockers[j]);
		}
	}

	/**
	 * This test checks that the count of nulls is right
	 */
	@Test
	public void testGetLockersNullCounter() {
		spaceship.createLocker(1,DEFAULT_CAPACITY);
		spaceship.createLocker(5,DEFAULT_CAPACITY*5);
		spaceship.createLocker(9,DEFAULT_CAPACITY*10);
		Locker[] lockers = spaceship.getLockers();
		int nullCounter = 0;
		for (Locker locker:
				lockers) {
			if (locker == null){
				nullCounter++;
			}
		}
		assertTrue(COUNT_NULL_MSG, nullCounter == 2);
	}
}
