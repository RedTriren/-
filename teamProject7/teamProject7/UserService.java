public class UserService {
    private UserDAO userDAO = new UserDAO();
    private User currentUser = null;

    public boolean register(String username, String password){
        return userDAO.register(new User(username, password));
    }

    public boolean login(String username, String password){
        User user = userDAO.login(username, password);
        if(user != null){
            currentUser = user;
            System.out.println("로그인 성공: " + user.getUsername());
            return true;
        }
        System.out.println("로그인 실패");
        return false;
    }

    public void logout(){
        if(currentUser != null){
            System.out.println("로그아웃: " + currentUser.getUsername());
            currentUser = null;
        } else {
            System.out.println("현재 로그인 상태가 아님");
        }
    }

    public void deleteAccount(){
        if(currentUser != null){
            if(userDAO.delete(currentUser.getUsername())){
                System.out.println("회원탈퇴 완료");
                currentUser = null;
            }
        } else {
            System.out.println("로그인 먼저 필요");
        }
    }

    public User getCurrentUser(){
        return currentUser;
    }
}
