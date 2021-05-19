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
    private Integer warning;

    public ProductTypeClass(Integer id, Integer quantity, String location, String note, String productDescription,
			String barCode, Double pricePerUnit, Integer warning) {

		this.id = id;
		this.quantity = quantity;
		this.location = location;
		this.note = note;
		this.productDescription = productDescription;
		this.barCode = barCode;
		this.pricePerUnit = pricePerUnit;
		this.warning = warning;
	}
    
    

	@Override
	public String toString() {
		return "ProductTypeClass [id=" + id + ", quantity=" + quantity + ", location=" + location + ", note=" + note
				+ ", productDescription=" + productDescription + ", barCode=" + barCode + ", pricePerUnit="
				+ pricePerUnit + ", discountRate="  + ", warning=" + warning + "]";
	}




    public Integer getWarning() {
        return this.warning;
    }

    public void setWarning(Integer warning) {
        this.warning = warning;
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


}
