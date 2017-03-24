# Class structure

One of the changes we implement is to make the code truly object-oriented. This means separate classes for the various game objects, while maintaining an overall Game class to contain the timer and drawing objects.

The structure of these classes is given below:

* Game **extends** JPanel **implements** KeyListener, ActionListener
* Pad **extends** Rectangle2D
* PlayerPad **extends** Pad
* AIPAD **extends** Pad
* Ball **extends** Ellipse2D

The additional variables and methods contained in each class are:

| **Class** | **Variables** | **Methods** |
| --- | --- | --- |
| Game | int height <br /> int width <br /> timer t // HashSet<String> keys // boolean first | |