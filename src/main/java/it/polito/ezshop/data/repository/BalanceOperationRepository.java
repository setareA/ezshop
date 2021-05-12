package it.polito.ezshop.data.repository;


import it.polito.ezshop.data.model.OrderClass;
import it.polito.ezshop.data.model.ReturnTransactionClass;
import it.polito.ezshop.data.model.SaleTransactionClass;
import it.polito.ezshop.data.model.TicketEntryClass;


import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BalanceOperationRepository {
    private static BalanceOperationRepository ourInstance = new BalanceOperationRepository();

    public static BalanceOperationRepository getInstance() {
        return ourInstance;
    }

    private BalanceOperationRepository() {
    }

    private static final String COLUMNS_ORDER = "orderId, balanceId, productCode, pricePerUnit, quantity, status, localDate, money";
    private static final String COLUMNS_SALE = "balanceId, localDate, money, type, ticketNumber, discountRate, price";
    private static final String COLUMNS_RETURN = "balanceId, localDate, money, type";
    private static final String COLUMNS_TICKET_ENTRY = "id, barcode, productDescription, amount, pricePerUnit, discountRate, saleId, returnId";

    public void initialize() throws SQLException {
        Connection con = DBCPDBConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "orderTable" + " " + "(balanceId INTEGER PRIMARY KEY, localDate DATE, money DOUBLE, type TEXT, productCode TEXT, pricePerUnit DOUBLE, quantity INTEGER, status TEXT, orderId INTEGER)");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "sale" + " " + "(balanceId INTEGER PRIMARY KEY, localDate DATE, money DOUBLE, type TEXT, ticketNumber INTEGER, discountRate DOUBLE, price DOUBLE)");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "return" + " " + "(balanceId INTEGER PRIMARY KEY, localDate DATE, money DOUBLE, type TEXT)");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + "ticket" + " " + "(INTEGER PRIMARY KEY AUTOINCREMENT, barcode TEXT, productDescription TEXT, amount INTEGER , pricePerUnit DOUBLE, discountRate DOUBLE, saleId INTEGER, returnId INTEGER, FOREIGN KEY (saleId) references sale(balanceId), FOREIGN KEY (returnId) references return(balanceId))");

        st.close();
        con.close();
    }

    private static ArrayList<String> getAttrsOrder(){
        ArrayList<String> attrs = new ArrayList<>(
                Arrays.asList("orderId", "balanceId", "productCode"
                        ,  "pricePerUnit", "quantity", "status", "localDate", "money"));
        return attrs;
    }
    private static ArrayList<String> getAttrsSale(){
        ArrayList<String> attrs = new ArrayList<>(
                Arrays.asList("balanceId", "localDate", "money", "type", "ticketNumber", "discountRate", "price"));
        return attrs;
    }
    private static ArrayList<String> getAttrsReturn(){
        ArrayList<String> attrs = new ArrayList<>(
                Arrays.asList( "balanceId", "localDate", "money", "type"));
        return attrs;
    }
    private static ArrayList<String> getAttrsTicket(){
        ArrayList<String> attrs = new ArrayList<>(
                Arrays.asList("barcode", "productDescription", "amount", "pricePerUnit", "discountRate", "saleId", "returnId"));

        return attrs;
    }

    private static String insertCommand(String tableName, ArrayList<String> attributes){
        String sqlCommand = "INSERT INTO " + tableName + "(";
        for(String attr: attributes)
            sqlCommand += attr + ",";
        sqlCommand = sqlCommand.substring(0, sqlCommand.length()-1);
        sqlCommand += ") VALUES(";
        for(int i = 0; i < attributes.size(); i++)
            sqlCommand += "?,";
        sqlCommand = sqlCommand.substring(0, sqlCommand.length()-1);
        sqlCommand += ");";
        return sqlCommand;
    }

    public void addNewOrder(OrderClass order) throws SQLException{

        HashMap<String, String> orderData = new HashMap<>();
        orderData.put("orderId", order.getOrderId().toString());
        orderData.put("balanceId", order.getBalanceId().toString() );
        orderData.put("productcode", order.getProductCode());
        orderData.put("PricePerUnit",  Double.toString(order.getPricePerUnit()));
        orderData.put("quantity",String.valueOf(order.getQuantity()));
        orderData.put("status", order.getStatus() );
        orderData.put("localDate",order.getLocalDate().toString());
        orderData.put("money", String.valueOf(order.getMoney()));


        Connection con = DBCPDBConnectionPool.getConnection();
        ArrayList<String> attrs = getAttrsOrder();
        System.out.println("adding new order");
        String sqlCommand = insertCommand("orderTable", attrs);
        PreparedStatement prp = con.prepareStatement(sqlCommand);
        for (int j = 0; j < attrs.size(); j++) {
                prp.setString(j + 1, orderData.get(attrs.get(j)));
        }

        prp.executeUpdate();
        prp.close();
        con.close();
    }

    public void addNewSale(SaleTransactionClass sale) throws SQLException{

        HashMap<String, String> saleData = new HashMap<>();
        saleData.put("balanceId", sale.getBalanceId().toString());
        saleData.put("localDate", sale.getDate().toString() );
        saleData.put("money", Double.toString(sale.getMoney()));
        saleData.put("type", sale.getType());
        saleData.put("ticketNumber", String.valueOf(sale.getTicketNumber()));
        saleData.put("discountRate", String.valueOf(sale.getDiscountRate()));
        saleData.put("price", String.valueOf(sale.getPrice()));


        Connection con = DBCPDBConnectionPool.getConnection();
        ArrayList<String> attrs = getAttrsSale();
        System.out.println("adding new sale");
        String sqlCommand = insertCommand("sale", attrs);
        PreparedStatement prp = con.prepareStatement(sqlCommand);
        for (int j = 0; j < attrs.size(); j++) {
            prp.setString(j + 1, saleData.get(attrs.get(j)));
        }
        prp.executeUpdate();
        prp.close();
        con.close();
    }

    public void addNewReturn(ReturnTransactionClass returnTransaction) throws SQLException{

        HashMap<String, String> returnData = new HashMap<>();
        returnData.put("balanceId", returnTransaction.getBalanceId().toString());
        returnData.put("localDate", returnTransaction.getDate().toString() );
        returnData.put("money", Double.toString(returnTransaction.getMoney()));
        returnData.put("type", returnTransaction.getType());

        Connection con = DBCPDBConnectionPool.getConnection();
        ArrayList<String> attrs = getAttrsReturn();
        System.out.println("adding new return");
        String sqlCommand = insertCommand("return", attrs);
        PreparedStatement prp = con.prepareStatement(sqlCommand);
        for (int j = 0; j < attrs.size(); j++) {
            prp.setString(j + 1, returnData.get(attrs.get(j)));
        }
        prp.executeUpdate();
        prp.close();
        con.close();
    }


   // barcode, productDescription, amount, pricePerUnit, discountRate,    saleId, returnId

    public void addNewTicketEntry(TicketEntryClass ticket, Integer saleId, Integer returnId) throws SQLException{

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

    protected OrderClass convertResultSetOrderToDomainModel(ResultSet rs) throws SQLException {
        return new OrderClass(rs.getInt(1),
                rs.getInt(2),
                rs.getString(3),
                rs.getDouble(4),
                rs.getInt(5),
                rs.getString(6),
                rs.getDate(7).toLocalDate(),
                rs.getInt(8)
        );
    }

    protected SaleTransactionClass convertResultSetSaleToDomainModel(ResultSet rs) throws SQLException {
        return new SaleTransactionClass(rs.getInt(1),
                rs.getDate(2).toLocalDate(),
                rs.getDouble(3),
                rs.getString(4),
                rs.getInt(5),
                rs.getDouble(6),
                rs.getInt(7)
        );
    }

  //  "balanceId, localDate, money, type";
    protected ReturnTransactionClass convertResultSetReturnToDomainModel(ResultSet rs) throws SQLException {
        return new ReturnTransactionClass(rs.getInt(1),
                rs.getDate(2).toLocalDate(),
                rs.getDouble(3),
                rs.getString(4)
        );
    }

    // barcode, productDescription, amount, pricePerUnit, discountRate, saleId, returnId";
    protected TicketEntryClass convertResultSetTicketToDomainModel(ResultSet rs) throws SQLException {
        return new TicketEntryClass(rs.getString(2),
        rs.getString(3),
                rs.getInt(4),
                rs.getDouble(5),
                rs.getDouble(6)
        );
    }


    private ArrayList<OrderClass> loadAllOrders(ResultSet rs) throws SQLException{

        ArrayList <OrderClass> result = new ArrayList<>();
        while(rs.next()) {
            OrderClass o = convertResultSetOrderToDomainModel(rs);
            result.add(o);
        }
        return result;
    }
    private ArrayList<SaleTransactionClass> loadAllSales(ResultSet rs) throws SQLException{

        ArrayList <SaleTransactionClass> result = new ArrayList<>();
        while(rs.next()) {
            SaleTransactionClass s = convertResultSetSaleToDomainModel(rs);
            result.add(s);
        }
        return result;
    }
    private ArrayList<ReturnTransactionClass> loadAllReturns(ResultSet rs) throws SQLException{

        ArrayList <ReturnTransactionClass> result = new ArrayList<>();
        while(rs.next()) {
            ReturnTransactionClass r = convertResultSetReturnToDomainModel(rs);
            result.add(r);
        }
        return result;
    }

    private ArrayList<TicketEntryClass> loadAllTickets(ResultSet rs) throws SQLException{

        ArrayList <TicketEntryClass> result = new ArrayList<>();
        while(rs.next()) {
            TicketEntryClass t = convertResultSetTicketToDomainModel(rs);
            result.add(t);
        }
        return result;
    }


    public ArrayList<OrderClass> getAllOrders(){
        try {
            String sqlCommand = geAllTransStatement("orderTable");
            Connection con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            ResultSet rs = prps.executeQuery();
            ArrayList<OrderClass> orders = loadAllOrders(rs);
            prps.close();
            con.close();
            return orders;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<SaleTransactionClass> getAllSales(){
        try {
            String sqlCommand = geAllTransStatement("sale");
            Connection con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            ResultSet rs = prps.executeQuery();
            ArrayList<SaleTransactionClass> sales = loadAllSales(rs);
            prps.close();
            con.close();
            return sales;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<ReturnTransactionClass> getAllReturns(){
        try {
            String sqlCommand = geAllTransStatement("return");
            Connection con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            ResultSet rs = prps.executeQuery();
            ArrayList<ReturnTransactionClass> returns = loadAllReturns(rs);
            prps.close();
            con.close();
            return returns;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<TicketEntryClass> getTicketsBySaleId(Integer saleId){
        try {
            String sqlCommand = getFindBySaleIdStatement();
            Connection con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, String.valueOf(saleId));
            ResultSet rs = prps.executeQuery();
            ArrayList<TicketEntryClass> tickets = loadAllTickets(rs);
            prps.close();
            con.close();
            return tickets;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<TicketEntryClass> getTicketsByReturnId(Integer returnId){
        try {
            String sqlCommand = getFindByReturnIdStatement();
            Connection con = DBCPDBConnectionPool.getConnection();
            PreparedStatement prps = con.prepareStatement(sqlCommand);
            prps.setString(1, String.valueOf(returnId));
            ResultSet rs = prps.executeQuery();
            ArrayList<TicketEntryClass> tickets = loadAllTickets(rs);
            prps.close();
            con.close();
            return tickets;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private String geAllTransStatement(String tableName) {
        String sqlCommand = "SELECT * FROM " + tableName;
        return sqlCommand;
    }
    protected static String getFindBySaleIdStatement() {
        return "SELECT * FROM ticket WHERE saleId = ?"  ;
    }

    protected static String getFindByReturnIdStatement() {
        return "SELECT * FROM ticket WHERE returnId = ?"  ;
    }
}
