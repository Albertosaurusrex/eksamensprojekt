package com.mycompany.coviddatabase;

import java.io.IOException;
import javafx.fxml.FXML;
import java.sql.*;
import javafx.scene.control.TextArea;

public class PrimaryController {

    @FXML    
    private TextArea textArea1 = new TextArea();
    
    @FXML
    private TextArea textArea2 = new TextArea();
    
    @FXML
    private TextArea textArea3 = new TextArea();
    
    @FXML
    private TextArea textArea4 = new TextArea();
   
    void getData(){
        Connection c = null;
        Statement stmt = null;
            
        try{
           Class.forName("org.sqlite.JDBC"); 
           c = DriverManager.getConnection("jdbc:sqlite:src/DATA.db");
           c.setAutoCommit(false);
           stmt = c.createStatement();
           
           //Select
           ResultSet rs = stmt.executeQuery("SELECT * FROM MOCK_DATA WHERE ssn = " + "\"" + textArea2.getText() + "\"");
           
           while(rs.next()){
            textArea1.setText(textArea1.getText() + "ID: " + rs.getString("ID") + " | SSN: " + rs.getString("ssn") +
            " \nFirst name: " + rs.getString("first_name") + " \nLast name: " + rs.getString("last_name") +
            " \nGender: " + rs.getString("gender") + " | Age: " + rs.getString("age") +
            " \nInfection status: " + rs.getString("infection_status") + " | Date of infection: " + rs.getString("infection_date") +
            " \nVaccine status: " + rs.getString("vaccine_status") + " | Date of last vaccination: " + rs.getString("last_vaccine_date")
            );
           }
        }
        catch(Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
     //finally{} forsøger at lukke tilslutningen til databasen
        } finally{
            if(c != null) {
             try{
                c.close();
             } catch(SQLException e) {}
            }
          }
    
}
    
    void updateInfection1() throws ClassNotFoundException, SQLException{
    Connection conn = null;

    String sql;
    
    try{
            Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:src/DATA.db");
    }
    catch(SQLException e){
        System.out.println("DB Error: " + e.getMessage());
    }
    
    sql = "UPDATE MOCK_DATA SET infection_status = 1 WHERE ssn = " + "\"" + textArea2.getText() + "\"";
    
    
    
    try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.execute();
        conn.close();
    }catch(SQLException e) {
    System.out.println(e.getMessage());
     }
    }
    
    void updateInfection0() throws ClassNotFoundException, SQLException{
    Connection conn = null;

    String sql;
    
    try{
            Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:src/DATA.db");
    }
    catch(SQLException e){
        System.out.println("DB Error: " + e.getMessage());
    }
    
    sql = "UPDATE MOCK_DATA SET infection_status = 0 WHERE ssn = " + "\"" + textArea2.getText() + "\"";
    
    
    
    try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.execute();
    conn.close();
    } catch(SQLException e) {
    System.out.println(e.getMessage());
     }
    }
    
    
    void updateVaccine1() throws ClassNotFoundException, SQLException{
    Connection conn = null;
    Statement stmt = null;

    String sql = null;
    String vacStat = null;
    
    try{
            Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:src/DATA.db");
       
           stmt = conn.createStatement();
    }
    catch(SQLException e){
        System.out.println("DB Error: " + e.getMessage());
    }
    
    ResultSet rs = stmt.executeQuery("SELECT * FROM MOCK_DATA WHERE ssn = " + "\"" + textArea2.getText() + "\"");
    
    while(rs.next()) {
    vacStat = rs.getString("vaccine_status");
    }
   
    if("1".equals(vacStat)){
        textArea1.setText("Person has already recieved first vaccination!");
    } else if("2".equals(vacStat)) {
        textArea1.setText("Person has already recieved second vaccinations!");
    } else if("3".equals(vacStat)) {
        textArea1.setText("Person has already recieved third vaccinations!");
    } else{
        sql = "UPDATE MOCK_DATA SET vaccine_status = 1 WHERE ssn = " + "\"" + textArea2.getText() + "\"";
        System.out.println("test1");
        System.out.println(vacStat);
    }
    
    try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.execute();
    conn.close();
    } catch(SQLException e) {
    System.out.println(e.getMessage());
     }
    }
    
    void updateVaccine2() throws ClassNotFoundException, SQLException{
    Connection conn = null;
    Statement stmt = null;

    String sql = null;
    String vacStat = null;
    
    try{
            Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:src/DATA.db");
           stmt = conn.createStatement();
    }
    catch(SQLException e){
        System.out.println("DB Error: " + e.getMessage());
    }
    
    ResultSet rs = stmt.executeQuery("SELECT * FROM MOCK_DATA WHERE ssn = " + "\"" + textArea2.getText() + "\"");
    
    while(rs.next()) {
    vacStat = rs.getString("vaccine_status");
    }
    
    if("2".equals(vacStat)) {
        textArea1.setText("Person has already recieved second vaccinations!");
    } else if("3".equals(vacStat)) {
        textArea1.setText("Person has already recieved third vaccinations!");
    } else if("0".equals(vacStat)) {
        textArea1.setText("Person has not recieved first vaccination!");
    }else{
        sql = "UPDATE MOCK_DATA SET vaccine_status = 2 WHERE ssn = " + "\"" + textArea2.getText() + "\"";
        System.out.println("test1");
        System.out.println(vacStat);
    }
    
    
    try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.execute();
    conn.close();
    } catch(SQLException e) {
    System.out.println(e.getMessage());
     }
    }
    
    void updateVaccine3() throws ClassNotFoundException, SQLException{
    Connection conn = null;
    Statement stmt = null;

    String sql = null;
    String vacStat = null;
    
    try{
            Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:src/DATA.db");
           stmt = conn.createStatement();
    }
    catch(SQLException e){
        System.out.println("DB Error: " + e.getMessage());
    }
    
    ResultSet rs = stmt.executeQuery("SELECT * FROM MOCK_DATA WHERE ssn = " + "\"" + textArea2.getText() + "\"");
    
    while(rs.next()) {
    vacStat = rs.getString("vaccine_status");
    }
    
    if("1".equals(vacStat)){
        textArea1.setText("Person has not recieved second vaccination!");
    } else if("0".equals(vacStat)) {
        textArea1.setText("Person has not recieved first vaccination!");
    } else if("3".equals(vacStat)) {
        textArea1.setText("Person has already recieved third vaccination!");
    } else{
        sql = "UPDATE MOCK_DATA SET vaccine_status = 3 WHERE ssn = " + "\"" + textArea2.getText() + "\"";
        System.out.println("test1");
        System.out.println(vacStat);
    }
    
    
    
    try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.execute();
    } catch(SQLException e) {
    System.out.println(e.getMessage());
     }
    System.out.println("test3.1");
    }
    
    void resetVaccine() throws ClassNotFoundException, SQLException{
    Connection conn = null;

    String sql = null;
    
    try{
            Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:src/DATA.db");
    }
    catch(SQLException e){
        System.out.println("DB Error: " + e.getMessage());
    }
    
        sql = "UPDATE MOCK_DATA SET vaccine_status = 0 WHERE ssn = " + "\"" + textArea2.getText() + "\"";

    
    
    
    try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.execute();
    } catch(SQLException e) {
    System.out.println(e.getMessage());
     }
    System.out.println("test3.1");
    }
    
    @FXML
    private void onButtonPressed() throws IOException {
    textArea1.setText("");
    getData();
    }
    
    @FXML
    private void infected() throws IOException, ClassNotFoundException, SQLException {
    updateInfection1();
    textArea1.setText("");
    getData();
    }
    
    @FXML
    private void notInfected() throws IOException, ClassNotFoundException, SQLException {
    updateInfection0();
    textArea1.setText("");
    getData();
    }
    
    @FXML
    private void vaccine1() throws IOException, ClassNotFoundException, SQLException {
    updateVaccine1();
    textArea1.setText("");
    getData();
    }
    
    @FXML
    private void vaccine2() throws IOException, ClassNotFoundException, SQLException {
    updateVaccine2();
    textArea1.setText("");
    getData();
    }
    
    @FXML
    private void vaccine3() throws IOException, ClassNotFoundException, SQLException {
    updateVaccine3();
    textArea1.setText("");
    getData();
    }
    
    @FXML
    private void resetVaccineButton() throws IOException, ClassNotFoundException, SQLException {
    resetVaccine();
    textArea1.setText("");
    getData();
    }
    
    
}
