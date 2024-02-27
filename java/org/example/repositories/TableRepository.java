package org.example.repositories;

import org.example.data.interfaces.IDB;
import org.example.models.Table;
import org.example.repositories.interfaces.ITableRepository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class TableRepository implements ITableRepository {
    private final IDB db;

    public TableRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createTable(Table table) {
        Connection con = null;

        try {
            con = db.getConnection();
            String sql = "INSERT INTO tables (firstName, secondName, capacity, reserved, phoneNumber) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, table.getFirstName());
            statement.setString(2, table.getSecondName());
            statement.setShort(3, table.getCapacity());
            statement.setBoolean(4, table.isReserved());
            statement.setString(5, table.getPhoneNumber());
            statement.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("sql error: " + e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("sql error: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public Table getTable(short id) {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "SELECT id, firstName, secondName, capacity, reserved, phoneNumber, status FROM tables WHERE id=? ORDER BY id";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setShort(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Table(resultSet.getShort("id"),
                        resultSet.getShort("capacity"),
                        resultSet.getBoolean("reserved"),
                        resultSet.getString("firstName"),
                        resultSet.getString("secondName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("status")
                );
            }
        } catch (SQLException e) {
            System.out.println("sql error: " + e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("sql error: " + e.getMessage());
            }
        }

        return null;
    }

    @Override
    public List<Table> getAllTables() {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "SELECT id, firstName, secondName, capacity, reserved, phoneNumber, status FROM tables ORDER BY id";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Table> reservedTables = new LinkedList<>();
            while (resultSet.next()) {
                Table table = new Table(resultSet.getShort("id"),
                        resultSet.getShort("capacity"),
                        resultSet.getBoolean("reserved"),
                        resultSet.getString("firstName"),
                        resultSet.getString("secondName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("status")
                );
                reservedTables.add(table);
            }
            return reservedTables;
        } catch (SQLException e) {
            System.out.println("sql error: " + e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("sql error: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public boolean reserveTable(short id, String firstName, String secondName, String phoneNumber, boolean reserved) {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "UPDATE tables SET reserved = ?, firstName = ?, secondName = ?, phoneNumber = ? WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setBoolean(1, reserved);
            statement.setString(2, firstName);
            statement.setString(3, secondName);
            statement.setString(4, phoneNumber);
            statement.setShort(5, id);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("sql error: " + e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("sql error: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean editInfo(short id, String firstName, String secondName, short capacity, String phoneNumber) {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "UPDATE tables SET firstName = ?, secondName = ?, capacity = ?, phoneNumber = ? WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, secondName);
            statement.setShort(3, capacity);
            statement.setString(4, phoneNumber);
            statement.setShort(5, id);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("sql error: " + e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("sql error: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean deleteTable(short id) {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "DELETE FROM tables WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setShort(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("sql error: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("sql error: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Table> getReservedTables() {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "SELECT id, firstName, secondName, capacity, reserved, phoneNumber, status FROM tables WHERE reserved = true ORDER BY id";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Table> reservedTables = new LinkedList<>();
            while (resultSet.next()) {
                Table table = new Table(resultSet.getShort("id"),
                        resultSet.getShort("capacity"),
                        resultSet.getBoolean("reserved"),
                        resultSet.getString("firstName"),
                        resultSet.getString("secondName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("status")
                );
                reservedTables.add(table);
            }
            return reservedTables;
        } catch (SQLException e) {
            System.out.println("sql error: " + e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("sql error: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public List<Table> getAvailableTables() {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "SELECT id, firstName, secondName, capacity, reserved, phoneNumber, status FROM tables WHERE reserved = false ORDER BY id";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Table> availableTables = new LinkedList<>();
            while (resultSet.next()) {
                Table table = new Table(resultSet.getShort("id"),
                        resultSet.getShort("capacity"),
                        resultSet.getBoolean("reserved"),
                        resultSet.getString("firstName"),
                        resultSet.getString("secondName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("status")
                );
                availableTables.add(table);
            }
            return availableTables;
        } catch (SQLException e) {
            System.out.println("sql error: " + e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("sql error: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public boolean setStatus(short id, String status) {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "UPDATE tables SET status = ? WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, status);
            statement.setShort(2, id);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("sql error: " + e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("sql error: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean clearTables() {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "DELETE FROM tables";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("sql error: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("sql error: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean resetAutoIncrement() {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "ALTER SEQUENCE tables_id_seq RESTART WITH 1;";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("sql error: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("sql error: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean cancelReservationByNameAndSurname(String firstName, String secondName) {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "UPDATE tables SET reserved = false, firstName = null, secondName = null, phoneNumber = null WHERE firstName = ? AND secondName = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, secondName);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("sql error: " + e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("sql error: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public String findReservationByNameAndSurname(String firstName, String secondName) {
        Connection con = null;
        try {
            con = db.getConnection();
            String sql = "SELECT id FROM tables WHERE firstName = ? AND secondName = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, secondName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return "Reservation found for " + firstName + " " + secondName + " with table ID: " + resultSet.getShort("id");
            } else {
                return "No reservation found for " + firstName + " " + secondName;
            }
        } catch (SQLException e) {
            System.out.println("sql error: " + e.getMessage());
            return "Error: " + e.getMessage();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("sql error: " + e.getMessage());
            }
        }
    }
}
