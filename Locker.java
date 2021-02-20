import oop.ex3.spaceship.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represent a locker object in a spaceship that contains different types of items.
 */
public class Locker implements StorageAbilities {

	// constants
	private static final int MOVE_TO_LTS = 1;
	private static final int SUCCESS = 0;
	private static final int FAILED = -1;
	private static final int FAILED_CONS = -2;
	private static final double START_LTS = 0.5;
	private static final double TARGET_LTS = 0.2;
	private static final String CONS_MSG = "Error: Your request cannot be completed at this time. Problem: " +
			"the locker cannot contain items of type %s, as it contains a contradicting item";
	private static final String ERROR_MSG = "Error: Your request cannot be completed at this time.";
	private static final String ERROR_STORAGE_MSG = "Error: Your request cannot be completed at this time. " +
			"Problem: no room for %s items of type %s";
	private static final String WARNING_MSG = "Warning: Action successful, but has caused items to be moved" +
											  " to storage";
	private static final String REMOVE_NEGATIVE_MSG = "Error: Your request cannot be completed at this time" +
													  ". Problem: cannot remove a negative number of items " +
													  "of type %s";
	private static final String REMOVE_ERROR_MSG = "Error: Your request cannot be completed at this time. " +
												   "Problem: the locker does not contain %s items of type %s";

	// class attributes
	private final int lockerCapacity; // the total amount of storage units it can hold.
	private final LongTermStorage spaceshipLts; // the long term storage of the spaceship of this locker.
	private final Item[][] spaceshipCons; // array of constraints that can't be together in the same locker.
	private HashMap<String, Integer> lockerInventory; // map of all items in this locker with their count
	// as value.
	private int availableCapacity; // how many storage units are unoccupied by Items.

	/**
	 * This constructor initializes a Locker object that is associated with the given long-term storage (you
	 * can assume they reside in the same spaceship), with the given capacity and Item constraints.
	 * @param lts long term storage object.
	 * @param capacity the total amount of storage units it can hold.
	 * @param constraints array of constraints that can't be together in the same locker
	 */
	public Locker(LongTermStorage lts, int capacity, Item[][] constraints){
		spaceshipLts = lts;
		lockerCapacity = capacity;
		spaceshipCons = constraints;
		lockerInventory = new HashMap<String, Integer>();
		availableCapacity = lockerCapacity;
	}

	/*
	This method check if an item cant added because the constraints.
	 */
	private boolean isCons(String type){
		for (Item[] constraint: spaceshipCons) {
			if (constraint[0].getType().equals(type) && lockerInventory.containsKey(constraint[1].getType())){
				return true;
			}
			else if (constraint[1].getType().equals(type) && lockerInventory.containsKey(constraint[0].getType())){
				return true;
			}
		}
		return false;
	}

	/*
	This method calc the amount of lockers that should added to the locker according to the rules (when it is
	less than 50% capacity and available).
	 */
	private int calcAmountToLocker(int n, Item item){
		int amount = n + getItemCount(item.getType());
		while (amount > 0 && item.getVolume() * amount > lockerCapacity*TARGET_LTS){
			amount--;
		}
		return amount;
	}

	/**
	 * This method adds n Items of the given type to the locker.
	 * @param item item to add
	 * @param n number of this item to add
	 * @return a number that represented the successful of the method
	 */
	public int addItem(Item item, int n){
		if (isCons(item.getType())){
			System.out.println(String.format(CONS_MSG, item.getType()));
			return FAILED_CONS;
		}
		if (n <= 0){
			System.out.println(ERROR_MSG);
			return FAILED;
		}
		if (n*item.getVolume() > availableCapacity){
			System.out.println(String.format(ERROR_STORAGE_MSG, n, item.getType()));
			return FAILED;
		}
		if (item.getVolume() * (n + getItemCount(item.getType())) <= lockerCapacity*START_LTS){
			lockerInventory.put(item.getType(), getItemCount(item.getType()) + n);
			availableCapacity -= item.getVolume()*n;
			return SUCCESS;
		}
		int amountToLocker = calcAmountToLocker(n, item);
		if (spaceshipLts.addItem(item, n - amountToLocker) == FAILED){
			System.out.println(String.format(ERROR_STORAGE_MSG, n, item.getType()));
			return FAILED;
		}
		System.out.println(WARNING_MSG);
		if (amountToLocker!=0){
			lockerInventory.put(item.getType(), amountToLocker);
			availableCapacity += item.getVolume()*(n - amountToLocker);
		}
		return MOVE_TO_LTS;
	}

	/**
	 * This method removes n Items of the type type from the locker
	 * @param item item to remove
	 * @param n amount of this item to remove
	 * @return a number that represented the successful of the method
	 */
	public int removeItem(Item item, int n){
		final int itemCount = getItemCount(item.getType());
		if (n <= 0){ // no Items should be removed
			System.out.println(String.format(REMOVE_NEGATIVE_MSG, item.getType()));
			return FAILED;
		}
		else if (itemCount < n){ //  less than n Items of this type in the locker, no items should be removed
			System.out.println(String.format(REMOVE_ERROR_MSG, n, item.getType()));
			return FAILED;
		}
		else if (itemCount == n){
			lockerInventory.remove(item.getType());
			availableCapacity += item.getVolume()*n;
			return SUCCESS;
		}
		else {
			lockerInventory.put(item.getType(), itemCount - n);
			availableCapacity += item.getVolume()*n;
			return SUCCESS;
		}
	}

	/**
	 * This method returns the number of Items of input type the locker contains.
	 * @param type type of item to check it count
	 * @return count of that item
	 */
	public int getItemCount(String type){
		if (!lockerInventory.containsKey(type)){
			return 0;
		}
		return lockerInventory.get(type);
	}

	/**
	 * This method returns a map of all the item types contained in the locker, and their respective
	 * quantities: {”Baseball bat”=1, ”helmet size 3"=5}
	 * @return map of all items in this locker with their count
	 */
	public Map<String, Integer> getInventory(){
		return lockerInventory;
	}

	/**
	 * This method returns the locker’s total capacity.
	 * @return the locker's total capacity
	 */
	public int getCapacity(){
		return lockerCapacity;
	}

	/**
	 * This method returns the locker’s available capacity: how many storage units are unoccupied by Items.
	 * @return the locker’s available capacity
	 */
	public int getAvailableCapacity(){
		return availableCapacity;
	}
}

