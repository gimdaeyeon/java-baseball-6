package baseball.domain;


import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.util.*;

public class Player {

    private final int NUMBER_OF_DIGITS = 3;
    private final String ERROR_MESSAGE ="입력값이 잘못되었습니다";
    public final PlayerType playerType;
    private final List<Integer> numberList;
    private final String HUMAN_CREATE_NUMBER_MESSAGE = "게임에서 사용할 숫자를 입력해주세요";
    private final String HUMAN_MAKE_ANSWER_MESSAGE = "숫자를 입력해주세요: ";


    public Player(PlayerType playerType) {
        this.playerType = playerType;
        this.numberList = initNumberList(playerType);
    }

    private List<Integer> initNumberList(PlayerType playerType){
        return switch (playerType){
//            case HUMAN-> createHumansNumberList();
            case HUMAN-> List.of(1,2,3);
            case COMPUTER-> createComputersNumberList();
        };
    }


//    사람의 숫자 셋팅
    public List<Integer> createHumansNumberList(){
//        printGuideMessage();
        String numberStr = Console.readLine();


        if(!verifyInputValue(numberStr)){
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        return numberToList(Integer.parseInt(numberStr));
    }

    private void printGuideMessage(){
        if(this.numberList== null){
            System.out.println(HUMAN_CREATE_NUMBER_MESSAGE);
            return ;
        }
        System.out.print(HUMAN_MAKE_ANSWER_MESSAGE);
    }


//  해당 메소드는 게임 중 입력값을 검증할 때도 필요 할 수 있으니 public으로 선언
    public boolean verifyInputValue(String numberStr){
        return isThreeDigits(numberStr) && isNumbers(numberStr) && isDuplicate(numberStr);
    }

//    사용자가 입력한 값이 3자리 수인지 검증
    private boolean isThreeDigits(String numberStr){
        return numberStr.length() == NUMBER_OF_DIGITS;
    }

//    입력한 값이 숫자인지 검증
    private boolean isNumbers(String numberStr){
        return numberStr.matches("^[1-9]+$");
    }

//    중복된 값이 있는지 검증
    private boolean isDuplicate(String numberStr){
        int length = numberStr.length();
        Set<Character> characterSet = new HashSet<>();

        for(int i = 0; i<numberStr.length(); i++){
            characterSet.add(numberStr.charAt(i));
        }
        return characterSet.size() == length;
    }

//    입력받은 숫자를 리스트로 반환
    private List<Integer> numberToList(int number){
        List<Integer> list = new ArrayList<Integer>();
        while(number != 0) {
            list.add(0,number%10);
            number/=10;
        }
        return list;
    };



//    컴퓨터의 숫자 셋팅
    private List<Integer> createComputersNumberList(){
        List<Integer> computer = new ArrayList<>();
        while (computer.size() < NUMBER_OF_DIGITS) {
            int randomNumber = Randoms.pickNumberInRange(1, 9);
            System.out.println(randomNumber);
            if (!computer.contains(randomNumber)) {
                computer.add(randomNumber);
            }
        }
        return computer;
    }

//    외부에서 들어온 숫자(다른 사용자가 추측한 답)와 해당 사용자의 답을 비교
    public List<Integer> compareAnswers(List<Integer> otherPlayersAnswer){
        return calculateStrikesAndBalls(this.numberList, otherPlayersAnswer);
    }

//    두개의 리스트를 비교해 스트라이크, 볼, 아웃을 비교
    private List<Integer> calculateStrikesAndBalls(List<Integer> thisList, List<Integer> otherList){
        int strike = 0;
        int ball = 0;
        int out = 0;

        for(int i =0; i < thisList.size();i++){
            if(!thisList.contains(otherList.get(i))){
                out++;
                continue;
            }
            if(Objects.equals(thisList.get(i), otherList.get(i))){
                strike++;
                continue;
            }
            ball++;
        }
        return List.of(strike, ball, out);
    }



    @Override
    public String toString() {
        return numberList.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(numberList, player.numberList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberList);
    }
}
