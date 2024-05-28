# Tetris
使用 **Java 的 GUI 工具包 Swing** 所做的簡易俄羅斯方塊

## 遊戲操作
* `Space`： 方塊直接下降 （**hard drop**）
*  `←,  →`： 方塊左、右移動 
*  `A,  D`： 方塊左旋和右旋
*  `   ↓   `： 方塊快速下降 （**soft drop**）
## 遊戲畫面
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*遊戲初始畫面(1)*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*遊戲初始畫面(2)*   
<img src="https://github.com/MingMinNa/Tetris/blob/v2/img/game_display/HomeScreen_display(1).png" alt="HomeScreen_display(1).png" width="200" height="200">
<img src="https://github.com/MingMinNa/Tetris/blob/v2/img/game_display/HomeScreen_display(2).png" alt="HomeScreen_display(2).png" width="200" height="200">  

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*GameState1*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*GameState2*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;GameState3*  
<img src="https://github.com/MingMinNa/Tetris/blob/v2/img/game_display/GameScreen_State1.png" alt="GameScreen_State1.png" width="200" height="200">
<img src="https://github.com/MingMinNa/Tetris/blob/v2/img/game_display/GameScreen_state2.png" alt="GameScreen_state2.png" width="200" height="200">
<img src="https://github.com/MingMinNa/Tetris/blob/v2/img/game_display/GameScreen_State3.png" alt="GameScreen_State3.png" width="200" height="200">  
註：當分數達到 500 分時，進入 State2； 當分數達到 1000 分時，進入 State3

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*GameOver*&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*GameClear*  
<img src="https://github.com/MingMinNa/Tetris/blob/v2/img/game_display/GameOver.png" alt="GameOver.png" width="200" height="200">
<img src="https://github.com/MingMinNa/Tetris/blob/v2/img/game_display/GameClear.png" alt="GameClear.png" width="200" height="200">

### 注意事項
1. 若要執行此程式，請先下載 Jlayer(用於播放 mp3)，並匯入。__[ 在做此程式時，所下載的版本為 Jlayer1.0.1 ]__ 
2. 由於怕把遊戲音樂有版權問題，所以我並沒有把音樂放上來，請先自行尋找 5 個音檔( mp3格式)，並將其依照 GameMusic.txt 裡面所述重新命名，再放進 sound 資料夾內。
