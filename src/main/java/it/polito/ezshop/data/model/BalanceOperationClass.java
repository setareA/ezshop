package it.polito.ezshop.data.model;

import it.polito.ezshop.data.BalanceOperation;
import java.time.LocalDate;


public class BalanceOperationClass implements BalanceOperation {
	
	private Integer balanceId ;
	private LocalDate localDate ;
	private double money ;
	private String type;
	
	@Override
	public int getBalanceId() {
		// TODO Auto-generated method stub
		return this.balanceId;
	}
	@Override
	public void setBalanceId(int balanceId) {
		// TODO Auto-generated method stub
		this.balanceId = balanceId;
		
	}
	@Override
	public LocalDate getDate() {
		// TODO Auto-generated method stub
		return localDate;
	}
	@Override
	public void setDate(LocalDate date) {
		// TODO Auto-generated method stub
		this.localDate =  date ;
	}
	@Override
	public double getMoney() {
		// TODO Auto-generated method stub
		return money;
	}
	@Override
	public void setMoney(double money) {
		// TODO Auto-generated method stub
		this.money = money;
	}
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}
	@Override
	public void setType(String type) {
		// TODO Auto-generated method stub
		this.type = type;
	}
	 
}
