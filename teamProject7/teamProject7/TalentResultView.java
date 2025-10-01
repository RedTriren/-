// TalentResultView.java (검색 결과에 번호 부여)

import java.util.List;

public class TalentResultView implements Observer {
    @Override
    public void update(List<RentalTalant> result) {
        System.out.println("\n--- 🔍 재능 검색 결과 ---");
        if (result.isEmpty()) {
            System.out.println("검색 결과가 없습니다.");
            return;
        }
        
        // 번호 매기기 추가
        for (int i = 0; i < result.size(); i++) {
            RentalTalant talant = result.get(i);
            // [번호]. 재능 이름 (소유자) [상태] 형식으로 출력
            String status = talant.isRented() ? "[대여 중]" : "[대여 가능]";
            System.out.println((i + 1) + ". " + talant.getName() 
                             + " (소유자: " + talant.getOwner() + ") " + status);
        }
        System.out.println("-------------------------");
    }
}