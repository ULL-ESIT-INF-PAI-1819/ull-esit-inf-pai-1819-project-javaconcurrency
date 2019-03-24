package src;

/*
 * This brief example illustrates how separating logic from constructors may lead to more
 * flexible class designs, which may be extremely useful for serialized objects 
 */
public class Factory {

	public static void main(String[] args) {

		ItemFactory.Item it1 = ItemFactory.CraftCleaningItem("Wax");
		ItemFactory.Item it2 = ItemFactory.CraftFurnitureItem("Dish washer");
		ItemFactory.Item it3 = ItemFactory
				.CraftCookingItem("Cooking chopsticks");

		System.out.println("I just bought the following items:\n" + it1 + "\n"
				+ it2 + "\n" + it3 + "\n");

	}
}
