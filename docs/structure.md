# Class structure

One of the changes we implement is to make the code truly object-oriented. This means separate classes for the various game objects, while maintaining an overall Game class to contain the timer and drawing objects, and ensuring data encapsulation throughout.

The structure of these classes is given below:

* Game **extends** JPanel **implements** KeyListener, ActionListener
* Pad **extends** Rectangle2D
* PlayerPad **extends** Pad
* AIPAD **extends** Pad
* Ball **extends** Ellipse2D

The additional variables and methods contained in each class are given below (we do not list inherited data structures):

| **Class** | **Variables** | **Methods** |
| --- | --- | --- |
| Game | int height <br /> int width <br /> timer t <br /> HashSet(String) keys <br /> boolean first | |
| Pad | int SPEED <br /> int padH <br /> int padW <br /> int padX | int returnPadH() <br /> int returnPadW() <br /> int returnPadX <br /> void setPadH(int padH) <br /> void setPadW(int padW) <br /> void setPadX(int padX) |
| PlayerPad | | void updatePadX(HashSet(String) keys) |
| AIPad | | void updatePadX(double ballX) |
| Ball | double ballX <br /> double ballY <br /> double velX <br /> double velY <br /> double ballSize <br /> | double returnBallX() <br /> double returnBallY() <br /> double returnVelX() <br /> double returnVelY() <br /> void setBallX(double ballX) <br /> void setBallY(double ballY) <br /> void setVelX(double velX) <br /> void setVelY(double velY) |
| Scores | int scoreTop <br /> int scoreBottom | void updateScores(double ballY) |