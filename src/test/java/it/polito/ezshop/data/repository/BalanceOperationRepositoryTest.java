package it.polito.ezshop.data.repository;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.model.OrderClass;
import it.polito.ezshop.data.model.ReturnTransactionClass;
import it.polito.ezshop.data.model.SaleTransactionClass;
import it.polito.ezshop.data.model.TicketEntryClass;

public class BalanceOperationRepositoryTest {
	static BalanceOperationRepository balanceOperationRepository;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		balanceOperationRepository = BalanceOperationRepository.getInstance();
		balanceOperationRepository.deleteTables();
        balanceOperationRepository.initialize();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetBalance() throws SQLException  {
		double balanceUpdate = 10;
		double balanceNow;
		balanceNow = balanceOperationRepository.getBalance();
	
		
		balanceOperationRepository.setBalance(balanceUpdate);
		assertEquals(balanceOperationRepository.getBalance(), balanceNow + balanceUpdate , 0.001);
		
		balanceUpdate = -10;
		balanceNow = balanceOperationRepository.getBalance();
		
		balanceOperationRepository.setBalance(balanceUpdate);
		assertEquals(balanceOperationRepository.getBalance(), balanceNow + balanceUpdate , 0.001);		
	}

	@Test
	public void testGetBalanceStatement() {
		fail("Not yet implemented");
	}

	@Test
	public void testResetBalance() throws SQLException {
		double balance = 0;
		balanceOperationRepository.resetBalance();
		assertEquals(balanceOperationRepository.getBalance(), balance , 0.001);
	}


	@Test
	public void testInsertBalance() {
		assertEquals(balanceOperationRepository.insertBalance(),false);
		balanceOperationRepository.deleteRow("balanceTable", "id", "1");
		assertEquals(balanceOperationRepository.insertBalance(),true);
		assertEquals(balanceOperationRepository.insertBalance(),false);
		}

	@Test
	public void testAddBalanceOperation() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewOrder() throws SQLException {
		OrderClass order = new OrderClass(0, 0, null, 0, 0, null, 0);
		
		balanceOperationRepository.addNewOrder(order);

		order.setOrderId(1);
	
		balanceOperationRepository.addNewOrder(order);
		balanceOperationRepository.addNewOrder(order);

		order.setOrderId(2);
		balanceOperationRepository.addNewOrder(order);
	}

	@Test
	public void testAddNewSale() throws SQLException {
		SaleTransactionClass sale = new SaleTransactionClass(null, 0, 0, null);
		
		balanceOperationRepository.addNewSale(sale);

		sale.setTicketNumber(1);
	
		balanceOperationRepository.addNewSale(sale);
		balanceOperationRepository.addNewSale(sale);

		sale.setTicketNumber(2);
		balanceOperationRepository.addNewSale(sale);
		}

	@Test
	public void testAddNewReturn() throws SQLException {
		ReturnTransactionClass retur = new ReturnTransactionClass(null, 0, null, null);
		
		balanceOperationRepository.addNewReturn(retur);

		retur.setReturnId(1);
	
		balanceOperationRepository.addNewReturn(retur);
		balanceOperationRepository.addNewReturn(retur);

		retur.setReturnId(2);
		balanceOperationRepository.addNewReturn(retur);
	}

	@Test
	public void testAddNewTicketEntry() throws SQLException {
		TicketEntryClass ticket = new TicketEntryClass(null, null, null, 0, 0, 0);
		
		balanceOperationRepository.addNewTicketEntry(ticket, null, null);
		
		ticket.setId(1);
		
		balanceOperationRepository.addNewTicketEntry(ticket, null, null);
		balanceOperationRepository.addNewTicketEntry(ticket, null, null);
	}
 
	@Test 
	public void testDeleteRow() throws SQLException {
		assertEquals(balanceOperationRepository.deleteRow(null, null, null),false);
		assertEquals(balanceOperationRepository.deleteRow("orderTable", null, null),false);
		assertEquals(balanceOperationRepository.deleteRow("orderTable", "orderId", null),false);
		assertEquals(balanceOperationRepository.deleteRow("orderTable", "orderId", "10"),false);
		Integer i = balanceOperationRepository.addNewOrder(new OrderClass(0, 0, null, 0, 0, null, 0));
		assertEquals(balanceOperationRepository.deleteRow("orderTable", "orderId", i.toString()),true);
	 }

	@Test 
	public void testUpdateRow() throws SQLException {
		assertEquals(balanceOperationRepository.updateRow(null, null, null, null, null),false);
		assertEquals(balanceOperationRepository.updateRow("orderTable", null, null, null, null),false);
		assertEquals(balanceOperationRepository.updateRow("orderTable", "productCode", null, null, null),false);
		assertEquals(balanceOperationRepository.updateRow("orderTable", "productCode", "orderId", null, null),false);
		assertEquals(balanceOperationRepository.updateRow("orderTable", "productCode", "orderId", 12, null),false);
		assertEquals(balanceOperationRepository.updateRow("orderTable", "productCode", "orderId", 12, "18377125254"),false);
		Integer i = balanceOperationRepository.addNewOrder(new OrderClass(0, 0, null, 0, 0, null, 0));
		assertEquals(balanceOperationRepository.updateRow("orderTable", "productCode", "orderId", i, "18377125254"),true);
		assertEquals(balanceOperationRepository.updateRow("orderTable", "productCode", "orderId", 78, "18377125254"),false);
		assertEquals(balanceOperationRepository.updateRow("orderTable", "productCode", "otherString", i, "18377125254"),false);
		assertEquals(balanceOperationRepository.updateRow("otherString", "productCode", "orderId", i, "18377125254"),false);
		assertEquals(balanceOperationRepository.updateRow("orderTable", "otherString", "orderId", i, "18377125254"),false);
	}

	@Test
	public void testUpdateState() throws SQLException {
		Integer o = balanceOperationRepository.addNewOrder(new OrderClass(0, 0, null, 0, 0, null, 0));
		Integer s = balanceOperationRepository.addNewSale(new SaleTransactionClass(0, 0, 0, null));
		assertEquals(balanceOperationRepository.updateState(null, null, null),false);
		assertEquals(balanceOperationRepository.updateState("orderTable", o, null),true);
		assertEquals(balanceOperationRepository.updateState("orderTable", o, "newState"),true);
		assertEquals(balanceOperationRepository.updateState("orderTable", 76, "newState"),false);
		assertEquals(balanceOperationRepository.updateState("sale", s, "newState"),false);
	}

	@Test
	public void testConvertResultSetOrderToDomainModel() {
		fail("Not yet implemented");
	}

	@Test
	public void testConvertResultSetSaleToDomainModel() {
		fail("Not yet implemented");
	}

	@Test
	public void testConvertResultSetReturnToDomainModel() {
		fail("Not yet implemented");
	}

	@Test
	public void testConvertResultSetTicketToDomainModel() {
		fail("Not yet implemented");
	}

	@Test
	public void testConvertResultSetBalanceToDomainModel() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllOrders() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllBalanceOperation() {
		fail("Not yet implemented");
	}

	@Test //TODO
	public void testGetTicketsBySaleId() throws SQLException {
		TicketEntryClass t1 = new TicketEntryClass(1,null , "prodotto1", 0, 0, 0);
		TicketEntryClass t2 = new TicketEntryClass(1,null , "prodotto2", 0, 0, 0);
		TicketEntryClass t3 = new TicketEntryClass(1, null , "prodotto3", 0, 0, 0);
		ArrayList<String> s1 = new ArrayList<>();
		ArrayList<String> s2 = new ArrayList<>();
		balanceOperationRepository.addNewTicketEntry(t1, 10, null);
		balanceOperationRepository.addNewTicketEntry(t2, 11, null);
		balanceOperationRepository.addNewTicketEntry(t3, 11, null);

		balanceOperationRepository.getTicketsBySaleId(10).forEach(k -> s1.add(k.getProductDescription()));
		assertEquals(s1,new ArrayList<String>(Arrays.asList(t1.getProductDescription())));
		balanceOperationRepository.getTicketsBySaleId(11).forEach(k -> s2.add(k.getProductDescription()));
		assertEquals(s2,new ArrayList<String>(Arrays.asList(t2.getProductDescription(),t3.getProductDescription())));


	}

	@Test // TODO
	public void testGetSalesByTicketNumber() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetReturnByReturnId() throws SQLException {
		ReturnTransactionClass O1 = new ReturnTransactionClass(null, 0, null, 0);
		ReturnTransactionClass O2 = new ReturnTransactionClass(null, 0, null, null);
		Integer o1 = balanceOperationRepository.addNewReturn(O1);
		Integer o2 = balanceOperationRepository.addNewReturn(O2);
		assertEquals(balanceOperationRepository.getReturnByReturnId(o1).getReturnId(),o1);
		assertEquals(balanceOperationRepository.getReturnByReturnId(o2).getReturnId(),o2);
		assertEquals(balanceOperationRepository.getReturnByReturnId(13),null);

		}

	@Test
	public void testGetTicketsByReturnId() throws SQLException {
		String s1 = "prodotto1";
		String s2 = "prodotto1";
		String s3 = "prodotto1";

		balanceOperationRepository.addNewTicketEntry(new TicketEntryClass(null, null, s1, 0, 0, 0), null, 1);
		balanceOperationRepository.addNewTicketEntry(new TicketEntryClass(null, null, s2, 0, 0, 0), null, 2);
		balanceOperationRepository.addNewTicketEntry(new TicketEntryClass(null, null, s3, 0, 0, 0), null, 2);

		assertEquals(balanceOperationRepository.getTicketsByReturnId(1).get(0).getProductDescription(), s1);
		assertEquals(balanceOperationRepository.getTicketsByReturnId(2).get(0).getProductDescription(), s2);
		assertEquals(balanceOperationRepository.getTicketsByReturnId(2).get(1).getProductDescription(), s3);


	}

	@Test
	public void testGetTicketsByForeignKeyAndBarcode() {

	}

	@Test
	public void testGetHighestTicketNumber() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetHighestOrderId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetHighestReturnId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetHighestBalanceId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOrderByOrderId() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteTables() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCreditCards() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBalanceOfACreditCard() {
		fail("Not yet implemented");
	}

	@Test
	public void testChangeCreditCardBalance2() {
		fail("Not yet implemented");
	}

	@Test
	public void testChangeCreditCardBalance() {
		fail("Not yet implemented");
	}

}
