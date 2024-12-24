package class1;

import java.sql.*;
import java.util.Scanner;

public class ToDoListApp {

    // Veritabanı bağlantı bilgileri
    private static final String URL = "jdbc:mysql://localhost:3306/ToDoApp";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "C2903";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("Veritabanına başarıyla bağlanıldı!");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\n--- To-Do List ---");
                System.out.println("1. Görev Ekle");
                System.out.println("2. Görevleri Listele");
                System.out.println("3. Görev Sil");
                System.out.println("4. Çıkış");
                System.out.print("Bir işlem seçin: ");

                int secim = scanner.nextInt();
                scanner.nextLine(); // Enter karakterini temizler

                switch (secim) {
                    case 1:
                        System.out.print("Eklemek istediğiniz görevi yazın: ");
                        String gorev = scanner.nextLine();
                        addTask(connection, gorev);
                        break;

                    case 2:
                        listTasks(connection);
                        break;

                    case 3:
                        System.out.print("Silmek istediğiniz görevin numarasını girin: ");
                        int taskId = scanner.nextInt();
                        deleteTask(connection, taskId);
                        break;

                    case 4:
                        System.out.println("Uygulamadan çıkılıyor. Hoşça kal!");
                        scanner.close();
                        System.exit(0);

                    default:
                        System.out.println("Geçersiz bir seçim yaptınız. Tekrar deneyin.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Veritabanı hatası: " + e.getMessage());
        }
    }

    // Görev ekleme
    private static void addTask(Connection connection, String description) {
        String sql = "INSERT INTO tasks (description) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, description);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Görev başarıyla eklendi!");
            } else {
                System.out.println("Görev eklenemedi.");
            }
        } catch (SQLException e) {
            System.err.println("Görev ekleme hatası: " + e.getMessage());
        }
    }

    // Görevleri listeleme
    private static void listTasks(Connection connection) {
        String sql = "SELECT * FROM tasks";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("\n--- Mevcut Görevler ---");
            boolean hasTasks = false;
            while (resultSet.next()) {
                hasTasks = true;
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                System.out.printf("%d. %s\n", id, description);
            }
            if (!hasTasks) {
                System.out.println("Henüz hiçbir görev eklenmedi.");
            }

        } catch (SQLException e) {
            System.err.println("Görevleri listeleme hatası: " + e.getMessage());
        }
    }

    // Görev silme
    private static void deleteTask(Connection connection, int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Görev başarıyla silindi!");
            } else {
                System.out.println("Silinecek görev bulunamadı.");
            }
        } catch (SQLException e) {
            System.err.println("Görev silme hatası: " + e.getMessage());
        }
    }
}


