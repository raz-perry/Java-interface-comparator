import org.junit.*;
import static org.junit.Assert.*;
import oop.ex3.spaceship.ItemFactory;
import oop.ex3.spaceship.Item;
import java.util.HashMap;
import java.util.Random;

/**
 * This class is a test class fot LongTermStorage class
 */
public class LongTermTest {
	// constants
	private static final int TESTS_ITEMS_AMOUNT = 5;
	private static final int TESTS_MAX_ITEMS_TO_ADD = 50;
	private static final int RUNS = 10;
	private static final int SUCCESS = 0;
	private static final int FAILED = -1;
	private static final int NEGATIVE_AMOUNT = -2;
	private static final int RANDOM_AMOUNT_TO_ADD_SMALL = 5;
	private static final int DEFAULT_CAPACITY = 1000;
	private static final String WRONG_TYPE = "Raz";
	private static final String NEGATIVE_MSG = "unvalid amount of item to add";
	private static final String AVAILABLE_MSG = "should be enough available capacity";
	private static final String LESS_AVAILABLE_MSG = "not enough available capacity";
	private static final String ITEM_COUNT_NONE_MSG = "item doesn't exist";
	private static final String ITEM_COUNT_WRONG_MSG = "item counting wrong";
	private static final String INVENTORY_WRONG_MSG = "inventory failed";
	private static final String CAPACITY_WRONG_MSG = "wrong capacity value";
	private static final String AVAILABLE_WRONG_MSG = "wrong available capacity value";

	/*
	class attributes
	 */
	private static Item[] legalItems;
	private LongTermStorage lts;
	private final Random rnd = new Random();

	/**
	 * create class attributes
	 */
	@BeforeClass
	public static void createTestObjects(){
		ItemFactory factory = new ItemFactory();
		legalItems = factory.createAllLegalItems(); // legal items
	}

	/**
	 * this method default class attributes that change at most tests
	 */
	@Before
	public void defaultLts(){
		lts = new LongTermStorage();
	}

	/*
	addItem tests
	 */

	/**
	 * This test checks unvalid input - negative amount
	 */
	@Test
	public void testAddItemNegativeAmount(){
		assertEquals(NEGATIVE_MSG, FAILED, lts.addItem(legalItems[0], NEGATIVE_AMOUNT));
	}

	/**
	 * This test checks normal behavior - should be 0 and what happens when available capacity is smaller
	 */
	@Test
	public void testAddItem(){
		int i = 0;
		int itemIndex;
		int amountToAdd;
		boolean flag = false;
		while (i < RUNS || !flag){
			itemIndex = rnd.nextInt(TESTS_ITEMS_AMOUNT);
			amountToAdd = rnd.nextInt(TESTS_MAX_ITEMS_TO_ADD);
			if ((amountToAdd+1)*legalItems[itemIndex].getVolume() <= lts.getAvailableCapacity()){
				assertEquals(AVAILABLE_MSG, SUCCESS, lts.addItem(legalItems[itemIndex], amountToAdd+1));
			}
			else {
				flag = true;
				assertEquals(LESS_AVAILABLE_MSG, FAILED, lts.addItem(legalItems[itemIndex], amountToAdd+1));
			}
			i++;
		}
	}

	/*
	resetInventory tests
	 */

	/**
	 * This test add items to the storage and than reset the inventory. checks if before the reset the
	 * inventory wasn't empty but after is empty.
	 */
	@Test
	public void testResetInventory(){
		int amountToAdd;
		for (int i = 0; i < TESTS_ITEMS_AMOUNT; i++){
			amountToAdd = rnd.nextInt(RANDOM_AMOUNT_TO_ADD_SMALL);
			lts.addItem(legalItems[i], amountToAdd);
		}
		assertTrue("reset inventory failed", 0 < lts.getInventory().size());
		lts.resetInventory();
		assertEquals("reset inventory failed", 0, lts.getInventory().size());
	}

	/*
	getItemCount tests
	 */

	/**
	 * This test checks capacity validation
	 */
	@Test
	public void testGetItemCountOverCapacity(){
		lts.addItem(legalItems[2], lts.getCapacity());
		assertEquals(ITEM_COUNT_WRONG_MSG,  0, lts.getItemCount(legalItems[2].getType()));
	}

	/**
	 * This test checks item type validation
	 */
	@Test
	public void testGetItemCountUnvalidType(){
		assertEquals(ITEM_COUNT_NONE_MSG, 0, lts.getItemCount(WRONG_TYPE));
		assertEquals(ITEM_COUNT_NONE_MSG, 0, lts.getItemCount(legalItems[0].getType()));
	}

	/**
	 * This test adds any item once so should be the same as added
	 */
	@Test
	public void testGetItemCountManyItemsSingleAdd(){
		int amountToAdd;
		for (int i = 0; i < TESTS_ITEMS_AMOUNT; i++){
			amountToAdd = rnd.nextInt(RANDOM_AMOUNT_TO_ADD_SMALL);
			lts.addItem(legalItems[i], amountToAdd);
			assertEquals(ITEM_COUNT_WRONG_MSG, amountToAdd, lts.getItemCount(legalItems[i].getType()));
		}
	}

	/**
	 * This test checks the count after added the same item many times.
	 */
	@Test
	public void testGetItemCountManyAdds(){
		lts.addItem(legalItems[0], 50);
		if (lts.addItem(legalItems[0], 50) != -1){
			assertEquals(ITEM_COUNT_WRONG_MSG, 100, lts.getItemCount(legalItems[0].getType()));
			if (lts.addItem(legalItems[0], 50) != -1){
				assertEquals(ITEM_COUNT_WRONG_MSG, 150, lts.getItemCount(legalItems[0].getType()));
				if (lts.addItem(legalItems[0], 50) != -1){
					assertEquals(ITEM_COUNT_WRONG_MSG, 200, lts.getItemCount(legalItems[0].getType()));
				}
			}
		}
	}

	/*
	getInventory tests
	 */

	/**
	 * This test checks empty storage is empty map
	 */
	@Test
	public void testGetInventoryEmpty(){
		assertEquals(INVENTORY_WRONG_MSG, 0, lts.getInventory().size());
	}

	/**
	 * This test checks the inventory when storage included all items added once per each
	 */
	@Test
	public void testGetInventory(){
		HashMap<String, Integer> tempMap = new HashMap<String, Integer>();
		int amountToAdd;
		for (int i = 0; i < TESTS_ITEMS_AMOUNT; i++){
			amountToAdd = rnd.nextInt(RANDOM_AMOUNT_TO_ADD_SMALL);
			lts.addItem(legalItems[i], amountToAdd+1);
			tempMap.put(legalItems[i].getType(), amountToAdd+1);
		}
		assertEquals(INVENTORY_WRONG_MSG, tempMap, lts.getInventory());
	}

	/**
	 * This test checks the inventory after added same item many times.
	 */
	@Test
	public void testGetInventoryAfterChange(){
		HashMap<String, Integer> tempMap = new HashMap<String, Integer>();
		lts.addItem(legalItems[2], 10);
		lts.addItem(legalItems[3], 15);
		lts.addItem(legalItems[2], 20);
		tempMap.put(legalItems[3].getType(), 15);
		tempMap.put(legalItems[2].getType(), 30);
		assertEquals(INVENTORY_WRONG_MSG, tempMap, lts.getInventory());
	}

	/*
	getCapacity tests
	 */

	/**
	 * This test checks is the same as init
	 */
	@Test
	public void testGetCapacity(){
		assertEquals(CAPACITY_WRONG_MSG, DEFAULT_CAPACITY, lts.getCapacity());
	}

	/*
	getAvailableCapacity tests
	 */

	/**
	 * This test checks the available capacity at default (need to be the same has the capacity) and after
	 * added many items.
	 */
	@Test
	public void testGetAvailableCapacity(){
		assertEquals(AVAILABLE_WRONG_MSG, DEFAULT_CAPACITY, lts.getAvailableCapacity());
		if (lts.addItem(legalItems[0], 99) != -1){
			int new_cap = DEFAULT_CAPACITY-(legalItems[0].getVolume()*99);
			assertEquals(AVAILABLE_WRONG_MSG, new_cap, lts.getAvailableCapacity());
			if (lts.addItem(legalItems[1], 99) != -1){
				new_cap -= legalItems[1].getVolume()*99;
				assertEquals(AVAILABLE_WRONG_MSG,new_cap, lts.getAvailableCapacity());
				if (lts.addItem(legalItems[2], 99) != -1){
					new_cap -= legalItems[2].getVolume()*99;
					assertEquals(AVAILABLE_WRONG_MSG,new_cap, lts.getAvailableCapacity());
				}
			}
		}
	}

	/**
	 * This test checks the available capacity after inventory was reset
	 */
	@Test
	public void testGetAvailableCapacityAfterReset(){
		lts.addItem(legalItems[3], 20);
		lts.addItem(legalItems[4], 30);
		lts.addItem(legalItems[2], 5);
		lts.resetInventory();
		assertEquals(AVAILABLE_WRONG_MSG, DEFAULT_CAPACITY, lts.getAvailableCapacity());
	}
}
