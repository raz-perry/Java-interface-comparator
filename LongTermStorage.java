import oop.ex3.spaceship.Item;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represent a long term storage of a spaceship that contains different types of items.
 */
public class LongTermStorage implements StorageAbilities {

	// constants
	private static final int LTS_CAPACITY = 1000;
	private static final int SUCCESS = 0;
	private static final int FAILED = -1;
	private static final String ERROR_MSG = "Error: Your request cannot be completed at this time.";
	private static final String ERROR_STORAGE_MSG = "Error: Your request cannot be completed at this time. " +
													"Problem: no room for %s items of type %s";

	// class attributes
	private final int capacity; // the total amount of storage units it can hold.
	private HashMap<String, Integer> inventory; // map of all items in this storage with their count
	// as value.
	private int availableCapacity; // how many storage units are unoccupied by Items.

	/**
	 *  This constructor initializes a Long-Term Storage object.
	 */
	public LongTermStorage(){
		capacity = LTS_CAPACITY;
		inventory = new HashMap<String, Integer>();
		availableCapacity = capacity;
	}

	/**
	 * This method adds n Items of the given type to the long term storage unit.
	 * @param item item to add to the storage
	 * @param n amount of that item to add
	 * @return a number that represented the successful of the method
	 */
	public int addItem(Item item, int n){
		if (n < 0){
			System.out.println(ERROR_MSG);
			return FAILED;
		}
		if (item.getVolume()*n > availableCapacity){ // n Items cannot be added to the locker - no Items to add
			System.out.println(String.format(ERROR_STORAGE_MSG, n, item.getType()));
			return FAILED;
		}
		inventory.put(item.getType(), getItemCount(item.getType()) + n);
		availableCapacity -= item.getVolume()*n;
		return SUCCESS;
	}

	/**
	 * This method resets the long-term storage’s inventory (i.e. after it is invoked the inventory does
	 * not contain any Item).
	 */
	public void resetInventory(){
		inventory.clear();
		availableCapacity = capacity;
	}

	/**
	 * This method returns the number of Items of type the long-term storage contains.
	 * @param type type of item to check it count
	 * @return count of that item
	 */
	public int getItemCount(String type){
		if (!inventory.containsKey(type)){
			return 0;
		}
		return inventory.get(type);
	}

	/**
	 * This method returns a map of all the Items contained in the long-term storage unit, and their
	 * respective quantities.
	 * @return map of all items in this storage with their count
	 */
	public Map<String, Integer> getInventory(){
		return inventory;
	}


	/**
	 * Returns the long-term storage’s total capacity.
	 * @return the storage total capacity
	 */
	public int getCapacity(){
		return capacity;
	}

	/**
	 * Returns the long-term storage’s available capacity: how many storage units are unoccupied by Items.
	 * @return the storage available capacity
	 */
	public int getAvailableCapacity(){
		return availableCapacity;
	}
}
