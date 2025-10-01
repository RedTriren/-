import java.util.ArrayList;
import java.util.List;

public class TalentSearcher implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private RentalTalantDAO talantDAO = new RentalTalantDAO();
    private List<RentalTalant> searchResult = new ArrayList<>(); // 현재 검색 결과

    //현재 검색 결과를 외부에서 접근할 수 있도록
    public List<RentalTalant> getSearchResult() {
        return searchResult;
    }
    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(searchResult);
        }
    }

    //검색 실행 및 결과 업데이트
    public void search(String keyword) {
        this.searchResult = talantDAO.findItems(keyword);
        notifyObservers();
    }

}
