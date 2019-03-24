package src;

public class ItemFactory {

	public static class Item {
		private final String name;
		private final String use;
		private final int serialNo;

		/*
		 * NOTE THAT THIS CONSTRUCTOR IS PRIVATE (AND LEGAL IN JAVA)
		 */
		private Item(String Name, String Use, int serialNo) {
			this.serialNo = serialNo;
			this.name = Name;
			this.use = Use;
		}

		public String getName() {
			return name;
		}

		public String getUse() {
			return use;
		}

		public int getSerialNo() {
			return serialNo;
		}

		public String toString() {
			return ("" + name + ":\n" + "Category: " + use
					+ "\nSerial Number: " + serialNo + "\n");
		}
	}

	private static int SerialNo = 0;

	/*
	 * ItemFactory "owns" Item class definition, so it can create Items at will.
	 * However, outsiders cannot creaft items without ItemFactory
	 */

	public static Item CraftFurnitureItem(String itemName) {
		return new Item(itemName, "Furniture", SerialNo++);
	}

	public static Item CraftCookingItem(String itemName) {
		return new Item(itemName, "Cooking", SerialNo++);
	}

	public static Item CraftCleaningItem(String itemName) {
		return new Item(itemName, "Cleaning", SerialNo++);
	}
}
