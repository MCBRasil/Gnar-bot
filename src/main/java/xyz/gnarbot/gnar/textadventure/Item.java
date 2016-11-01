package xyz.gnarbot.gnar.textadventure;

public class Item{
	private static int lastID = 0;

	private int id;
	private String itemName, itemType, itemDescription;
	private int amount = 0;
	public Item(String itemName, String itemType, String itemDescription, int amount){
		this.itemName = itemName;
		this.itemType = itemType;
		this.itemDescription = itemDescription;
		this.id = lastID++;
		this.amount = amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void reduceAmount(){
		this.amount--;
	}

	public void addAmount(){
		this.amount++;
	}

	public void reduceAmount(int amount){
		this.amount -= amount;
		if (this.amount < 0){
			this.amount = 0;
		}
	}

	public void addAmount(int amount){
		this.amount += amount;
	}

	public int getAmount() {
		return amount;
	}

	public int getID() {
		return id;
	}

	public String getItemName() {
		return itemName;
	}

	public String getItemType() {
		return itemType;
	}

	public String getItemDescription() {
		return itemDescription;
	}
}

