import oop.ex3.spaceship.Item;
import java.util.HashMap;

/**
 * This class represent a spaceship object that manages lockers to its crew members. It contains crew
 * members, lockers and a long term storage.
 */
public class Spaceship {

	private static final int SUCCESS = 0;
	private static final int WRONG_ID = -1;
	private static final int WRONG_CAPACITY = -2;
	private static final int FULL = -3;

	// class attributes
	private final int allowedLockers; // number of lockers allowed in this spaceship.
	private final String spaceshipName; // name of this spaceship.
	private final int[] spaceshipCrewIDs; // array of crew members id's.
	private final Item[][] cons; // array of constraints that can't be together in the same locker.
	private final HashMap<Locker, Integer> crewLockers; // map of lockers as keys and the relevant crew id as value
	private final LongTermStorage lts; // the long term storage of this spaceship.

	/**
	 *  This constructor initializes a Spaceship object that mange lockers to its crew members.
	 * @param name // name of a spaceship
	 * @param crewIDs // array of crew id's
	 * @param numOfLockers // number of lockers allowed
	 * @param constraints // array of constraints that can't be together
	 */
	public Spaceship(String name, int[] crewIDs, int numOfLockers, Item[][] constraints){
		spaceshipName = name;
		spaceshipCrewIDs = crewIDs;
		allowedLockers = numOfLockers;
		cons = constraints;
		crewLockers = new HashMap<Locker, Integer>();
		lts = new LongTermStorage();
	}

	/**
	 * This method returns the long-term storage object associated with that Spaceship.
	 * @return the relevant longTermStorage object
	 */
	public LongTermStorage getLongTermStorage(){
		return lts;
	}

	/*
	This method checks if input id is valid
	 */
	private boolean isIdValid(int crewID){
		boolean flag = false;
		for (int id: spaceshipCrewIDs) {
			if (id == crewID){
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * This method creates a Locker object, and adds it as part of the Spaceship’s storage.
	 * The new Locker is associated with a crew member with the given id, and has the given capacity.
	 *  @param crewID id of a crew member
	 * @param capacity capacity of the new wanted locker
	 * @return a number that represented the successful of the method
	 */
	public int createLocker(int crewID, int capacity){
		if (!isIdValid(crewID)){
			return WRONG_ID;
		}
		if (capacity < 0){
			return WRONG_CAPACITY;
		}
		if (crewLockers.keySet().size() == allowedLockers){
			return FULL;
		}
		Locker locker = new Locker(lts, capacity, cons);
		crewLockers.put(locker, crewID);
		return SUCCESS;
	}

	/**
	 * This methods returns an array with the crew’s ids.
	 * @return array of crew’s ids
	 */
	public int[] getCrewIDs(){
		return spaceshipCrewIDs;
	}

	/**
	 * This methods returns an array of the Lockers, whose length is numOfLockers
	 * @return array of the Lockers with length of numOfLockers
	 */
	public Locker[] getLockers(){
		Locker[] lockers = new Locker[allowedLockers];
		int index = 0;
		for (Locker locker: crewLockers.keySet()) {
			lockers[index] = locker;
			index++;
		}
		return lockers;
	}
}
