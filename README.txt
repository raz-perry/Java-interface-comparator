raz.perry


=============================
=      File description     =
=============================
StorageAbilities.java - This class is an interface that describes methods of things with storage abilities.
Locker.java - This class represent a locker object in a spaceship that contains different types of items, 
implements StorageAbilities.
LongTermStorage.java - This class represent a long term storage of a spaceship that contains different types
of items, implements StorageAbilities. 
Spaceship.java - This class represent a spaceship object that manages lockers to its crew members. It 
contains crew members, lockers and a long term storage.

LockerTest.java - tests for Locker.java
LongTermTest.java - test for LongTermStorage.java
SpaceshipTest.java - tests for Spaceship.java
SpaceshipDepositoryTest.java - suite for all tests in that section

BoopingSite.java - This class is providing the users of the site the ability to get a list of hotels based on
different parameters.
SortByRating.java - This class is a comparator that define sorted hotels by their rating stars and when
equals by name.
SortByProximity.java - This class is a comparator that define sorted hotels by their proximity according to 
an input coordinates.

BoopingSiteTest.java - tests for BoopingSite.java

=============================
=          Design           =
=============================
Spaceship:
First of all i choose to create an interface that describes the abilities of any storage in the spaceship.
So locker and long term storage are implements this interface. I chose that design and not abstract class
because as i see it, locker and long term storage have the same abilites as storage types but they aren't
the same type of any super-class.

I choose to store the locker information in HashMap when keys are types of items and values are the amount
of this type in the locker. I choose that kind of map because the order of the items doesn't metter and the
most importent thing is to get fastly the value according to the key (for checking at addItem and for 
getItemCount). Also i choose map becuase i wanted to use the method getOrDefault and by that its make it
simplier when there is no different between added items that exist or added new items.
Another choice i did is to create attribute of current available capacity and update it immediatly at any
change (add new items, removing items, moving them to the long term storage...). 
In longTermStorage i also stored the items in HashMap from the same reasons and also create the available
attribute that updates in every change.


BoopingSite:
I choose to work with comparators - I create two new classes as comparators to define the sort.
One class is for the rating method and the other class is for both proximity methods.

=============================
=  Implementation details   =
=============================
Tests:
I tried to check edge cases for each methods, unvalid inputs and as same "normal behavior".
Most tests are randoms and checks all kinds of given data.
I splitted the tests in porpuse that any test has only one kind of thing to check - so there could be many
tests for one method.

Comparators:
I decided in each method to convert the dataset needed (by city or full) to a List because i wanted to use
after the Sort method of Collections. So i created comparators for each sorted type and i executed the sort
method according to the belonging comparator. By that way the code became very modulary and easy to change,
all sort logics are in one place and all main logics (like a driver) are in other place. Also by that it was
easy to write to third method withoud code duplication (the method is pretty like a combination of the
previous ones)

=============================
=    Answers to questions   =
=============================
1. how i use the dataset in booking testing:
I stored all sites in an array and run each general test on all of them. In general test i meant unvalid
inputs and normal behavior check - for example to check that an array is really sorted.
In those kind of checks i can expect for the same behavior from each dataset so there wasn't any problem
to run them all.
I add few more tests on specific dataset because i use the information i had one them. For example i checked
that the empty dataset return empty hotels list for any ask.
