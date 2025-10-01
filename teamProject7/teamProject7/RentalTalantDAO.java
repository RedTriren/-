import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalTalantDAO {

    public RentalTalantDAO() {
        // 기존 테이블이 있다면 삭제하고 새 테이블을 생성하는 로직
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 기존 talants 테이블이 있다면 삭제
            stmt.execute("DROP TABLE IF EXISTS talants");
            
            // 'talants' 테이블을 새로운 스키마로 생성
            stmt.execute("CREATE TABLE IF NOT EXISTS talants (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "owner TEXT NOT NULL, " +
                    "description TEXT, " +       // 상세 설명
                    "rental_fee INTEGER, " +     // 대여료
                    "is_rented BOOLEAN DEFAULT 0, " + // 대여 상태
                    "borrower TEXT NULL)");      // 대여한 사용자
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 재능 등록
    public boolean addItem(RentalTalant talant){
        String sql = "INSERT INTO talants(name, owner, description, rental_fee) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, talant.getName());
            pstmt.setString(2, talant.getOwner());
            pstmt.setString(3, talant.getDescription()); 
            pstmt.setInt(4, talant.getRentalFee());    
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("재능 등록 실패: " + e.getMessage());
            return false;
        }
    }
    
    // 전체 필드 조회용 쿼리
    private static final String SELECT_ALL_FIELDS = "SELECT name, owner, description, rental_fee, is_rented, borrower FROM talants ";

    // 모든 재능 목록 조회 또는 검색
    public List<RentalTalant> findItems(String keyword){
        List<RentalTalant> talants = new ArrayList<>();
        String sql = SELECT_ALL_FIELDS;
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " WHERE name LIKE ? OR description LIKE ?";
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (keyword != null && !keyword.trim().isEmpty()) {
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                talants.add(new RentalTalant(
                        rs.getString("name"),
                        rs.getString("owner"),
                        rs.getString("description"),
                        rs.getInt("rental_fee"),
                        rs.getBoolean("is_rented"),
                        rs.getString("borrower")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return talants;
    }
    
    // 재능 상세 정보 조회
    public RentalTalant findTalantByName(String name){
        String sql = SELECT_ALL_FIELDS + " WHERE name=?";
         try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return new RentalTalant(
                    rs.getString("name"),
                    rs.getString("owner"),
                    rs.getString("description"),
                    rs.getInt("rental_fee"),
                    rs.getBoolean("is_rented"),
                    rs.getString("borrower")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 재능 대여
    public boolean rent(String talantName, String borrower){
        String sql = "UPDATE talants SET is_rented=1, borrower=? WHERE name=? AND is_rented=0"; 
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, borrower);
            pstmt.setString(2, talantName);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 재능 대여 종료 이후
    public boolean returnTalant(String talantName, String borrower){
        String sql = "UPDATE talants SET is_rented=0, borrower=NULL WHERE name=? AND borrower=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, talantName);
            pstmt.setString(2, borrower);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}