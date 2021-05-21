package it.polito.ezshop.data.repository;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BalanceOperationRepositoryTest {
	static BalanceOperationRepository balanceOperationRepository;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		balanceOperationRepository = BalanceOperationRepository.getInstance();
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
	public void testSetBalance()  {
		double balanceUpdate = 10;
		double balanceNow;
		try {
		balanceNow = balanceOperationRepository.getBalance();
	
		
		balanceOperationRepository.setBalance(balanceUpdate);
		assertEquals(balanceOperationRepository.getBalance(), balanceNow + balanceUpdate , 0.001);
		
		balanceUpdate = -10;
		balanceNow = balanceOperationRepository.getBalance();
		
		balanceOperationRepository.setBalance(balanceUpdate);
		assertEquals(balanceOperationRepository.getBalance(), balanceNow + balanceUpdate , 0.001);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetBalanceStatement() {
		fail("Not yet implemented");
	}

	@Test
	public void testResetBalance() {
		double balance = 0;
		balanceOperationRepository.resetBalance();
		try {
			assertEquals(balanceOperationRepository.getBalance(), balance , 0.001);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
	public void testAddNewOrder() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewSale() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewReturn() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewTicketEntry() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteRow() {
		fail("Not yet implemented");
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
