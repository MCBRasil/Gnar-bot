package xyz.gnarbot.gnar.textadventure;

import java.util.HashMap;

public class Inventory{
	private HashMap<Integer, Item> storage = new HashMap<>();

	private int inventorySize = 9;

	public Inventory(int size){
		this.inventorySize = size;
	}

	public Inventory(){
		this.inventorySize = 9;
	}

	public int getInventorySize() {
		return inventorySize;
	}

	public int addItem(Item item){
		for (int slot = 0; slot < getInventorySize(); slot++){
			if (!storage.containsKey(slot)){
				storage.put(slot, item);
				return slot;
			}
		}
		return -1;
	}

	public Item getItem(int slot){
		if (storage.containsKey(slot)){
			return storage.get(slot);
		}return null;
	}

	public void setItem(int slot, Item item){
		storage.put(slot, item);
	}

}