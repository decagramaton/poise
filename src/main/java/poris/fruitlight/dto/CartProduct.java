package poris.fruitlight.dto;

import lombok.Data;

@Data
public class CartProduct {
	private int pid;
	private byte[] img;
	private String name;
	private String option;
	private int price;
	private int stock;
	private int shippingPrice;
	private int shippingFreeRule;
}
