import java.io.*;
import java.sql.*;

public class Mysql {
    public static final String url = "jdbc:mysql://localhost:3306/crawler?serverTimezone=GMT%2B8&useSSL=false";
    public static final String name = "com.mysql.cj.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "123";

    public Connection conn = null;
    public PreparedStatement pst = null;


    public void SaveChapter(int id,int chapter,String titie,File file){
        try{
            String sql = "insert into chapter values (?,?,?,?)";
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);
            pst = conn.prepareStatement(sql);
            pst.setInt(1,id);
            pst.setInt(2,chapter);
            pst.setString(3,titie);
            FileReader fileReader=new FileReader(file);
            pst.setCharacterStream(4, fileReader , (int)file.length());
            pst.execute();
        }catch (Exception e){
            System.out.println("章节储存失败");
        }finally {
            try {
                this.conn.close();
                this.pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void SaveNovel(int id, String title, String writer, String introduction, File file) {
        try {
            String sql = "insert into novel values(?,?,?,?,?)";
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);
            pst = conn.prepareStatement(sql);
            pst.setInt(1,id);
            pst.setString(2,title);
            pst.setString(3,writer);
            pst.setString(4,introduction);
            FileInputStream fis=new FileInputStream(file);
            byte[] b=new byte[fis.available()];
            fis.read(b);
            Blob cover=conn.createBlob();
            OutputStream os=cover.setBinaryStream(1);
            os.write(b);
            os.close();
            fis.close();
            pst.setBlob(5, cover);
            pst.execute();
        } catch (Exception e) {
            System.out.println("小说储存失败");
        } finally {
            try {
                this.conn.close();
                this.pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void SaveMovie(int id, File file, String title, String time,Double score,Integer scorenum
    ,String star5,String star4,String star3,String star2,String star1) {
        try {
            String sql = "insert into movie values(?,?,?,?,?,?,?,?,?,?,?)";
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);
            pst = conn.prepareStatement(sql);
            FileInputStream fis=new FileInputStream(file);
            byte[] b=new byte[fis.available()];
            fis.read(b);
            Blob cover=conn.createBlob();
            OutputStream os=cover.setBinaryStream(1);
            os.write(b);
            os.close();
            fis.close();
            pst.setInt(1,id);
            pst.setBlob(2, cover);
            pst.setString(3,title);
            pst.setString(4,time);
            if(score!=null)
                pst.setDouble(5,score);
            else
                pst.setNull(5,Types.DOUBLE);
            if(scorenum!=null)
                pst.setInt(6,scorenum);
            else
                pst.setNull(6,Types.INTEGER);
            pst.setString(7,star5);
            pst.setString(8,star4);
            pst.setString(9,star3);
            pst.setString(10,star2);
            pst.setString(11,star1);
            pst.execute();
        } catch (Exception e) {
            System.out.println("电影储存失败");
        } finally {
            try {
                this.conn.close();
                this.pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void SaveDirector(int id,int num,String director) {
        try {
            String sql = "insert into director values (?,?,?)";
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);
            pst = conn.prepareStatement(sql);
            pst.setInt(1,id);
            pst.setInt(2,num);
            pst.setString(3,director);
            pst.execute();
        } catch (Exception e) {
            System.out.println("导演储存失败");
        } finally {
            try {
                this.conn.close();
                this.pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void SaveScreenwriter(int id,int num,String screenwriter) {
        try {
            String sql = "insert into screenwriter values (?,?,?)";
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);
            pst = conn.prepareStatement(sql);
            pst.setInt(1,id);
            pst.setInt(2,num);
            pst.setString(3,screenwriter);
            pst.execute();
        } catch (Exception e) {
            System.out.println("编剧储存失败");
        } finally {
            try {
                this.conn.close();
                this.pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void SaveActor(int id,int num,String actor) {
        try {
            String sql = "insert into actor values (?,?,?)";
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);
            pst = conn.prepareStatement(sql);
            pst.setInt(1,id);
            pst.setInt(2,num);
            pst.setString(3,actor);
            pst.execute();
        } catch (Exception e) {
            System.out.println("演员储存失败");
        } finally {
            try {
                this.conn.close();
                this.pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
