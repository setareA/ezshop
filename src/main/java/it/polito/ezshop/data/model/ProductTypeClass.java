package it.polito.ezshop.data.model;

import it.polito.ezshop.data.ProductType;

public class ProductTypeClass implements ProductType {
    private Integer id;
    private Integer quantity;
    private String location;
    private String note;
    private String productDescription;
    private String barCode;
    private Double pricePerUnit;

    public ProductTypeClass(Integer id, Integer quantity, String location, String note, String productDescription,
			String barCode, Double pricePerUnit) {

		this.id = id;
		this.quantity = quantity;
		this.location = location;
		this.note = note;
		this.productDescription = productDescription;
		this.barCode = barCode;
		this.pricePerUnit = pricePerUnit;
	}
    
    

	@Override
	public String toString() {
		return "ProductTypeClass [id=" + id + ", quantity=" + quantity + ", location=" + location + ", note=" + note
				+ ", productDescription=" + productDescription + ", barCode=" + barCode + ", pricePerUnit="
				+ pricePerUnit +"]";
	}


    @Override
    public Integer getQuantity() {
        return this.quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;

    }

    @Override
    public String getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;

    }

    @Override
    public String getNote() {
        return this.note;
    }

    @Override
    public void setNote(String note) {
        this.note = note;

    }

    @Override
    public String getProductDescription() {
        return this.productDescription;
    }

    @Override
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;

    }

    @Override
    public String getBarCode() {
        return this.barCode;
    }

    @Override
    public void setBarCode(String barCode) {
        this.barCode= barCode;

    }

    @Override
    public Double getPricePerUnit() {
        return this.pricePerUnit;
    }

    @Override
    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;

    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;

    }

	public static boolean checkValidityProductcode(String productCode) {
		// TODO Auto-generated method stub
		int  tmp = 0 ;
		int j= 1;
		if(productCode == null)return false;
		if(productCode.length()<12 || productCode.length() >14 ) return false;
		try {
		for( j=1 ; j< productCode.length() ; j++ ) {
			int a = Integer.parseInt(String.valueOf(productCode.toCharArray()[j-1])) ;
			
			if(j%2==0) tmp +=  a*3;
			else tmp += a;
		}}
		catch (NumberFormatException e) { return false; }
		int tmp1 = tmp/10;		
		tmp1 = (tmp1+1)*10;
		tmp = tmp1 -tmp;
		if(Integer.parseInt(String.valueOf(productCode.toCharArray()[j-1])) == tmp) return true;
		else return false;
	}
}
