package it.polito.ezshop.data.repository;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
		OrderClass order = new OrderClass(1, 0, null, 0, 0, null, 0);
		balanceOperationRepository.addNewOrder(order);
	
		assertThrows(SQLException.class, ()-> balanceOperationRepository.addNewOrder(order));
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

	@Test // "orderTable" "orderId" "1" TO FINISH
	public void testDeleteRow() throws SQLException {
		assertEquals(balanceOperationRepository.deleteRow(null, null, null),false);
		assertEquals(balanceOperationRepository.deleteRow("orderTable", null, null),false);
		assertEquals(balanceOperationRepository.deleteRow("orderTable", "orderId", null),false);
		assertEquals(balanceOperationRepository.deleteRow("orderTable", "orderId", "1"),false);
		balanceOperationRepository.addNewOrder(new OrderClass(1, 0, null, 0, 0, null, 0));
		assertEquals(balanceOperationRepository.deleteRow("orderTable", "orderId", "1"),false);

	 }

	@Test
	public void testUpdateRow() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateState() {
		fail("Not yet implemented");
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

	@Test
	public void testGetTicketsBySaleId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSalesByTicketNumber() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetReturnByReturnId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTicketsByReturnId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTicketsByForeignKeyAndBarcode() {
		fail("Not yet implemented");
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
