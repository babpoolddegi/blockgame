Block Break Game<br><br>

1. 게임 설명<br>
 - 벽돌깨기 게임<br>
  : 공을 튕겨서 벽돌에 닿으면 벽돌이 없어지고 모든 벽돌이 없어지면 끝나는 게임<br><br>
  ![image](https://user-images.githubusercontent.com/114123375/198939927-5526b099-8896-4c77-a73e-19bdf514d811.png)<br><br>
 - 게임개요<br>
 ![image](https://user-images.githubusercontent.com/114123375/198938842-6d081049-ccb0-4b39-932d-f20b070e845a.png)<br>
 
 ① 공이 움직이는 방향(direction=dir)을 총 4방향으로 지정<br>
 ② 바는 방향키 ( ←, → )로 움직임(keyListener)<br>
 ③ 공(ball)이 각각 벽돌(block)과 바(bar)에 부딪치면 튕겨져 나오도록(충돌) 설계<br>
 ④ 공이 벽돌과 부딪치면 벽돌이 사라짐(hidden)<br>
 ⑤ 벽돌이 모두 사라지면 게임 재시작됨<br><br>
 
 2. 프로그래밍 설명<br>
  - 상수, 변수 설정 : 디테일하게 값을 설정하여 적용과 수정이 용이하게 작업<br>
  - 공, 벽돌, 바의 위치 지정  <br><br>
// *ball -> int x = CANVAS_WIDTH/2 - BALL_WIDTH/2; <br>
![image](https://user-images.githubusercontent.com/114123375/199013276-0d6015cf-47ba-4e9d-9162-9b325cf53e6a.png)<br><br>
// *ball -> Point getCenter<br>
![image](https://user-images.githubusercontent.com/114123375/199023937-4cc989a5-7d0c-4d10-a5c0-4e0b0435f45d.png)<br><br>

  - piant, draw 메서드 사용
  - 벽돌, string, 공, 바 그려주기<br>
 // java-draw 기능 참고 <br>
 // https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=heoguni&logNo=130169579359<br>
  - 프레임창 세팅 : initData(); -> block값 초기화
  - 방향키 입력으로 바가 움직일 수 있게 설정<br>
   : barXTarget -> 왼쪽(-), 오른쪽(+) 값 지정/ 값 늘리면 반응속도가 더 빨라지는 것을 볼 수 있음<br>
   : barXTarger=bar.x; -> 해당 값 없으면 뚝뚝 끊기면서 이동<br>
  - game running time
  - 게임 끝나는 상황 설정
  - 게임 재시작 상황 설정
  - 공의 움직임 설정
  - ( 벽&바 : 공 )에 대한 충돌 처리<br>
  ![image](https://user-images.githubusercontent.com/114123375/199016846-2ef58d26-e9ea-4173-b419-4181d0bc442e.png)<br><br>
   : dir=0일때, 공이 위로 향하는 값이기 때문에 바에 부딪치지 않으므로 bar에 대한 값은 none<br>
  - ( 벽돌 : 공 )에 대한 충돌 처리<br>
   : 벽돌의 양 옆, 위, 아래 충돌값 각각 지정<br>
   : 각 벽돌에 공이 충돌하여 벽돌이 사라졌을 때, 벽돌별 스코어를 지정하여 스코어가 올라가게 지정<br>
  ![image](https://user-images.githubusercontent.com/114123375/199023176-0e19f130-f7c0-4818-b731-75b46cc700df.png)<br><br>

  
  3. 어려웠던 점<br>
   - 처음에 지정된 x,y축의 +,-값 이해를 못해서 해매다가 검색을 통해 개념 이해했음<br>
   - 코드가 워낙 복잡하고 수학적 계산이 필요해서 어려웠음<br>
   - 발표 준비 중 해당 코드들에 대한 설명을 하려니 이해한 부분이 많이 없어서 다시 공부 다하느라 시간이 오래걸림<br>
   - 재시작 구현 과정이 길고 어려웠음<br><br>

  4. 아쉬운 점<br>
   - 버튼을 누르면 재시작이 되게끔 설정하고 싶었지만 개념이 또 달라서 시간관계상 재시작구현한 것으로 만족해야 했음<br>
   - 게임이 끝나고 재시작 될 때 시간적 여유가 있게 설정하고 싶었으나 구현하지 못함<br><br>
  
  5. 추가 구현한 부분 <br>
  - 게임이 끝나도 (벽돌이 모두 사라져도) 공이 계속 튕기면서 움직임<br>
   --> timerStop(); 함수로 이미지와 같이 화면이 멈추게만 표현(마지막 벽돌을 깬 순간)<br>
 ![image](https://user-images.githubusercontent.com/114123375/199038655-c4e1d9a7-dc3e-42a4-9687-d5059cc2daae.png)<br>
  --> 게임 재시작을 구현해보고 싶었음<br>
  --> 처음부터 코드를 밤새도록 뜯어서 재시작(벽돌 새로 생성, 공 위치 및 방향 초기화, 스코어 초기화) 할 수 있게 변경<br><br>
  - 오른쪽 벽 충돌 시 이미지와 같이 공이 바깥으로 더 나가보임<br>
 ![image](https://user-images.githubusercontent.com/114123375/199039963-c32c36cd-580e-4dc2-8eaf-77237da5fd4d.png)<br>
    --> 공이 튕겨져 나오는 위치 값을 조금 더 왼쪽으로 옮겨줌( 값 - )<br><br>
    
   6. 자바 프로그래밍 게임 개발 프로젝트 작업 후기<br>
   - 처음 아이디어는 5X5 빙고 게임 개발<br>
   - 아직도 이해 못하고 어려운 코드이지만 보람이 느껴지는 작업<br>
   - 마지막이 재시작 구현하였을 때 엄청난 희열을 느낌<br><br><br>


[출처] : 유투브 코딩강사 - 자바 벽돌깨기 게임(Java Block break game) 1~4<br>
// https://www.youtube.com/watch?v=wWKl-82svhg<br>
--> 같이 필요한 클래스와 메서드를 생성해나가면서 차근차근 같이 게임 만들어 나갈 수 있는 좋은 기회<br>
--> 다른 게임 종류도 많고 양질의 영상이 많음<


