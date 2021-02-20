import org . junit .*;
import oop.ex3.spaceship.ItemFactory;
import oop.ex3.spaceship.Item;
import java.util.HashMap;
import java.util.Random;
import static org.junit.Assert.*;

/**
 * This class is a test class fot Locker class
 */
public class LockerTest {
	/*
	constants
	 */
	private static final int MOVE_TO_LTS = 1;
	private static final int SUCCESS = 0;
	private static final int FAILED = -1;
	private static final int FAILED_CONS = -2;

	private static final int TESTS_LOCKERS_AMOUNT = 3;
	private static final int TESTS_ITEMS_AMOUNT = 5;
	private static final int RANDOM_AMOUNT_TO_ADD = 5;
	private static final int BIG_LOCKER_CAPACITY = 1000;

	private static final int NEGATIVE_AMOUNT = -2;
	private static final String WRONG_TYPE = "Raz";
	private static final String NEGATIVE_MSG = "unvalid amount of item to add";
	private static final String AVAILABLE_MSG = "should be enough available capacity";
	private static final String LESS_AVAILABLE_MSG = "not enough available capacity";
	private static final String CAPACITY_MSG = "item is bigger than locker capacity";
	private static final String CONS_MSG = "item is in constraints and can't added";
	private static final String LTS_MSG = "more than 50%, should moved to long term storage";
	private static final String REMOVE_EMPTY_MSG = "can't remove item from empty locker";
	private static final String REMOVE_UNVALID_NUMBER_MSG = "unvalid number to remove";
	private static final String REMOVE_TOO_MUCH_MSG = "can't remove more items than exist";
	private static final String REMOVE_MSG = "item should be removed";
	private static final String ITEM_COUNT_NONE_MSG = "item doesn't exist";
	private static final String ITEM_COUNT_WRONG_MSG = "item counting wrong";
	private static final String ITEM_COUNT_WRONG_LTS_MSG = "item counting wrong after added item overed 50%" +
														   " capacity and should moved to long term storage";
	private static final String INVENTORY_WRONG_MSG = "inventory failed";
	private static final String CAPACITY_WRONG_MSG = "wrong capacity value";
	private static final String CAPACITY_NEGATIVE_MSG = "capacity can't be negative number";
	private static final String AVAILABLE_WRONG_MSG = "wrong available capacity value";

	/*
	class attributes
	 */
	private static ItemFactory factory;
	private static Item[] legalItems;
	private static Item[][] cons;
	private static LongTermStorage lts;
	private Locker[] lockers = new Locker[TESTS_LOCKERS_AMOUNT];
	private Locker tempLocker;
	private Locker bigLocker;
	private Random rnd = new Random();
	private HashMap<String, Integer> tempMap;

	/**
	 * create class attributes
	 */
	@BeforeClass
	public static void createTestObjects(){
		factory = new ItemFactory();
		legalItems = factory.createAllLegalItems(); // legal items
		cons = factory.getConstraintPairs();
		lts = new LongTermStorage();
	}

	/**
	 * this method default class attributes that change at most tests
	 */
	@Before
	public void defaultValues(){
		for (int i = 0; i < TESTS_LOCKERS_AMOUNT; i++){
			lockers[i] = new Locker(lts, 10*(i+1), cons);
		}
		tempMap = new HashMap<String, Integer>();
		bigLocker = new Locker(lts, BIG_LOCKER_CAPACITY, cons);
	}

	/*
	this method creates a locker according to an input capacity
	 */
	private Locker createLockerByCapacity(int capacity){
		return new Locker(lts, capacity, cons);
	}

	/*
	addItem tests
	 */
	/**
	 * This test checks unvalid input - negative amount
	 */
	@Test
	public void testAddItemNegativeAmount(){
		assertEquals(NEGATIVE_MSG, FAILED, lockers[0].addItem(legalItems[0], NEGATIVE_AMOUNT));
	}

	/**
	 * This test checks unvalid input - zero amount
	 */
	@Test
	public void testAddItemZeroAmount(){
		assertEquals(NEGATIVE_MSG, FAILED, lockers[1].addItem(legalItems[1], 0));
	}

	/**
	 * This test checks normal behavior (adding without problems)
	 */
	@Test
	public void testAddItem(){
		for (int i = 0; i < legalItems.length; i++){
			if (!legalItems[i].getType().equals("football")){
				assertEquals(AVAILABLE_MSG, SUCCESS, bigLocker.addItem(legalItems[i], 1));
			}
		}
	}

	/**
	 * This test checks if there is no more available capacity - should be -1
	 */
	@Test
	public void testAddItemAvailableCapacity(){
		Item big = null; // vol 10
		Item small = null; // vol 2
		Item helmet = null;
		for (Item item:
				legalItems) {
			if (item.getVolume() == 10){
				big = item;
			}
			else if (item.getVolume() == 2){
				small = item;
			}
			else if (item.getVolume() == 3){
				helmet = item;
			}
		}
		if (big != null && small != null && helmet != null){
			assertEquals(AVAILABLE_MSG, SUCCESS, lockers[1].addItem(big, 1));
			assertEquals(AVAILABLE_MSG, SUCCESS, lockers[1].addItem(small, 5));
			assertEquals(LESS_AVAILABLE_MSG, FAILED, lockers[1].addItem(helmet, 1));
		}
	}

	/**
	 * This test checks if capacity is smaller than item wanted - should be -1
	 */
	@Test
	public void testAddItemSmallCapacity(){
		assertEquals(CAPACITY_MSG, FAILED, bigLocker.addItem(legalItems[2], BIG_LOCKER_CAPACITY));
	}

	/**
	 * This test checks constraints problems - should be -2
	 */
	@Test
	public void testAddItemCons(){
		Item football = null;
		Item baseballBat = null;
		for (Item item:
			 legalItems) {
			if (item.getType().equals("football")){
				football = item;
			}
			else if (item.getType().equals("baseball bat")){
				baseballBat = item;
			}
		}
		if (football != null && baseballBat != null){
			lockers[0].addItem(football, 1);
			assertEquals(CONS_MSG, FAILED_CONS, lockers[0].addItem(baseballBat, 1));
			lockers[1].addItem(baseballBat, 1);
			assertEquals(CONS_MSG, FAILED_CONS, lockers[1].addItem(football, 1));
		}
	}

	/**
	 * This test checks moving items to lts when locker was empty
	 */
	@Test
	public void testAddItemMoveToLts(){
		tempLocker = createLockerByCapacity(legalItems[4].getVolume()*5);
		assertEquals(LTS_MSG, MOVE_TO_LTS, tempLocker.addItem(legalItems[4], 3));
	}

	/**
	 * This test checks moving items to lts when locker had items
	 */
	@Test
	public void testAddItemMoveToLtsNotEmpty(){
		tempLocker = createLockerByCapacity(legalItems[0].getVolume()*5);
		tempLocker.addItem(legalItems[0], 2);
		assertEquals(LTS_MSG, MOVE_TO_LTS, tempLocker.addItem(legalItems[0], 1));
	}

	/*
	removeItem tests
	 */
	/**
	 * This test checks impossible remove - empty locker
	 */
	@Test
	public void testRemoveItemFromEmptyLocker(){
		assertEquals(REMOVE_EMPTY_MSG, FAILED, lockers[0].removeItem(legalItems[2], 1));
	}

	/**
	 * This test checks impossible remove - unvalid number
	 */
	@Test
	public void testRemoveItemUnvalidNumber(){
		lockers[1].addItem(legalItems[3], 1);
		assertEquals(REMOVE_UNVALID_NUMBER_MSG, FAILED, lockers[1].removeItem(legalItems[3], -2));
	}

	/**
	 * This test checks impossible remove - higher number
	 */
	@Test
	public void testRemoveItemMoreThanExist(){
		bigLocker.addItem(legalItems[2], 2);
		assertEquals(REMOVE_TOO_MUCH_MSG, FAILED, bigLocker.removeItem(legalItems[2],	3));
	}

	/**
	 * This test checks impossible remove - higher number when locker wasn't empty
	 */
	@Test
	public void testRemoveItemMoreThanExistAfterChange(){
		tempLocker = createLockerByCapacity(10*legalItems[3].getVolume());
		tempLocker.addItem(legalItems[3], 4);
		assertEquals(REMOVE_MSG, SUCCESS, tempLocker.removeItem(legalItems[3],	3));
		assertEquals(REMOVE_TOO_MUCH_MSG, FAILED, tempLocker.removeItem(legalItems[3],	3));
	}

	/**
	 * This test checks normal behavior - remove should success
	 */
	@Test
	public void testRemoveItem(){
		bigLocker.addItem(legalItems[4], 5);
		assertEquals(REMOVE_MSG, SUCCESS, bigLocker.removeItem(legalItems[4], 1));
		assertEquals(REMOVE_MSG, SUCCESS, bigLocker.removeItem(legalItems[4], 2));
		assertEquals(REMOVE_MSG, SUCCESS, bigLocker.removeItem(legalItems[4], 2));
	}

	/*
	getItemCount tests
	 */

	/**
	 * This test checks unvalid inputs should be 0
	 */
	@Test
	public void testGetItemCountUnvalidType(){
		assertEquals(ITEM_COUNT_NONE_MSG, 0, lockers[0].getItemCount(WRONG_TYPE));
		assertEquals(ITEM_COUNT_NONE_MSG, 0, lockers[1].getItemCount(legalItems[0].getType()));
	}

	/**
	 * This test add any item once (for sure enough available storage place) so should be the same as added
	 */
	@Test
	public void testGetItemCountManyItemsSingleAdd(){
		int amountToAdd;
		for (int i = 0; i < TESTS_ITEMS_AMOUNT; i++){
			amountToAdd = rnd.nextInt(RANDOM_AMOUNT_TO_ADD);
			if (bigLocker.addItem(legalItems[i], amountToAdd) != FAILED_CONS){
				assertEquals(ITEM_COUNT_WRONG_MSG, amountToAdd,
							 bigLocker.getItemCount(legalItems[i].getType()));
			}
		}
	}

	/**
	 * This test checks the count after added the same item many times (for sure enough space)
	 */
	@Test
	public void testGetItemCountSingleItemManyAdds(){
		tempLocker = createLockerByCapacity(10*legalItems[0].getVolume());
		tempLocker.addItem(legalItems[0], 2);
		assertEquals(ITEM_COUNT_WRONG_MSG, 2, tempLocker.getItemCount(legalItems[0].getType()));
		tempLocker.addItem(legalItems[0], 1);
		assertEquals(ITEM_COUNT_WRONG_MSG, 3,
					 tempLocker.getItemCount(legalItems[0].getType()));
		tempLocker.addItem(legalItems[0], 2);
		assertEquals(ITEM_COUNT_WRONG_MSG, 5,
					 tempLocker.getItemCount(legalItems[0].getType()));
	}

	/**
	 * This test checks the count after item moved to lts
	 */
	@Test
	public void testGetItemCountMoveToLtsSingle(){
		tempLocker = createLockerByCapacity((int) (legalItems[1].getVolume() * 1.5));
		tempLocker.addItem(legalItems[1], 1);
		assertEquals(ITEM_COUNT_WRONG_LTS_MSG,  0,
					 tempLocker.getItemCount(legalItems[1].getType()));
	}

	/**
	 * This test checks the count after item moved to lts when locker wasn't empty
	 */
	@Test
	public void testGetItemCountMoveToLtsMany(){
		tempLocker = createLockerByCapacity(legalItems[2].getVolume()*5);
		tempLocker.addItem(legalItems[2], 2);
		assertEquals(ITEM_COUNT_WRONG_MSG, 2, tempLocker.getItemCount(legalItems[2].getType()));
		tempLocker.addItem(legalItems[2], 2);
		assertEquals(ITEM_COUNT_WRONG_LTS_MSG, 1, tempLocker.getItemCount(legalItems[2].getType()));
	}

	/*
	getInventory tests
	 */

	/**
	 * This test checks if empty locker return empty map
	 */
	@Test
	public void testGetInventoryEmpty(){
		assertEquals(INVENTORY_WRONG_MSG, 0, lockers[2].getInventory().size());
	}

	/**
	 * This test checks the inventory of a locker that includes all items added once per each (according to
	 * cons)
	 */
	@Test
	public void testGetInventory(){
		int amountToAdd;
		for (int i = 0; i < TESTS_ITEMS_AMOUNT; i++){
			amountToAdd = rnd.nextInt(RANDOM_AMOUNT_TO_ADD);
			if (bigLocker.addItem(legalItems[i], amountToAdd+1) != FAILED_CONS){
				tempMap.put(legalItems[i].getType(), amountToAdd+1);
			}
		}
		assertEquals(INVENTORY_WRONG_MSG, tempMap, bigLocker.getInventory());
	}

	/**
	 * This test checks the inventory after added same item many times.
	 */
	@Test
	public void testGetInventoryAfterChange(){
		bigLocker.addItem(legalItems[2], 2);
		bigLocker.addItem(legalItems[2], 5);
		tempMap.put(legalItems[2].getType(), 7);
		assertEquals(INVENTORY_WRONG_MSG, tempMap, bigLocker.getInventory());
	}

	/**
	 * This test checks the inventory after items moved to lts
	 */
	@Test
	public void testGetInventoryAfterMoved(){
		tempLocker = createLockerByCapacity(legalItems[0].getVolume()*10);
		tempLocker.addItem(legalItems[0], 2);
		tempLocker.addItem(legalItems[0], 4);
		tempMap.put(legalItems[0].getType(), 2);
		assertEquals(INVENTORY_WRONG_MSG, tempMap, tempLocker.getInventory());
	}

	/**
	 * This test checks the inventory after items removed from the locker
	 */
	@Test
	public void testGetInventoryAfterRemoved(){
		bigLocker.addItem(legalItems[3], 2);
		bigLocker.addItem(legalItems[3], 5);
		bigLocker.removeItem(legalItems[3], 1);
		tempMap.put(legalItems[3].getType(), 6);
		assertEquals(INVENTORY_WRONG_MSG, tempMap, bigLocker.getInventory());
	}

	/*
	getCapacity tests
	 */

	/**
	 * This test checks capacity is equal as init
	 */
	@Test
	public void testGetCapacity(){
		assertEquals(CAPACITY_WRONG_MSG, BIG_LOCKER_CAPACITY, bigLocker.getCapacity());
	}

	/**
	 * This test checks capacity is positive
	 */
	@Test
	public void testGetCapacityPositive(){
		for (int i = 0; i < TESTS_LOCKERS_AMOUNT; i++){
			assertTrue(CAPACITY_NEGATIVE_MSG,lockers[i].getCapacity()  >= 0);
		}
	}

	/*
	getAvailableCapacity tests
	default value has to be the same as capacity (test 1). Test 2 checks the available capacity after added
	 many items and test 3 checks it after removing items.
	 */

	/**
	 * This test checks that default value is the same as capacity
	 */
	@Test
	public void testGetAvailableCapacityDefault(){
		for (int i = 0; i < TESTS_LOCKERS_AMOUNT; i++){
			assertEquals(AVAILABLE_WRONG_MSG,10*(i+1),lockers[i].getAvailableCapacity());
		}
	}

	/**
	 * This test checks the available capacity after added many items
	 */
	@Test
	public void testGetAvailableCapacityManyItems(){
		int new_capacity = BIG_LOCKER_CAPACITY;
		int amountToAdd;
		for (int i = 0; i < TESTS_ITEMS_AMOUNT; i++){
			amountToAdd = rnd.nextInt(RANDOM_AMOUNT_TO_ADD);
			if (bigLocker.addItem(legalItems[i], amountToAdd) != FAILED_CONS){
				new_capacity -= legalItems[i].getVolume()*amountToAdd;
				assertEquals(AVAILABLE_WRONG_MSG, new_capacity, bigLocker.getAvailableCapacity());
			}
		}
	}

	/**
	 * This test checks the available capacity after items removed from the locekr
	 */
	@Test
	public void testGetAvailableCapacityAfterRemove(){
		bigLocker.addItem(legalItems[3], 2);
		bigLocker.addItem(legalItems[3], 3);
		bigLocker.removeItem(legalItems[3], 1);
		bigLocker.removeItem(legalItems[3], 2);
		int new_capacity = BIG_LOCKER_CAPACITY-legalItems[3].getVolume()*2;
		assertEquals(AVAILABLE_WRONG_MSG, new_capacity, bigLocker.getAvailableCapacity());
	}
}
