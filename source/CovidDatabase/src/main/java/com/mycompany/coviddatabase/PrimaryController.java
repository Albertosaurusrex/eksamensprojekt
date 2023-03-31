package com.mycompany.coviddatabase;

import java.io.IOException;
import javafx.fxml.FXML;
import java.sql.*;
import java.util.regex.Pattern;
import javafx.scene.control.TextArea;

public class PrimaryController {
        //Tekstfelter defineres
    @FXML    
    private TextArea textArea1 = new TextArea();
    
    @FXML
    private TextArea textArea2 = new TextArea();
    
    @FXML
    private TextArea textArea3 = new TextArea();
    
    @FXML
    private TextArea textArea4 = new TextArea();
    
            //Benytter Regular Expression (RegEx) til at tjekke om en String overholder
            //det gældende dato format. Koden er taget fra Baeldung.com
    private static Pattern DATE_PATTERN = Pattern.compile(
            //Den første linje tjekker om d. 29. februar eksisterer i det gældende år
            //Hvis året er 2000, 2400 eller 2800, er der tale om skudår
            //(19|2[0-9]) tjekker om de to første cifre i året er 19 eller 20-29
            //(0[48]|[2468][048]|[13579][26]) tjekker om de to sidste cifre er delelige med 4
            //Først tjekker 0[48] om de to cifre enten er 04 eller 08
            //[2468][048] tjekker om de er 20, 40, 60, 80, 24, 44, 64, 84, 60, 64, 68, 80, 84 eller 88
            //[13579][26] tjekker om de er 12, 16, 32, 36, 52, 56, 72, 76, 92 eller 96
            //-02-29 tjekker om datoen er d. 29. februar
      "^((2000|2400|2800|(19|2[0-9])(0[48]|[2468][048]|[13579][26]))-02-29)$" 
            //Den anden linje tjekker om d. 28. matcher i alle andre år
            //(19|2[0-9]) 1900 eller 2000-2900
            //[0-9]{2} de næste to cifre er 0-9
            //-02- måneden er februar
            //(0[1-9]|1[0-9]|2[0-8]) dagen er enten 01-09, 10-19 eller 20-28
      + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
            //Den tredje linje tjekker om inputtet matcher måneder med 31 dage
            //((19|2[0-9])[0-9]{2}) tjekker år som før
            //-(0[13578]|10|12) måneden er enten 01, 03, 05, 07, 08, 10 eller 12
            //-(0[1-9]|[12][0-9]|3[01]) dagen er enten 01-09, 10-19, 20-29, 30 eller 31
      + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$" 
            //Den fjerde linje tjekker om inputtet matcher måneder med 30 dage
            //((19|2[0-9])[0-9]{2}) tjekker år
            //-(0[469]|11) måneden er enten 04, 06, 09 eller 11
            //(0[1-9]|[12][0-9]|30) dagen er enten 01-09, 10-19, 20-29 eller 30
      + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");

            //Tjekker om en inputtet String overholder en af de ovenstående RegEx linjer
            //returner true hvis match
    public static boolean dateMatch(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }
    
            //getData() skal hente alt data i databasen om en bestemt person
    void getData(){
        Connection c = null;
        Statement stmt = null;
        
        try{
            //Forsøger at oprette forbindelse til databasen DATA.bd
           Class.forName("org.sqlite.JDBC");
           c = DriverManager.getConnection("jdbc:sqlite:src/DATA.db");
            //Gør så programmet ikke comitter nogle ændringer til databasen
            //Da getData() kun skal hente og vise data fra databasen
           c.setAutoCommit(false);
           stmt = c.createStatement();
           
           //Select statement udføres, og resultaterne samles i et resultset
           //Select statmentet vælger alt fra MOCK_DATA tabellen hvor ssn (social security number)
           //er det som står i SSN tekstfeltet i programmet
           ResultSet rs = stmt.executeQuery("SELECT * FROM MOCK_DATA WHERE ssn = " + "\"" + textArea2.getText() + "\"");
           
            //Mens vi har resultaterne, udskrives den gældende persons information i tekstfelt 1
            //Det store tekstfelt i programmet
           while(rs.next()){
            textArea1.setText(textArea1.getText() + "ID: " + rs.getString("ID") + " | SSN: " + rs.getString("ssn") +
            " \nFirst name: " + rs.getString("first_name") + " \nLast name: " + rs.getString("last_name") +
            " \nGender: " + rs.getString("gender") + " | Age: " + rs.getString("age") +
            " \nInfection status: " + rs.getString("infection_status") + " | Date of infection: " + rs.getString("infection_date") +
            " \nVaccine status: " + rs.getString("vaccine_status") + " | Date of last vaccination: " + rs.getString("last_vaccine_date")
            );
           }
        }
            //Fejlhåndtering
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
     //updateInfection1() skal opdatere den gældende persons infektionsstatus fra 0 til 1, fra rask til inficeret
    void updateInfection1() throws ClassNotFoundException, SQLException{
    Connection conn = null;

    String sql = null;
    
     //En forbindelse til databasen oprettes
    try{
            Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:src/DATA.db");
    }
     //Fejlhåndtering
    catch(SQLException e){
        System.out.println("DB Error: " + e.getMessage());
    }
    
     //En boolean "date" oprettes, der køres et RegEx tjek på strengen i tekstfelt 3
    boolean date = dateMatch(textArea3.getText());
    
     //Hvis RegEx tjekket var positivt, og date derfor er true skrives et UPDATE statement
     //infection_status opdateres til 1, og infection_date opdateres til datoen i tekstfelt 3
    if(date == true){    
        sql = "UPDATE MOCK_DATA SET infection_status = 1, infection_date = " + "\"" + textArea3.getText() + "\" WHERE ssn = " + 
            "\"" + textArea2.getText() + "\";";
     //Hvis RegEx tjekket var negativt, og date derfor er false, bedes brugeren om at inputte en gyldig dato
     //Der skrives ikke et UPDATE statement
    } else{
        textArea1.setText("Please input a valid date!");
    }
    
    //Programmet forsøger at sende strengen "sql" til databasen
    //hvis "date" er true er der skrevet et UPDATE statement i "sql", og det sendes til databasen
    //hvis "date" er false, er sql = null, og der sendes null til databasen
    try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.execute();
         //Database connectionen lukkes
        conn.close();
     //Fejlhåndtering
    }catch(SQLException e) {
    System.out.println(e.getMessage());
     }
    }
    
     //updateInfection0() skal ændre den gældende persons infection_status til 0, og infection_date til null
     //Denne virker på præcis samme måde som updateInfection1() med undtagelse af ændringen i UPDATE statementet
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
    
    sql = "UPDATE MOCK_DATA SET infection_status = 0, infection_date = NULL WHERE ssn = " + "\"" + textArea2.getText() + "\"";
    
    
    
    try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.execute();
    conn.close();
    } catch(SQLException e) {
    System.out.println(e.getMessage());
     }
    }
    
     //updateVaccine1() skal opdatere den gældende persons vaccine_status til 1,
     //og ændre last_vaccine_date til den gyldige dato i tekstfelt 4
     //Overordnet virker denne metode på samme måde som de to foregående (se detaljeret beskrivelse under updateInfection1())
     //Alle undtagelse beskrives herunder
    void updateVaccine1() throws ClassNotFoundException, SQLException{
    Connection conn = null;
     //Der oprettes et Statement, ligesom i getData(), da metoden skal bruge data fra databasen
    Statement stmt = null;

    String sql = null;
     //En streng "vacStat" oprettes, kort for vaccine status, dette bruges senere
    String vacStat = null;
    
    try{
            Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:src/DATA.db");
       
           stmt = conn.createStatement();
    }
    catch(SQLException e){
        System.out.println("DB Error: " + e.getMessage());
    }
    
     //String i tekstfelt 4 tjekkes med RegEx
    boolean date = dateMatch(textArea4.getText());
    
    if(date == true) {
     //Hvis datoen findes får vi et resultset for den gældende person
    ResultSet rs = stmt.executeQuery("SELECT * FROM MOCK_DATA WHERE ssn = " + "\"" + textArea2.getText() + "\"");
    
     //den gældende persons vaccine_status udtrækkes
    while(rs.next()) {
    vacStat = rs.getString("vaccine_status");
    }
   
     //Hvis vaccine_status allerede er 1, skrives det til brugeren
    if("1".equals(vacStat)){
        textArea1.setText("Person has already recieved first vaccination!");
     //Hvis personen allerede har modtaget 2 vacciner, kan tallet ikke ændres tilbage til 1
    } else if("2".equals(vacStat)) {
        textArea1.setText("Person has already recieved second vaccinations!");
     //Hvis personen allerede har modtaget 3 vacciner, kan tallet ikke ændres tilbage til 1
    } else if("3".equals(vacStat)) {
        textArea1.setText("Person has already recieved third vaccinations!");
     //Hvis personen hverken har 1, 2 eller 3 vacciner ændres tallet til 1
     //og last_vaccine_date ændres til strengen i tekstfelt 4
     //Da ændringerne sker med knapper, kan tallet ikke være andet end 0 (hvor det starter i databasen)
     //1, 2 eller 3
    } else{
        sql = "UPDATE MOCK_DATA SET vaccine_status = 1, last_vaccine_date = " + "\"" + textArea4.getText() + "\" WHERE ssn = " + 
            "\"" + textArea2.getText() + "\";";
    }
     //Hvis strengen i tekstfelt 4 ikke er en gyldig dato skrives til til brugeren
    } else{
        textArea1.setText("Please input a valid date!");
    }
    
     //sql statmentet sendes til databasen
    try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.execute();
    conn.close();
    } catch(SQLException e) {
    System.out.println(e.getMessage());
     }
    }
    
     //updateVaccine2() skal ændre den gældende persons vaccine_status til 2, og ændre last_vaccine_date
     //Metoden virker på præcis samme måde som updateVaccine1() med untagelse af ændringen i UPDATE statementet
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
    
    boolean date = dateMatch(textArea4.getText());
    
    if(date == true){
    ResultSet rs = stmt.executeQuery("SELECT * FROM MOCK_DATA WHERE ssn = " + "\"" + textArea2.getText() + "\"");
    
    while(rs.next()) {
    vacStat = rs.getString("vaccine_status");
    }
    
     //Hvis vaccine_status allerede er 2, informeres brugeren om det
    if("2".equals(vacStat)) {
        textArea1.setText("Person has already recieved second vaccinations!");
        //Hvis tallet er 3, kan det ikke ændres tilbage til 2
    } else if("3".equals(vacStat)) {
        textArea1.setText("Person has already recieved third vaccinations!");
        //Hvis tallet er 0, kan det ikke springe til 2, det skal først have været 1
    } else if("0".equals(vacStat)) {
        textArea1.setText("Person has not recieved first vaccination!");
        //Altså, hvis tallet er 1, ændres det til 2 og dato opdateres
    }else{
        sql = "UPDATE MOCK_DATA SET vaccine_status = 2, last_vaccine_date = " + "\"" + textArea4.getText() + "\" WHERE ssn = " + 
            "\"" + textArea2.getText() + "\";";
        System.out.println(vacStat);
    }
     //Hvis dato ikke er gyldig informeres brugeren om det
    } else{
        textArea1.setText("Pleas input a valid date!");
    }
    
     //sql statment sendes til databasen, og databasen lukkes
    try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.execute();
    conn.close();
     //Fejlhåndtering
    } catch(SQLException e) {
    System.out.println(e.getMessage());
     }
    }
    
     //updateVaccine3() skal ændre vaccine_status til 3 og opdatere last_vaccine_date
     //Metoden virker på præcis samme måde som updateVaccine1() og updateVaccine2()
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
    
    boolean date = dateMatch(textArea4.getText());
    
    if(date == true){
    ResultSet rs = stmt.executeQuery("SELECT * FROM MOCK_DATA WHERE ssn = " + "\"" + textArea2.getText() + "\"");
    
    while(rs.next()) {
    vacStat = rs.getString("vaccine_status");
    }
    
     //Hvis vaccine_status er 1, kan personen ikke 3. vaccine, da personen ikke har fået 2. vaccine endnu
    if("1".equals(vacStat)){
        textArea1.setText("Person has not recieved second vaccination!");
        //Hvis tallet er 0 har personen ikke modtaget 1. vaccine, og kan derfor ikke få 3.
    } else if("0".equals(vacStat)) {
        textArea1.setText("Person has not recieved first vaccination!");
        //Hvis tallet er 3, har personen allerede 3 vacciner, og brugeren informeres
    } else if("3".equals(vacStat)) {
        textArea1.setText("Person has already recieved third vaccination!");
        //Altså, hvis tallet er 2, opdateres det til 3, og datoen opdateres
    } else{
        sql = "UPDATE MOCK_DATA SET vaccine_status = 3, last_vaccine_date = " + "\"" + textArea4.getText() + "\" WHERE ssn = " + 
            "\"" + textArea2.getText() + "\";";
        System.out.println(vacStat);
    }
     //Hvis dato ikke er gyldig informeres brugeren
    } else{
        textArea1.setText("Please input a valid date!");
    }
    
     //sql statement udføres og database connection lukkes
    try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.execute();
    conn.close();
    } catch(SQLException e) {
    System.out.println(e.getMessage());
     }
    }
    
     //resetVaccine() skal ændre den gældende persons vaccine_status til 0, og last_vaccine_date til null
     //Denne metode skal blot bruges til testing, da man ikke vil kunne fjerne vacciner i det endelige program
     //Metoden virker stor set på samme måde som alle de foregående UPDATE metoder
     //Der er dog ikke nogle restrictions på denne som f.eks. på updateVaccine1()
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
    
        sql = "UPDATE MOCK_DATA SET vaccine_status = 0, last_vaccine_date = NULL WHERE ssn = " + 
            "\"" + textArea2.getText() + "\";";
    
    try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.execute();
    conn.close();
    } catch(SQLException e) {
    System.out.println(e.getMessage());
     }
    }
    
     //Alt herunder definere buttons i programmet
    
     //onButtonPressed kalder getData() og henter den gældende persons data i programmet
    @FXML
    private void onButtonPressed() throws IOException {
    textArea1.setText("");
    getData();
    }
    
     //infected() kalder updateInfection1() og opdaterer den gældende persons infection_status til 1 og infection_date og henter data
    @FXML
    private void infected() throws IOException, ClassNotFoundException, SQLException {
    updateInfection1();
    textArea1.setText("");
    getData();
    }
    
     //notInfected() kalder updateInfection0() og opdaterer den gældende persons infection_status til 0 og infection_date til null
     //og henter data
    @FXML
    private void notInfected() throws IOException, ClassNotFoundException, SQLException {
    updateInfection0();
    textArea1.setText("");
    getData();
    }
    
     //vaccine1() kalder updateVaccine1() og opdaterer den gældende persons vaccine_status til 1 og last_vaccine date
     //hvis alle krav er overholdt, henter data
    @FXML
    private void vaccine1() throws IOException, ClassNotFoundException, SQLException {
    updateVaccine1();
    textArea1.setText("");
    getData();
    }
     //vaccine2() kalder updateVaccine2() og opdaterer den gældende persons vaccine_status til 2 og last_vaccine date
     //hvis alle krav er overholdt, henter data
    @FXML
    private void vaccine2() throws IOException, ClassNotFoundException, SQLException {
    updateVaccine2();
    textArea1.setText("");
    getData();
    }
    
     //vaccine3() kalder updateVaccine3() og opdaterer den gældende persons vaccine_status til 3 og last_vaccine date
     //hvis alle krav er overholdt, henter data
    @FXML
    private void vaccine3() throws IOException, ClassNotFoundException, SQLException {
    updateVaccine3();
    textArea1.setText("");
    getData();
    }
    
     //resetVaccineButton() kalder resetVaccine(), og sætter den gældende persons vaccine_status til 0 og last_vaccine_date til null
     //til testing, henter data
    @FXML
    private void resetVaccineButton() throws IOException, ClassNotFoundException, SQLException {
    resetVaccine();
    textArea1.setText("");
    getData();
    }   
}
