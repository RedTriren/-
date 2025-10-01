public class RentalTalant {
    private String name;
    private String owner;
    private String description;
    private int rentalFee; 
    private boolean isRented; 
    private String borrower;  

    // 등록 시 생성자 (상세 정보 및 대여료 포함)
    public RentalTalant(String name, String owner, String description, int rentalFee) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.rentalFee = rentalFee;
        this.isRented = false;
        this.borrower = null;
    }

    // DB 조회 시 사용될 전체 필드 포함 생성자
    public RentalTalant(String name, String owner, String description, int rentalFee, boolean isRented, String borrower) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.rentalFee = rentalFee;
        this.isRented = isRented;
        this.borrower = borrower;
    }
    
    // Getter 메서드 추가
    public String getName(){ return name; }
    public String getOwner(){ return owner; }
    public String getDescription(){ return description; }
    public int getRentalFee(){ return rentalFee; }     
    public boolean isRented(){ return isRented; }      
    public String getBorrower(){ return borrower; }    
}