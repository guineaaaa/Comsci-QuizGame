package model;

public class Item {
	private int itemId;
	private String name;
	private String type;
	private int price;
	
	public Item(int itemId, String name, String type, int price) {
		this.itemId=itemId;
		this.name=name;
		this.type=type;
		this.price=price;
	}
	
    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

}
