package it.polito.ezshop.data.repository;


import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.model.*;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BalanceOperationRepository {
    private static final String COLUMNS_ORDER = "orderId, balanceId, productCode, pricePerUnit, quantity, status, money";
    private static final String COLUMNS_SALE = "ticketNumber, discountRate, price, status";
    private static final String COLUMNS_RETURN = "returnId, price, status, ticketNumber";
    private static final String COLUMNS_TICKET_ENTRY = "id, barcode, productDescription, amount, pricePerUnit, discountRate, saleId, returnId";
    private static final String CLUMNS_BALANCE_OPERATION = "balanceId, localDate, money, type";
    private static final BalanceOperationRepository ourInstance = new BalanceOperationRepository();
    private static double balance; // TODO : ADD TO DB NOT HERE
    private static Integer nextTicketNumber = 0;
    private static Integer nextReturnId = 0;
    private BalanceOperationRepository() {
    }

    public static BalanceOperationRepository getInstance() {
        return ourInstance;
    }

    private static ArrayList<String> getAttrsBalOp() {
        ArrayList<String> attrs = new ArrayList<>(
                Arrays.asList("balanceId", "localDate", "money"
                        , "type"));
        return attrs;
    }

    private static ArrayList<String> getAttrsOrder() {
        ArrayList<String> attrs = new ArrayList<>(
                Arrays.asList("orderId", "balanceId", "productCode"
                        , "pricePerUnit", "quantity", "status", "money"));
        return attrs;
    }

    private static ArrayList<String> getAttrsSale() {
        ArrayList<String> attrs = new ArrayList<>(
                Arrays.asList("ticketNumber", "discountRate", "price", "status"));
        return attrs;
    }

    private static ArrayList<String> getAttrsReturn() {
        ArrayList<String> attrs = new ArrayList<>(
                Arrays.asList("returnId", "price", "status", "ticketNumber"));
        return attrs;
    }

    private static ArrayList<String> getAttrsTicket() {
        ArrayList<String> attrs = new ArrayList<>(
                Arrays.asList("barcode", "productDescription", "amount", "pricePerUnit", "discountRate", "saleId", "returnId"));

        return attrs;
    }

    protected static String getBalanceStatement() {
        return "SELECT " + "balance" +
                " FROM balanceTable" +
                " WHERE id = ?";
    }

    private static String insertCommand(String tableName, ArrayList<String> attributes) {
        String sqlCommand = "INSERT INTO " + tableName + "(";
        for (String attr : attributes)
            sqlCommand += attr + ",";
        sqlCommand = sqlCommand.substring(0, sqlCommand.length() - 1);
        sqlCommand += ") VALUES(";
        for (int i = 0; i < attributes.size(); i++)
            sqlCommand += "?,";
        sqlCommand = sqlCommand.substring(0, sqlCommand.length() - 1);
        sqlCommand += ");";
        return sqlCommand;
    }

    private static String getFindBySaleIdStatement() {
        return "SELECT * FROM ticket WHERE saleId = ?";
    }

    private static String getFindByReturnIdStatement() {
        return "SELECT * FROM ticket WHERE returnId = ?";
    }

    private static String getFindByForeignKeyAndBarcodeStatement(String foreignKey) {
        return "SELECT * FROM ticket WHERE " + foreignKey + " = ? AND barcode = ?";
    }

    private static String getFindByTicketNumberStatement() {
        return "SELECT * FROM sale WHERE ticketNumber = ?";
    }

    private static String getFindByReturnIdFromReturnTableStatement() {
        return "SELECT * FROM returnTable WHERE returnId = ?";
    }

    private static String getDeleteRowStatement(String tableName, String columnName, String columnName2) {
        return "DELETE FROM " + tableName + " WHERE " + columnName + "= ? AND " + columnName2 + "= ?;";
    }

    public static Double getBalanceOfACreditCard(String creditCard) throws IOException {
        String filePath = new File("").getAbsolutePath();
        filePath = filePath.concat("\\src\\main\\java\\it\\polito\\ezshop\\utils\\CreditCards.txt");

        File file = new File(filePath);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;

        int n = 0;
        while ((st = br.readLine()) != null) {
            if (creditCard.equals(st.substring(0, 16))) {
                return Double.parseDouble(st.substring(17, st.length() - 1));
            }
            n = n + 1;
        }
        return 0.0;
    }

    public void initialize() throws SQLException {
        Connection con = DBCPDBConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "orderTable" + " " + "(orderId INTEGER PRIMARY KEY, balanceId INTEGER, productCode TEXT, pricePerUnit DOUBLE, quantity INTEGER, status TEXT, money DOUBLE)");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "sale" + " " + "(ticketNumber INTEGER PRIMARY KEY, discountRate DOUBLE, price DOUBLE, status TEXT)");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "returnTable" + " " + "(returnId INTEGER PRIMARY KEY, price DOUBLE, status TEXT, ticketNumber INTEGER, FOREIGN KEY (ticketNumber) references sale(ticketNumber))");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "ticket" + " " + "(id INTEGER PRIMARY KEY AUTOINCREMENT, barcode TEXT, productDescription TEXT, amount INTEGER , pricePerUnit DOUBLE, discountRate DOUBLE, saleId INTEGER, returnId INTEGER, FOREIGN KEY (saleId) references sale(ticketNumber), FOREIGN KEY (returnId) references returnTable(returnId))");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "balanceTable" + " " + "(id INTEGER PRIMARY KEY , balance DOUBLE )");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "balanceOperationTable" + " " + "(balanceId INTEGER PRIMARY KEY , localDate DATE , money DOUBLE , type TEXT)");
        try {
            this.getBalance();
        } catch (SQLException e) {
            this.insertBalance();
        }
        st.close();
        con.close();
    }

    public boolean setBalance(double b) {
        Connection con = null;
    	try {
            con = DBCPDBConnectionPool.getConnection();
            String sqlCommand = "UPDATE balanceTable SET balance = balance + ? WHERE id = ?";
            PreparedStatement prp = con.prepareStatement(sqlCommand);
            prp.setString(1, Double.toString(b));
            prp.setString(2, "1");
            prp.executeUpdate();
            prp.close();
            con.close();
            return true;
        } catch (SQLException e) {
            try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }
        return false;

    }


    // barcode, productDescription, amount, pricePerUnit, discountRate,    saleId, returnId

    public boolean resetBalance() {
        Connection con = null;
    	try {
            con = DBCPDBConnectionPool.getConnection();
            String sqlCommand = "UPDATE balanceTable SET balance = ?  WHERE id = ?";
            PreparedStatement prp = con.prepareStatement(sqlCommand);
            prp.setString(1, "0");
            prp.setString(2, "1");
            prp.executeUpdate();
            prp.close();
            con.close();
            return true;
        } catch (SQLException e) {
            try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }
        return false;

    }

    public double getBalance() throws SQLException {

        Connection con = DBCPDBConnectionPool.getConnection();
        String sqlCommand = getBalanceStatement();
        PreparedStatement prp = con.prepareStatement(sqlCommand);
        prp.setString(1, "1");
        ResultSet rs = prp.executeQuery();
        rs.next();
        double b = rs.getDouble(1);
        prp.close();
        con.close();
        return b;

    }

    public boolean insertBalance() {
        Connection con = null;
    	try {
            con = DBCPDBConnectionPool.getConnection();
            String sqlCommand = insertCommand("balanceTable", new ArrayList<String>(Arrays.asList("id", "balance")));
            PreparedStatement prp = con.prepareStatement(sqlCommand);
            prp.setString(1, "1");
            prp.setString(2, "0");
            prp.executeUpdate();
            prp.close();
            con.close();
            return true;
        } catch (SQLException e) {
            try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }
    	return false;
    }

    public void addBalanceOperation(BalanceOperationClass b) throws SQLException {

        HashMap<String, String> balanceData = new HashMap<>();
        balanceData.put("balanceId", String.valueOf(b.getBalanceId()));
        balanceData.put("localDate", b.getDate().toString());
        balanceData.put("money", String.valueOf(b.getMoney()));
        balanceData.put("type", b.getType());

        Connection con = DBCPDBConnectionPool.getConnection();
        ArrayList<String> attrs = getAttrsBalOp();
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "adding new balance operation: " + b.getBalanceId());
        String sqlCommand = insertCommand("balanceOperationTable", attrs);
        PreparedStatement prp = con.prepareStatement(sqlCommand);
        for (int j = 0; j < attrs.size(); j++) {
            prp.setString(j + 1, balanceData.get(attrs.get(j)));
        }

        prp.executeUpdate();
        prp.close();
        con.close();
    }

    public Integer addNewOrder(OrderClass order) throws SQLException {
    	Integer nextOrderId = this.getHighestOrderId() +1;
        HashMap<String, String> orderData = new HashMap<>();
        orderData.put("orderId", String.valueOf(nextOrderId));
        orderData.put("balanceId", order.getBalanceId().toString());
        orderData.put("productCode", order.getProductCode());
        orderData.put("pricePerUnit", Double.toString(order.getPricePerUnit()));
        orderData.put("quantity", String.valueOf(order.getQuantity()));
        orderData.put("status", order.getStatus());
        orderData.put("money", String.valueOf(order.getMoney()));

        Connection con = DBCPDBConnectionPool.getConnection();
        ArrayList<String> attrs = getAttrsOrder();
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "adding new order: " + order.getOrderId());
        String sqlCommand = insertCommand("orderTable", attrs);
        PreparedStatement prp = con.prepareStatement(sqlCommand);
        for (int j = 0; j < attrs.size(); j++) {
            prp.setString(j + 1, orderData.get(attrs.get(j)));
        }

        prp.executeUpdate();
        prp.close();
        con.close();
        return this.getHighestOrderId();
    }

    public Integer addNewSale(SaleTransactionClass sale) throws SQLException {

        nextTicketNumber = ourInstance.getHighestTicketNumber() + 1;
        HashMap<String, String> saleData = new HashMap<>();
        saleData.put("ticketNumber", String.valueOf(nextTicketNumber));
        saleData.put("discountRate", String.valueOf(sale.getDiscountRate()));
        saleData.put("price", String.valueOf(sale.getPrice()));
        saleData.put("status", sale.getState());

        Connection con = DBCPDBConnectionPool.getConnection();
        ArrayList<String> attrs = getAttrsSale();
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "adding new sale with saleId: " + nextTicketNumber.toString());
        String sqlCommand = insertCommand("sale", attrs);
        PreparedStatement prp = con.prepareStatement(sqlCommand);
        for (int j = 0; j < attrs.size(); j++) {
            prp.setString(j + 1, saleData.get(attrs.get(j)));
        }
        prp.executeUpdate();
        prp.close();
        con.close();
        return nextTicketNumber;
    }

    public Integer addNewReturn(ReturnTransactionClass returnTransaction) throws SQLException {
        nextReturnId = getHighestReturnId() + 1;
        HashMap<String, String> returnData = new HashMap<>();
        returnData.put("returnId", String.valueOf(nextReturnId));
        returnData.put("price", Double.toString(returnTransaction.getPrice()));
        returnData.put("status", returnTransaction.getState());
        returnData.put("ticketNumber", String.valueOf(returnTransaction.getTicketNumber()));

        Connection con = DBCPDBConnectionPool.getConnection();
        ArrayList<String> attrs = getAttrsReturn();
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "adding new return with returnId: " + nextReturnId);
        String sqlCommand = insertCommand("returnTable", attrs);
        PreparedStatement prp = con.prepareStatement(sqlCommand);
        for (int j = 0; j < attrs.size(); j++) {
            prp.setString(j + 1, returnData.get(attrs.get(j)));
        }
        prp.executeUpdate();
        prp.close();
        con.close();
        return nextReturnId;
    }

    public void addNewTicketEntry(TicketEntryClass ticket, Integer saleId, Integer returnId) throws SQLException {

        HashMap<String, String> ticketData = new HashMap<>();
        //  ticketData.put("balanceId", ticket.getBalanceId().toString());
        ticketData.put("barcode", ticket.getBarCode());
        ticketData.put("productDescription", ticket.getProductDescription());
        ticketData.put("amount", String.valueOf(ticket.getAmount()));
        ticketData.put("pricePerUnit", String.valueOf(ticket.getPricePerUnit()));
        ticketData.put("discountRate", String.valueOf(ticket.getDiscountRate()));
        ticketData.put("saleId", String.valueOf(saleId));
        ticketData.put("returnId", String.valueOf(returnId));

        Connection con = DBCPDBConnectionPool.getConnection();
        ArrayList<String> attrs = getAttrsTicket();
        System.out.println("adding new ticket");
        String sqlCommand = insertCommand("ticket", attrs);
        PreparedStatement prp = con.prepareStatement(sqlCommand);
        for (int j = 0; j < attrs.size(); j++) {
            prp.setString(j + 1, ticketData.get(attrs.get(j)));
        }
        prp.executeUpdate();
        prp.close();
        con.close();
    }

    public boolean deleteRow(String tableName, String idName, String id) {
    	 PreparedStatement prps = null;
    	 Connection con = null;
        try {
            String sqlCommand = getDeleteRowStatement(tableName, idName);
            con = DBCPDBConnectionPool.getConnection();
            Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "deleting row with id: " + id);
            prps = con.prepareStatement(sqlCommand);
            prps.setString(1, id);
            int returnVal = prps.executeUpdate();
            prps.close();
            con.close();
            return (returnVal == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            return false;
        }
    }

    /**
     * @param tableName    the name of the table to be updated
     * @param columnName   the name of the column which is going to change
     * @param idName       the name of the column which is going to be used in where clause
     * @param id           the value of the id
     * @param newColumnVal the value of the column which is being updated
     **/
    public boolean updateRow(String tableName, String columnName, String idName, Integer id, String newColumnVal) {
    	PreparedStatement prps = null;
    	Connection con = null;
    	try {
            String sqlCommand = getUpdateRowStatement(tableName, columnName, idName);
            con = DBCPDBConnectionPool.getConnection();
            prps = con.prepareStatement(sqlCommand);
            prps.setString(1, newColumnVal);
            prps.setString(2, String.valueOf(id));
            int returnVal = prps.executeUpdate();
            prps.close();
            con.close();
            return (returnVal == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        return false;
    }

    public boolean updateState(String tableName, Integer id, String state) {
    	PreparedStatement prps = null;
    	Connection con = null;
    	try {
            String sqlCommand = getUpdateStateStatement(tableName);
            con = DBCPDBConnectionPool.getConnection();
            prps = con.prepareStatement(sqlCommand);
            prps.setString(1, String.valueOf(state));
            prps.setString(2, String.valueOf(id));
            int returnVal = prps.executeUpdate();
            prps.close();
            con.close();
            return (returnVal == 1);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        return false;
    }

    private String getUpdateQuantityStatement() {
        return "UPDATE ticket SET amount = ? WHERE id = ?";
    }

    private String getUpdateStateStatement(String tableName) {
        return "UPDATE " + tableName + " SET status  = ? WHERE orderId = ?";
    }

    private String getDeleteTicketStatement() {
        return "DELETE FROM ticket WHERE id= ?;";
    }

    private String getUpdateRowStatement(String tableName, String columnName, String idName) {
        return "UPDATE " + tableName + " SET " + columnName + " = ? WHERE " + idName + " = ?";
    }

    private String getDeleteRowStatement(String tableName, String idName) {
        return "DELETE FROM " + tableName + " WHERE " + idName + "= ?;";
    }

    protected OrderClass convertResultSetOrderToDomainModel(ResultSet rs) throws SQLException {
        return new OrderClass(rs.getInt(1),
                rs.getInt(2),
                rs.getString(3),
                rs.getDouble(4),
                rs.getInt(5),
                rs.getString(6),
                rs.getDouble(7)
        );
    }

    // ticketNumber, discountRate, price, state";
    protected SaleTransactionClass convertResultSetSaleToDomainModel(ResultSet rs) throws SQLException {
        return new SaleTransactionClass(rs.getInt(1),
                rs.getDouble(2),
                rs.getDouble(3),
                rs.getString(4)
        );
    }

    //  "balanceId, price, state";
    protected ReturnTransactionClass convertResultSetReturnToDomainModel(ResultSet rs) throws SQLException {
        return new ReturnTransactionClass(rs.getInt(1),
                rs.getDouble(2),
                rs.getString(3),
                rs.getInt(4)
        );
    }

    // barcode, productDescription, amount, pricePerUnit, discountRate, saleId, returnId";
    protected TicketEntryClass convertResultSetTicketToDomainModel(ResultSet rs) throws SQLException {
        return new TicketEntryClass(rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getInt(4),
                rs.getDouble(5),
                rs.getDouble(6)
        );
    }

    protected BalanceOperationClass convertResultSetBalanceToDomainModel(ResultSet rs) throws SQLException {
        return new BalanceOperationClass(rs.getInt(1),
                LocalDate.of(Integer.valueOf(rs.getString(2).split("-")[0]), Integer.valueOf(rs.getString(2).split("-")[1]), Integer.valueOf(rs.getString(2).split("-")[2])),
                rs.getDouble(3),
                rs.getString(4)
        );
    }

    private ArrayList<OrderClass> loadAllOrders(ResultSet rs) throws SQLException {

        ArrayList<OrderClass> result = new ArrayList<>();
        while (rs.next()) {
            OrderClass o = convertResultSetOrderToDomainModel(rs);
            result.add(o);
        }
        return result;
    }

    private ArrayList<SaleTransactionClass> loadAllSales(ResultSet rs) throws SQLException {

        ArrayList<SaleTransactionClass> result = new ArrayList<>();
        while (rs.next()) {
            SaleTransactionClass s = convertResultSetSaleToDomainModel(rs);
            result.add(s);
        }
        return result;
    }

    private ArrayList<ReturnTransactionClass> loadAllReturns(ResultSet rs) throws SQLException {

        ArrayList<ReturnTransactionClass> result = new ArrayList<>();
        while (rs.next()) {
            ReturnTransactionClass r = convertResultSetReturnToDomainModel(rs);
            result.add(r);
        }
        return result;
    }

    private ArrayList<TicketEntry> loadAllTickets(ResultSet rs) throws SQLException {

        ArrayList<TicketEntry> result = new ArrayList<>();
        while (rs.next()) {
            TicketEntryClass t = convertResultSetTicketToDomainModel(rs);
            result.add(t);
        }
        return result;
    }

    private ArrayList<BalanceOperationClass> loadAllBalanceOperation(ResultSet rs) throws SQLException {

        ArrayList<BalanceOperationClass> result = new ArrayList<>();
        while (rs.next()) {
            BalanceOperationClass t = convertResultSetBalanceToDomainModel(rs);
            result.add(t);
        }
        return result;
    }

    public ArrayList<OrderClass> getAllOrders() {
    	Connection con = null;
    	try {
            String sqlCommand = geAllTransStatement("orderTable");
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            ResultSet rs = prps.executeQuery();
            ArrayList<OrderClass> orders = loadAllOrders(rs);
            prps.close();
            con.close();
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }
        return null;
    }

    public ArrayList<BalanceOperationClass> getAllBalanceOperation() {
    	Connection con = null;
    	try {
            String sqlCommand = geAllTransStatement("balanceOperationTable");
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            ResultSet rs = prps.executeQuery();
            ArrayList<BalanceOperationClass> balances = loadAllBalanceOperation(rs);
            prps.close();
            con.close();
            return balances;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }
        return null;
    }

    public ArrayList<TicketEntry> getTicketsBySaleId(Integer saleId) {
    	Connection con = null;
    	try {
            String sqlCommand = getFindBySaleIdStatement();
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, String.valueOf(saleId));
            ResultSet rs = prps.executeQuery();
            ArrayList<TicketEntry> tickets = loadAllTickets(rs);
            prps.close();
            con.close();
            return tickets;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }
        return null;
    }

    public SaleTransactionClass getSalesByTicketNumber(Integer ticketNumber) {
    	Connection con = null;
    	try {
            String sqlCommand = getFindByTicketNumberStatement();
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, String.valueOf(ticketNumber));
            ResultSet rs = prps.executeQuery();
            rs.next();
            SaleTransactionClass s = convertResultSetSaleToDomainModel(rs);
            prps.close();
            con.close();
            s.setEntries(getTicketsBySaleId(ticketNumber));
            return s;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }
        return null;
    }

    public ReturnTransactionClass getReturnByReturnId(Integer returnId) {
    	Connection con = null;
    	try {
            String sqlCommand = getFindByReturnIdFromReturnTableStatement();
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, String.valueOf(returnId));
            ResultSet rs = prps.executeQuery();
            rs.next();
            ReturnTransactionClass r = convertResultSetReturnToDomainModel(rs);
            prps.close();
            con.close();
            r.setEntries(getTicketsByReturnId(returnId));
            return r;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }
        return null;
    }

    public ArrayList<TicketEntry> getTicketsByReturnId(Integer returnId) {
    	Connection con = null;
    	try {
            String sqlCommand = getFindByReturnIdStatement();
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, String.valueOf(returnId));
            ResultSet rs = prps.executeQuery();
            ArrayList<TicketEntry> tickets = loadAllTickets(rs);
            prps.close();
            con.close();
            return tickets;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        return null;
    }

    public TicketEntryClass getTicketsByForeignKeyAndBarcode(String foreignKey, Integer key, String barcode) {
    	Connection con = null;

    	try {
            String sqlCommand = getFindByForeignKeyAndBarcodeStatement(foreignKey);
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, String.valueOf(key));
            prps.setString(2, String.valueOf(barcode));
            ResultSet rs = prps.executeQuery();
            rs.next();
            TicketEntryClass ticket = convertResultSetTicketToDomainModel(rs);
            prps.close();
            con.close();
            return ticket;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }
        return null;
    }

    public Integer getHighestTicketNumber() {
    	Connection con = null;

        try {
            String sqlCommand = getMaxTicketNumberStatement();
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            ResultSet rs = prps.executeQuery();
            rs.next();
            Integer highestId = rs.getInt(1);
            prps.close();
            con.close();
            return highestId;
           
        } catch (SQLException e) {
            e.printStackTrace();
            try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }
        return null;
    }

    public Integer getHighestTicketNumberInTicket() {
    	Connection con = null;;
    	try {
            String sqlCommand = getMaxTicketNumberInTicketStatement();
            con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            ResultSet rs = prps.executeQuery();
            rs.next();
            Integer highestId = rs.getInt(1);
            prps.close();
            con.close();
            return highestId;
            
        } catch (SQLException e) {
            e.printStackTrace();
            try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }
        return null;
    }
    
    public Integer getHighestOrderId() throws SQLException {

        String sqlCommand = getMaxOrderIdStatement();
        Connection con = DBCPDBConnectionPool.getConnection();
        PreparedStatement prps = con.prepareStatement(sqlCommand);
        ResultSet rs = prps.executeQuery();
        rs.next();
        Integer highestId = rs.getInt(1);
        prps.close();
        con.close();
        return highestId;
        
    }

    public Integer getHighestReturnId() {
    	Connection con = null;
    	try {
            String sqlCommand = getMaxReturnIdStatement();
             con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            ResultSet rs = prps.executeQuery();
            rs.next();
            Integer highestId = rs.getInt(1);
            prps.close();
            con.close();
            return highestId;
           
        } catch (SQLException e) {
            e.printStackTrace();
            try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }
        return null;

    }

    public Integer getHighestBalanceId() throws SQLException {

        String sqlCommand = getMaxBalanceIdStatement();
        Connection con = DBCPDBConnectionPool.getConnection();
        PreparedStatement prps = con.prepareStatement(sqlCommand);
        ResultSet rs = prps.executeQuery();
        rs.next();
        Integer highestId = rs.getInt(1);
        prps.close();
        con.close();
        return highestId;
      
    }

    public OrderClass getOrderByOrderId(String orderId) throws SQLException {

        String sqlCommand = getOrderByOrderIdStatement();
        Connection con = DBCPDBConnectionPool.getConnection();
        PreparedStatement prps = con.prepareStatement(sqlCommand);
        prps.setString(1, orderId);
        ResultSet rs = prps.executeQuery();
        rs.next();
        OrderClass o = this.convertResultSetOrderToDomainModel(rs);
        prps.close();
        con.close();
        return o;
    }

    public void deleteTables() throws SQLException {
        Logger.getLogger(EZShop.class.getName()).log(Level.INFO, "deleting transactions");
        Connection con = DBCPDBConnectionPool.getConnection();
        PreparedStatement prp = con.prepareStatement("DELETE FROM orderTable;");
        prp.executeUpdate();
        prp = con.prepareStatement("DELETE FROM sale;");
        prp.executeUpdate();
        prp = con.prepareStatement("DELETE FROM returnTable;");
        prp.executeUpdate();
        prp = con.prepareStatement("DELETE FROM ticket;");
        prp.executeUpdate();
        prp = con.prepareStatement("DELETE FROM balanceOperationTable;");
        prp.executeUpdate();
        prp.close();
        con.close();
    }

    private String getOrderByOrderIdStatement() {
        return "SELECT " + COLUMNS_ORDER +
                " FROM orderTable" +
                " WHERE  orderId= ?";
    }

    private String getMaxOrderIdStatement() {
        return "SELECT MAX(orderId) FROM orderTable";
    }

    private String getMaxBalanceIdStatement() {
        return "SELECT MAX(balanceId) FROM balanceOperationTable";
    }

    private String geAllTransStatement(String tableName) {

        String sqlCommand = "SELECT * FROM " + tableName;
        return sqlCommand;
    }

    private String getMaxTicketNumberStatement() {
        String sqlCommand = "SELECT MAX(ticketNumber) FROM sale";
        return sqlCommand;
    }
    private String getMaxTicketNumberInTicketStatement() {
        String sqlCommand = "SELECT MAX(ticketNumber) FROM ticket";
        return sqlCommand;
    }
    private String getMaxReturnIdStatement() {
        String sqlCommand = "SELECT MAX(returnId) FROM returnTable";
        return sqlCommand;
    }

    public HashMap<String, Double> getCreditCards() throws IOException {
        String filePath = new File("").getAbsolutePath();
        filePath = filePath.concat("\\src\\main\\java\\it\\polito\\ezshop\\utils\\CreditCards.txt");

        File file = new File(filePath);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;

        //4716258050958645;0.00

        HashMap<String, Double> creditCards = new HashMap<String, Double>();
        int n = 0;
        while ((st = br.readLine()) != null) {
            if (n > 4) {
                creditCards.put(st.substring(0, 16), Double.parseDouble(st.substring(17, st.length() - 1)));
            }
            n = n + 1;
        } 
        return creditCards;
    }

    public Boolean changeCreditCardBalance2(String creditCard, Double price) {
        try {
            String filePath = new File("").getAbsolutePath();
            filePath = filePath.concat("\\src\\main\\java\\it\\polito\\ezshop\\utils\\CreditCards.txt");
            File file = new File(filePath);
            BufferedWriter outStream = new BufferedWriter(new FileWriter(file, true));


            price = price + getBalanceOfACreditCard(creditCard);
            outStream.newLine();
            outStream.write(creditCard + ";" + price);
            outStream.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }
 

    public void changeCreditCardBalance(String creditCard, Double price) {
        try {
            // input the file content to the StringBuffer "input"
            String filePath = new File("").getAbsolutePath();
            filePath = filePath.concat("\\src\\main\\java\\it\\polito\\ezshop\\utils\\CreditCards.txt");
            BufferedReader file = new BufferedReader(new FileReader(filePath));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            file.close();
            String inputStr = inputBuffer.toString();

            String[] lines = inputStr.split("\r\n|\n|\r");

            System.out.println(inputStr); // display the original file for debugging
            for (int i = 1; i < lines.length; i++) {
                System.out.println(lines[i]);
                if (lines[i].contains(creditCard)) {
                    inputStr = inputStr.replace(lines[i], creditCard + ";" + price.toString());
                }
            }


            // display the new file for debugging
            System.out.println("----------------------------------\n" + inputStr);

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(filePath);
            fileOut.write(inputStr.getBytes());
            fileOut.close();

        } catch (Exception e) {
            System.out.println("Problem reading file.");
        }
    }

}
