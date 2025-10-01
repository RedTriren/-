import java.util.List;
import java.util.Scanner;

public class ShowBasic {
    UserService userService = new UserService();
    RentalService rentalService = new RentalService();
    
    // 재능 검색 및 상세 보기를 위한 옵저버 패턴 객체
    TalentSearcher searcher = new TalentSearcher();
    TalentResultView resultView = new TalentResultView();
    TalentDetailView detailView = new TalentDetailView(); 
    
    Scanner sc = new Scanner(System.in);

    // ShowBasic 생성자: 목록 보기 옵저버는 기본으로 등록
    public ShowBasic() {
        searcher.addObserver(resultView);
    }

    public void run() {
        while (true) {
            User currentUser = userService.getCurrentUser();
            int choice = -1;

            if (currentUser == null) {
                // 로그아웃 상태 메뉴
                choice = displayAndGetLogoutMenu();
                if (choice == 0) return;
                handleLogoutMenu(choice);
            } else {
                // 로그인 상태 메뉴
                choice = displayAndGetLoginMenu(currentUser.getUsername());
                if (choice == 0) return;
                handleLoginMenu(choice, currentUser);
            }
        }
    }
    
    // ====================== 메뉴 표시 및 입력 처리 ======================

    private int displayAndGetLogoutMenu() {
        System.out.println("\n===  메뉴 (로그인 필요) ===");
        System.out.println("1. 회원가입");
        System.out.println("2. 로그인");
        System.out.println("6. 물품 검색/목록");
        System.out.println("0. 종료");
        System.out.print("선택: ");
        return readIntInput();
    }

    private int displayAndGetLoginMenu(String username) {
        System.out.println("\n=== 메뉴 (사용자: " + username + ") ===");
        System.out.println("3. 로그아웃");
        System.out.println("4. 회원탈퇴");
        System.out.println("--- 재능 관리 ---");
        System.out.println("5. 재능 등록");
        System.out.println("6. 재능 검색/목록");
        System.out.println("7. 재능 대여");
        System.out.println("8. 재능 반납");
        System.out.println("0. 종료");
        System.out.print("선택: ");
        return readIntInput();
    }
    
    private int readIntInput() {
        while (true) {
            if (sc.hasNextInt()) {
                int choice = sc.nextInt();
                sc.nextLine(); // 버퍼 비우기
                return choice;
            } else {
                System.out.println("잘못된 입력입니다.");
                sc.nextLine(); // 잘못된 문자열 비우기
            }
        }
    }

    // ====================== 메뉴 핸들러 (로그아웃 상태) ======================

    private void handleLogoutMenu(int choice) {
        switch (choice) {
            case 1: 
                registerProcess();
                break;
            case 2: 
                loginProcess();
                break;
            case 6: // 물품 검색
                searchTalentProcess();
                break;
            default:
                System.out.println("잘못된 선택입니다");
        }
    }

    // ====================== 메뉴 핸들러 (로그인 상태) ======================

    private void handleLoginMenu(int choice, User currentUser) {
        switch (choice) {
            case 3: 
                userService.logout();
                break;
            case 4: 
                userService.deleteAccount();
                break;
            case 5: 
                registerTalentProcess(currentUser.getUsername());
                break;
            case 6: 
                searchTalentProcess();
                break;
            case 7: // 재능 대여
                rentTalentProcess(currentUser.getUsername());
                break;
            case 8: // 재능 반납
                returnTalantProcess(currentUser.getUsername());
                break;
            default:
                System.out.println("잘못된 선택입니다");
        }
    }

    // ====================== 기능 메서드 분리 ======================

    private void registerProcess() {
        System.out.print("아이디: ");
        String regId = sc.nextLine();
        System.out.print("비밀번호: ");
        String regPw = sc.nextLine();
        userService.register(regId, regPw);
    }

    private void loginProcess() {
        System.out.print("아이디: ");
        String id = sc.nextLine();
        System.out.print("비밀번호: ");
        String pw = sc.nextLine();
        userService.login(id, pw);
    }

    private void registerTalentProcess(String owner) {
        System.out.print("등록할 재능 이름: ");
        String name = sc.nextLine();
        System.out.print("재능 상세 설명: ");
        String description = sc.nextLine();
        System.out.print("대여료(원): ");
        int rentalFee = readIntInput();
        
        rentalService.addItem(name, owner, description, rentalFee);
    }
    
    private void searchTalentProcess() {
    System.out.print("재능 검색 (전체 목록은 Enter): ");
    String keyword = sc.nextLine();
    
    // 목록 출력을 위해 resultView만 사용
    searcher.removeObserver(detailView);
    //searcher.addObserver(resultView); 
    
    searcher.search(keyword.trim());
    
    // 검색 결과가 있으면 후처리 메뉴 호출
    List<RentalTalant> currentResults = searcher.getSearchResult();
    if (!currentResults.isEmpty()) {
        handleSearchResultOptions(currentResults);
    }
}

    private void rentTalentProcess(String borrower) {
        System.out.print("대여할 재능 이름: ");
        String name = sc.nextLine();
        rentalService.rent(name, borrower);
    }
    
    private void returnTalantProcess(String borrower) {
        System.out.print("반납할 재능 이름: ");
        String name = sc.nextLine();
        rentalService.returnTalant(name, borrower);
    }

    private void handleSearchResultOptions(List<RentalTalant> results) {
    while (true) {
        // =======================================================
        // 1. 재능 번호 입력 (목록은 이미 TalentResultView가 출력함)
        // =======================================================
        System.out.print("\n▶ 상세 보기/대여를 원하는 재능의 번호를 입력하세요 (취소: 0): ");
        int choice = readIntInput();
        
        if (choice == 0) return;
        int index = choice - 1;
        
        if (index >= 0 && index < results.size()) {
            RentalTalant selectedTalant = results.get(index);
            // =======================================================
            // 2. 해당 재능에 대한 상세 정보 출력 및 옵션 선택
            // =======================================================
            System.out.println("\n--- 선택된 재능: " + selectedTalant.getName() + " 상세 정보 ---");
            System.out.println("소유자: " + selectedTalant.getOwner());
            System.out.println("설명: " + selectedTalant.getDescription());
            System.out.println("대여료: " + selectedTalant.getRentalFee() + "원");
            System.out.println("상태: " + (selectedTalant.isRented() ? "[대여 중]" : "[대여 가능]"));
            // =======================================================
            // 3. 상세 정보 확인 후 대여 여부 질문 추가
            // =======================================================
            if (selectedTalant.isRented()) {
                System.out.println("\n이 재능은 현재 대여 중이므로 대여할 수 없습니다.");
            } else {
                System.out.print("\n 이 재능을 대여하시겠습니까? (예: Y / 아니오: N) ");
                String rentConfirm = sc.nextLine().trim().toUpperCase();

                if (rentConfirm.equals("Y")) {
                    User currentUser = userService.getCurrentUser();
                    if (currentUser != null) {
                        rentalService.rent(selectedTalant.getName(), currentUser.getUsername());
                    } else {
                        System.out.println("대여를 위해서는 로그인이 필요합니다. 메인 메뉴로 돌아갑니다.");
                    }
                } else if (rentConfirm.equals("N")) {
                    System.out.println("대여를 취소하고 목록 선택으로 돌아갑니다.");
                } else {
                    System.out.println("잘못된 입력입니다. 대여를 취소하고 목록 선택으로 돌아갑니다.");
                }
            }
            return;
            
        } 
        else {
            System.out.println("잘못된 재능 번호입니다. 목록에 있는 번호를 다시 확인해 주세요.");
            }
        }
    }
}
