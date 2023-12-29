package model;

import java.io.InputStream;
import java.sql.*;
import java.util.Vector;

public class DBHandler {
    Connection dbConnection;
    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://"+config.dbHost+":"+config.dbPort+"/"+config.dbName;
        Class.forName("com.mysql.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString,config.dbUser,config.dbPass);
        return dbConnection;
    }

    public void singUpUser(String name, String FirstName, String SecondName, String password) {
        singUpUser(name,FirstName,SecondName,password, "basic");
    }
    public void singUpUser(String name, String FirstName, String SecondName, String password, String role){
        try {
            String insert = "INSERT INTO "+Const.USERS_TABLE +"("+Const.USERS_NAME+","+Const.USERS_FIRST_NAME+","+Const.USERS_SECOND_NAME+","+Const.USERS_PASSWORD+","
                    +Const.USERS_ROLE+")"+"VALUES(?,?,?,?,?)";
            PreparedStatement prSt =getDbConnection().prepareStatement(insert);
            prSt.setString(1, name);
            prSt.setString(2, FirstName);
            prSt.setString(3, SecondName);
            prSt.setString(4, password);
            prSt.setString(5, role);
            prSt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public boolean CheckUser(String name){
        ResultSet resSet = null;
        String select = "SELECT * FROM "+ Const.USERS_TABLE + " WHERE "+Const.USERS_NAME + "=?";
        try {
            PreparedStatement prSt =getDbConnection().prepareStatement(select);
            prSt.setString(1, name);
            resSet = prSt.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        int counter =0;
        try {
            while (resSet.next()){
                counter++;
                break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(counter>=1){
            return true;
        }
        return false;
    }
    public boolean CheckUser(String name, String password){
        ResultSet resSet = null;
        String select = "SELECT * FROM "+ Const.USERS_TABLE + " WHERE "+Const.USERS_NAME + "=?"+" AND "+Const.USERS_PASSWORD+"=?";
        try {
            PreparedStatement prSt =getDbConnection().prepareStatement(select);
            prSt.setString(1, name);
            prSt.setString(2, password);
            resSet = prSt.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        int counter =0;
        try {
            while (resSet.next()){
                counter++;
                break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(counter>=1){
            return true;
        }
        return false;
    }
    public int getNumOfEnter(){
        ResultSet resSet = null;
        String select = "SELECT * FROM "+ Const.NUMOFENTER_TABLE;
        try {
            PreparedStatement prSt =getDbConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
            resSet.next();
            return resSet.getInt(Const.NUMOFENTER_NUM);
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return 0;
    }
    public void upNumOfEnter(){
        try {
            String insert = "UPDATE "+Const.NUMOFENTER_TABLE + " SET " + Const.NUMOFENTER_NUM + " = ?";
            PreparedStatement prSt =getDbConnection().prepareStatement(insert);
            int tmp = getNumOfEnter();
            tmp++;
            String rez ="";
            rez=rez+tmp;
            prSt.setString(1, rez.trim());
            prSt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public boolean isModer(String name){
        ResultSet resSet = null;
        String select = "SELECT * FROM "+ Const.USERS_TABLE + " WHERE "+Const.USERS_NAME + "=?";
        try {
            PreparedStatement prSt =getDbConnection().prepareStatement(select);
            prSt.setString(1, name);
            resSet = prSt.executeQuery();
            resSet.next();
            if(resSet.getString(Const.USERS_ROLE).equals("moderator")||resSet.getString(Const.USERS_ROLE).equals("admin")){
                return true;
            }else {
                return false;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean isAdmin(String name){
        ResultSet resSet = null;
        String select = "SELECT * FROM "+ Const.USERS_TABLE + " WHERE "+Const.USERS_NAME + "=?";
        try {
            PreparedStatement prSt =getDbConnection().prepareStatement(select);
            prSt.setString(1, name);
            resSet = prSt.executeQuery();
            resSet.next();
            if(resSet.getString(Const.USERS_ROLE).equals("admin")){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return false;
    }
    public ResultSet GetPosters(){
        ResultSet resSet = null;
        String select = "SELECT * FROM "+ Const.POSTERS_TABLE;
        try {
            PreparedStatement prSt =getDbConnection().prepareStatement(select);
            resSet = prSt.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return resSet;
    }
    public byte[] getImgByName(String name){
        ResultSet resSet = null;
        String select = "SELECT * FROM "+ Const.POSTERS_TABLE+ " WHERE "+Const.POSTER_NAME + "=?";
        try {
            PreparedStatement prSt =getDbConnection().prepareStatement(select);
            prSt.setString(1, name);
            resSet = prSt.executeQuery();
            resSet.next();
            return resSet.getBytes(Const.POSTER_IMAGE);
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
    public void AddNewFilmPost(String name, String descr, InputStream img){
        try {
            String insert = "INSERT INTO "+Const.POSTERS_TABLE +"("+Const.POSTER_NAME+","+Const.POSTER_DECKIPTION+","+Const.POSTER_IMAGE+")"+"VALUES(?,?,?)";
            PreparedStatement prSt =getDbConnection().prepareStatement(insert);
            prSt.setString(1, name);
            prSt.setString(2, descr);
            prSt.setBlob(3, img);
            prSt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public void AddFavorite(String nameOfUser,String nameOfPost){
        try {

            String insert = "INSERT INTO "+Const.FAVORITE_TABLE +"("+Const.FAVORITE_NAME_OF_USER+","+Const.FAVORITE_NAME_OF_POST+")"+"VALUES(?,?)";
            PreparedStatement prSt =getDbConnection().prepareStatement(insert);
            prSt.setString(1, nameOfUser);
            prSt.setString(2, nameOfPost);
            prSt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public boolean CheckFavorite(String nameOfUser,String nameOfPost){
        ResultSet resSet = null;
        try {
        String insert =  "SELECT * FROM "+ Const.FAVORITE_TABLE + " WHERE "+Const.FAVORITE_NAME_OF_USER + "=?"+" AND "+Const.FAVORITE_NAME_OF_POST+"=?";
        PreparedStatement prSt =getDbConnection().prepareStatement(insert);
        prSt.setString(1, nameOfUser);
        prSt.setString(2, nameOfPost);
        resSet = prSt.executeQuery();
        while (resSet.next()){
            return true;
        }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();}
        return false;
    }
    public void DeletePost(String name){
        try {
            String delete = "DELETE FROM "+ Const.POSTERS_TABLE + " WHERE " + Const.POSTER_NAME + "=?";
            PreparedStatement prSt =getDbConnection().prepareStatement(delete);
            prSt.setString(1, name);
            prSt.executeUpdate();
            delete = "DELETE FROM "+ Const.FAVORITE_TABLE + " WHERE " + Const.FAVORITE_NAME_OF_POST + "=?";
            prSt =getDbConnection().prepareStatement(delete);
            prSt.setString(1, name);
            prSt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public void UpdatePost(String Oldname,String Newname,String descr){
        try {
            PreparedStatement prSt = null;
            String sql = "UPDATE " + Const.POSTERS_TABLE + " SET " + Const.POSTER_NAME + " = ? , " + Const.POSTER_DECKIPTION + " = ?  WHERE " + Const.POSTER_NAME + " = ?";
            prSt = getDbConnection().prepareStatement(sql);
            prSt.setString(1, Newname);
            prSt.setString(2, descr);
            prSt.setString(3, Oldname);
            prSt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public void UpdatePost(String Oldname,String Newname,String descr, InputStream img){
        try {
            PreparedStatement prSt = null;
            String sql = "UPDATE " + Const.POSTERS_TABLE + " SET " + Const.POSTER_NAME + " = ? , " + Const.POSTER_DECKIPTION + " = ? , "+Const.POSTER_IMAGE+"= ? WHERE " + Const.POSTER_NAME + " = ?";
            prSt = getDbConnection().prepareStatement(sql);
            prSt.setString(1, Newname);
            prSt.setString(2, descr);
            prSt.setString(3, Oldname);
            prSt.setBlob(4, img);
            prSt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public boolean CheckPost(String name){
        ResultSet resSet = null;
        try {
            String insert =  "SELECT * FROM "+ Const.POSTERS_TABLE + " WHERE "+Const.POSTER_NAME + "=?";
            PreparedStatement prSt =getDbConnection().prepareStatement(insert);
            prSt.setString(1, name);
            resSet = prSt.executeQuery();
            while (resSet.next()){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();}
        return false;
    }
    public ResultSet GetFavoritePost(String nameOfUser){

        ResultSet resSet = null;
        String select = "SELECT * FROM "+ Const.FAVORITE_TABLE+ " WHERE "+Const.FAVORITE_NAME_OF_USER + "=?";
        try {
            PreparedStatement prSt =getDbConnection().prepareStatement(select);
            prSt.setString(1, nameOfUser);
            resSet = prSt.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return resSet;
    }
    public ResultSet GetPost(String name){
        ResultSet resSet = null;
        String select = "SELECT * FROM "+ Const.POSTERS_TABLE+ " WHERE "+Const.POSTER_NAME + "=?";
        try {
            PreparedStatement prSt =getDbConnection().prepareStatement(select);
            prSt.setString(1, name);
            resSet = prSt.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return resSet;
    }
    public void DeleteFavorite(String name){
        try {
            String delete = "DELETE FROM "+ Const.FAVORITE_TABLE + " WHERE " + Const.FAVORITE_NAME_OF_POST + "=?";
            PreparedStatement prSt =getDbConnection().prepareStatement(delete);
            prSt.setString(1, name);
            prSt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public void UpdateUser(String Oldname,String name, String FirstName, String SecondName, String password){
        UpdateUser(Oldname,name,FirstName,SecondName,password,"basic");
    }
    public void UpdateUser(String Oldname,String name, String FirstName, String SecondName, String password, String role){
        try{
        PreparedStatement prSt = null;
        String sql = "UPDATE " + Const.USERS_TABLE + " SET " + Const.USERS_NAME + " = ? , " + Const.USERS_FIRST_NAME + " = ? , "+Const.USERS_SECOND_NAME+" = ? , "+Const.USERS_PASSWORD+" = ? , "+Const.USERS_ROLE+"= ? WHERE " + Const.USERS_NAME + " = ?";
        prSt = getDbConnection().prepareStatement(sql);
        prSt.setString(1, name);
        prSt.setString(2, FirstName);
        prSt.setString(3, SecondName);
        prSt.setString(4, password);
        prSt.setString(5, role);
        prSt.setString(6, Oldname);
        prSt.executeUpdate();
    }catch (SQLException e){
        e.printStackTrace();
    }catch (ClassNotFoundException e){
        e.printStackTrace();
    }
    }
    public void DeleteUser(String name){
    try {
        String delete = "DELETE FROM "+ Const.USERS_TABLE + " WHERE " + Const.USERS_NAME + "=?";
        PreparedStatement prSt =getDbConnection().prepareStatement(delete);
        prSt.setString(1, name);
        prSt.executeUpdate();
    }catch (SQLException e){
        e.printStackTrace();
    }catch (ClassNotFoundException e){
        e.printStackTrace();
    }
}
    public ResultSet GetUser(String name){
    ResultSet resSet = null;
    String select = "SELECT * FROM "+ Const.USERS_TABLE+ " WHERE "+Const.USERS_NAME + "=?";
    try {
        PreparedStatement prSt =getDbConnection().prepareStatement(select);
        prSt.setString(1, name);
        resSet = prSt.executeQuery();
    }catch (SQLException e){
        e.printStackTrace();
    }catch (ClassNotFoundException e){
        e.printStackTrace();
    }
    return resSet;
}
public ResultSet GetUsers(){
    ResultSet resSet = null;
    String select = "SELECT * FROM "+ Const.USERS_TABLE;
    try {
        PreparedStatement prSt =getDbConnection().prepareStatement(select);
        resSet = prSt.executeQuery();
    }catch (SQLException e){
        e.printStackTrace();
    }catch (ClassNotFoundException e){
        e.printStackTrace();
    }
    return resSet;
}

}
