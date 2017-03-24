# Class structure

One of the changes we implement is to make the code truly object-oriented. This means separate classes for the various game objects, while maintaining an overall Game class to contain the timer and drawing objects, and ensuring data encapsulation throughout.

The structure of these classes is given below:

* Game **extends** JPanel **implements** KeyListener, ActionListener
* Pad **extends** Rectangle2D.Double
* PlayerPad **extends** Pad
* AIPAD **extends** Pad
* Ball **extends** Ellipse2D.Double
* Scores

The additional fields and methods contained in each class are given below (we do not list inherited data structures):

| **Class** | **Fields and Methods** | **Description** |
| --- | --- | --- |
| Game | int height <br /> int width <br /> timer t <br /> HashSet(String) keys <br /> boolean first | |
| Pad | int SPEED <br /> int padH <br /> int padW <br /> int padX | int getPadH() <br /> int getPadW() <br /> int getPadX() <br /> void setPadH(int padH) <br /> void setPadW(int padW) <br /> void setPadX(int padX) |
| PlayerPad | | void updatePadX(HashSet(String) keys) |
| AIPad | | void updatePadX(double ballX) |
| Ball | double ballX <br /> double ballY <br /> double velX <br /> double velY <br /> double ballSize <br /> void updatePos() <br /> void detectLRCollision(int frameWidth) <br /> string detectTBCollision(int frameHeight) <br /> void detectPlayerPadCollision(PlayerPad P, int inset) <br /> void detectAIPadCollision(AIPad P, int inset) | <br /> <br /> <br /> <br /> <br /> Advances one frame by updating ball position, using velX, velY. <br /> Detects collision with left and right walls and updates velX. <br /> Detects collision with top and bottom walls, updates velY and returns a string to indicate whether top or bottom wall has been collided. <br /> Detects collision with player pad and updates velY. <br /> Detects collision with AI pad and updates velY.
| Scores | int scoreTop <br /> int scoreBottom | void updateScores(double ballY) |

Actually, this table is somewhat misleading because some of the fields and methods listed above are inherited from the superclass. We list the relevant data in the table below:

| **Class** | **Fields** | **Methods** |
| --- | --- | --- |
| Rectangle2D.Double | | |
| Ellipse2D.Double | double x (replaces ballX) <br /> double y (replaces ballY) <br /> double height, double width (replace ballSize) | double getX() (replaces getBallX()) <br /> double getY() (replaces getBallY()) |

Putting these two tables together, we arrive at a table indicating all the *new* data which must be included within the subclass definition.

| **Class** | **Fields** | **Methods** |
| --- | --- | --- |
| Game | int height <br /> int width <br /> timer t <br /> HashSet(String) keys <br /> boolean first | |
| Pad | int SPEED <br /> int padH <br /> int padW <br /> int padX | int getPadH() <br /> int getPadW() <br /> int getPadX() <br /> void setPadH(int padH) <br /> void setPadW(int padW) <br /> void setPadX(int padX) |
| PlayerPad | | void updatePadX(HashSet(String) keys) |
| AIPad | | void updatePadX(double ballX) |
| Ball | double velX <br /> double velY | double getVelX() <br /> double getVelY() <br /> void setBallX(double ballX) <br /> void setBallY(double ballY) <br /> void setVelX(double velX) <br /> void setVelY(double velY) |
| Scores | int scoreTop <br /> int scoreBottom | void updateScores(double ballY) |

Finally, we will also require new constructors for many of the classes.