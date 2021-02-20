import oop.ex3.spaceship.Item;
import java.util.Map;

/**
 * This class is an interface of storage abilities
 */
public interface StorageAbilities {

	/**
	 * This method adds n Items of the given type to this storage.
	 * @param item item to add to the storage
	 * @param n amount of that item to add
	 * @return a number that represented the successful of the method
	 */
	public int addItem(Item item, int n);

	/**
	 * This method returns the number of Items of type this storage contains.
	 * @param type type of item to check it count
	 * @return count of that item
	 */
	public int getItemCount(String type);

	/**
	 * This method returns a map of all the Items contained in this storage unit, and their respective
	 * quantities.
	 * @return map of all items in this storage with their count
	 */
	public Map<String, Integer> getInventory();

	/**
	 * Returns this total capacity.
	 * @return the storage total capacity
	 */
	public int getCapacity();

	/**
	 * Returns this available capacity: how many storage units are unoccupied by Items.
	 * @return the storage available capacity
	 */
	public int getAvailableCapacity();
}
