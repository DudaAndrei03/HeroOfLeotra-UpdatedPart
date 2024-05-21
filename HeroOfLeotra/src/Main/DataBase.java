package Main;

import Entities.Player;
import GameStates.Playing;
import Levels.LevelManager;

import java.sql.*;

public class DataBase {

    private Game game;
    private Connection c = null;
    private Statement stmt = null;

    public DataBase(Game game) {
        this.game = game;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            stmt = c.createStatement();

            ResultSet tables = c.getMetaData().getTables(null, null, "GAME", null);
            ResultSet columns = c.getMetaData().getColumns(null, null, "GAME", null);

            int columnCount = 0;
            while(columns.next())
            {
                columnCount++;
            }

            if(columnCount != 0) {
                if (columnCount != 6) {
                    stmt.execute("DROP TABLE GAME");
                }
            }

            if (!tables.next()) { // daca tabelul nu exista

                String create_tabel = "CREATE TABLE GAME " +
                        "(ID INT PRIMARY KEY NOT NULL, " +
                        " LVLINDEX INT NOT NULL, " +
                        " SCORE INT NOT NULL, " +
                        " HEALTH INT NOT NULL, " +
                        " PLAYER_X REAL NOT NULL, " +
                        " PLAYER_Y REAL NOT NULL)";
                stmt.execute(create_tabel);

                String insert_first_row = "INSERT INTO GAME (ID, LVLINDEX, SCORE, HEALTH, PLAYER_X, PLAYER_Y) VALUES (1, 0, 0, 1, 0, 0)";
                stmt.executeUpdate(insert_first_row);
            }

            stmt.close();
            c.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public void update() {

        try {
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            int Health = game.getPlaying().getPlayer().getCurrentHealth();
            double playerX = game.getPlaying().getPlayer().getHitbox().getX();
            double playerY = game.getPlaying().getPlayer().getHitbox().getY();
            int score = Player.getScore();
            int lvlIndex = LevelManager.getLvlIndex();

            String sql = "UPDATE GAME SET SCORE = ?, LVLINDEX = ?, HEALTH = ?, PLAYER_X = ?, PLAYER_Y = ? WHERE ID = 1";
            try (PreparedStatement pstmt = c.prepareStatement(sql)) {
                pstmt.setInt(1, score);
                pstmt.setInt(2, lvlIndex);
                pstmt.setInt(3, Health);
                pstmt.setDouble(4, playerX);
                pstmt.setDouble(5, playerY);

                pstmt.executeUpdate();
                pstmt.close();
            }
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public int getLvlIndex() {
        int lvlIndex = 1;

        try {
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            stmt = c.createStatement();

            String sql = "SELECT LVLINDEX FROM GAME WHERE ID = 1";
            ResultSet rs = stmt.executeQuery(sql);

            // Retrieve data from the result set
            if (rs.next()) {
                lvlIndex = rs.getInt("LVLINDEX");
            }

            // Close resources
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return lvlIndex;
    }


    public int getScore() {
        int SCORE = 0;

        try {
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            stmt = c.createStatement();

            String sql = "SELECT SCORE FROM GAME WHERE ID = 1";
            ResultSet rs = stmt.executeQuery(sql);

            // Retrieve data from the result set
            if (rs.next()) {
                SCORE = rs.getInt("SCORE");
            }

            // Close resources
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return SCORE;
    }


    public int getHealth() {
        int Health = 1;

        try {
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            stmt = c.createStatement();

            String sql = "SELECT HEALTH FROM GAME WHERE ID = 1";
            ResultSet rs = stmt.executeQuery(sql);

            // Retrieve data from the result set
            if (rs.next()) {
                Health = rs.getInt("HEALTH");
            }

            // Close resources
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return Health;
    }

    public double getPlayerX() {
        double playerX = 0;

        try {
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            stmt = c.createStatement();

            String sql = "SELECT PLAYER_X FROM GAME WHERE ID = 1";
            ResultSet rs = stmt.executeQuery(sql);

            // Retrieve data from the result set
            if (rs.next()) {
                playerX = rs.getDouble("PLAYER_X");
            }

            // Close resources
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return playerX;
    }

    public double getPlayerY() {
        double playerY = 0;

        try {
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            stmt = c.createStatement();

            String sql = "SELECT PLAYER_Y FROM GAME WHERE ID = 1";
            ResultSet rs = stmt.executeQuery(sql);

            // Retrieve data from the result set
            if (rs.next()) {
                playerY = rs.getDouble("PLAYER_Y");
            }

            // Close resources
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return playerY;
    }


}



