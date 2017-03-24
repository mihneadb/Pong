# Class structure

One of the changes we implement is to make the code truly object-oriented. This means separate classes for the various game objects, while maintaining an overall Game class to contain the timer and drawing objects, and ensuring data encapsulation throughout.

The structure of these classes is given below:

* Game **extends** JPanel **implements** KeyListener, ActionListener
* Pad **extends** Rectangle2D.Double
* PlayerPad **extends** Pad
* AIPad **extends** Pad
* Ball **extends** Ellipse2D.Double
* Scores

The additional fields and methods contained in each class are given below (we do not list inherited data structures):

## Game
| **Field** | **Description** |
| --- | --- |
|int height | Height of the frame. |
|int width | Width of the frame. |
|timer t | Timer which triggers the game loop. |
|HashSet(String) keys | Database of strings keeping track of which keys are pressed. |
|boolean first | Boolean keeping track of whether or not we are at the start of the game.|

| **Method** | **Description** |
| --- | --- |

## Pad
| **Field** | **Description** |
| --- | ---|
| int SPEED | Speed of movement. |
| int padH | Height of the pad. |
| int padW | Width of the pad. |
| int padX | Horizontal position of the pad. |
| int padY | Vertical position of the pad. |

| **Method** | **Description** |
| --- | --- |

### PlayerPad (extends Pad)
| **Field** | **Description** |
| --- | --- |

| **Method** | **Description** |
| --- | --- |
| void updatePadX(HashSet(String) keys) | Updates the position of the pad, according to which keys are pressed.|

### AIPAD (extends Pad)
| **Field** | **Description** |
| --- | --- |

| **Method** | **Description** |
| --- | --- |
| void updatePadX(double ballX) | Updates the position of the pad, according to the horizontal position of the ball.|

## Ball
| **Field** | **Description** |
| --- | --- |
| double ballX | Horizontal position of ball. |
| double ballY | Vertical position of ball. |
| double ballSize | Size (diameter) of ball. |
| double velX | Horizontal velocity of ball. |
| double velY | Vertical velocity of ball. |

| **Method** | **Description** |
| --- | --- |
| void updatePos() | Advances one frame by updating ball position, using velX, velY. |
| void detectLRCollision(int frameWidth) | Detects collision with left and right walls and updates velX. |
| string detectTBCollision(int frameHeight) | Detects collision with top and bottom walls, updates velY and returns a string to indicate which of the walls was collided with. |
| void detectPlayerPadCollision(PlayerPad P, int inset) | Detects collision with player's pad (bottom pad) and updates velY. |
| void detectAIPadCollision(AIPad P, int inset) | Detects collision with AI's pad (top pad) and updates velY. |
| void resetState(double ballX, double ballY, double ballSize, double velX, double velY) | Resets all the object's fields to the inputted values. |

## Scores
| **Field** | **Description** |
| --- | --- |
| int scoreBottom | Player's score |
| int scoreTop | AI's score |

| **Method** | **Description** |
| --- | --- |
| void bottomScores() | Increase scoreBottom by 1. |
| void topScores() | Increase scoreTop by 1. |
| int getScoreBottom() | Return scoreBottom. |
| int getScoreTop() | Return scoreTop. |

Actually, this table is somewhat misleading because some of the fields and methods listed above are inherited from the superclass. We list the relevant data in the table below:

## Rectangle2D.Double
| **Field** | **Acts as** |
| --- | --- |

| **Method** | **Acts as** |
| --- | --- |

## Ellipse2D.Double
| **Field** | **Acts as** |
| --- | --- |
| double x | ballX |
| double y | ballY |
| double height | ballSize |
| double width | ballSize |

| **Method** | **Acts as** |
| --- | --- |

Putting these two tables together, we arrive at a table indicating all the *new* data which must be included within the subclass definition.

[TODO]

Finally, we will also require new constructors for many of the classes.