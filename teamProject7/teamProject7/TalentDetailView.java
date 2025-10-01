import java.util.List;

public class TalentDetailView implements Observer {
    // TalentSearcher와 동일한 List<RentalTalant>를 받지만, 여기서는 상세 정보 확인용으로 사용

    @Override
    public void update(List<RentalTalant> result) {
        if (result.isEmpty() || result.size() != 1) {
            // 상세 정보는 한 건에 대해서만 출력하는 것이 일반적
            System.out.println("상세 정보를 출력할 재능을 찾을 수 없습니다.");
            return;
        }
        
        RentalTalant talant = result.get(0);

        System.out.println("\n--- 재능 상세 정보 ---");
        System.out.println("재능 이름: " + talant.getName());
        System.out.println("소유자: " + talant.getOwner());
        System.out.println("-------------------------");
        System.out.println("상세 설명: " + talant.getDescription()); // 상세 정보 출력
        System.out.println("대여료: " + talant.getRentalFee() + "원"); // 대여료 출력
        
        String rentalStatus = talant.isRented() ? 
            "대여 중 (대여자: " + talant.getBorrower() + ")" : 
            "대여 가능";
        System.out.println("대여 상태: " + rentalStatus); // 대여 상태 출력
        System.out.println("-------------------------");
    }
}