public class RentalService {
    // RentalTalantDAO를 사용하여 DB에 접근
    private RentalTalantDAO talantDAO = new RentalTalantDAO();

    /**
     * 재능을 등록하는 기능(상세 설명 및 대여료 추가)
     * @param talantName 재능 이름
     * @param owner 등록자 ID
     * @param description 재능 상세 설명
     * @param rentalFee 대여료
     */
    public void addItem(String talantName, String owner, String description, int rentalFee){
        RentalTalant newTalant = new RentalTalant(talantName, owner, description, rentalFee);
        if(talantDAO.addItem(newTalant)) {
            System.out.println("등록 완료: " + talantName + " (소유자: " + owner + ")");
        }
    }

    /**
     * 재능 검색 기능을 담당 << 옵저버 패턴 사용
     * 실제 검색은 TalentSearcher에서 DAO를 통해 수행
     * 결과 출력은 TalentResultView에서 담당
     * @param searcher TalentSearcher 객체
     */
    public void showAllItems(TalentSearcher searcher){
        System.out.println("전체 등록된 재능 목록을 조회합니다.");
        // null 키워드로 검색 시 전체 목록 조회
        searcher.search(null); 
    }

    /**
     * 재능을 대여하는 기능입니다. (소유권 이전 없이 대여자만 변경)
     * @param talantName 대여할 재능 이름
     * @param borrower 대여할 사용자 ID
     */
    public void rent(String talantName, String borrower){
        // 대여 전 재능 정보를 DB에서 조회
        RentalTalant talant = talantDAO.findTalantByName(talantName);
        
        if (talant == null) {
            System.out.println("해당 재능을 찾을 수 없습니다.");
            return;
        }
        
        // 본인 재능 대여 방지
        if (talant.getOwner().equals(borrower)) {
            System.out.println("본인이 등록한 재능은 대여할 수 없습니다.");
            return;
        }

        // DB에서 대여 상태 업데이트
        if(talantDAO.rent(talantName, borrower)){
            System.out.println(talantName + " 대여 완료! 대여자: " + borrower);
            System.out.println("대여료: " + talant.getRentalFee() + "원");
        } else {
            System.out.println("대여에 실패했습니다. (이미 대여 중이거나 재능을 찾을 수 없습니다)");
        }
    }
    
    /**
     * 재능을 반납하는 기능
     * @param talantName 반납할 재능 이름
     * @param borrower 현재 대여 중인 사용자 ID
     */
    public void returnTalant(String talantName, String borrower){
        // DB에서 대여 상태를 '대여 가능'으로 업데이트
        if(talantDAO.returnTalant(talantName, borrower)){
            System.out.println(talantName + " 반납 완료.");
        } else {
            // 대여자가 일치하지 않거나, 해당 재능이 대여 중이 아닐 경우
            System.out.println("반납에 실패했습니다. (대여 목록에 없거나 잘못된 재능입니다)");
        }
    }
}