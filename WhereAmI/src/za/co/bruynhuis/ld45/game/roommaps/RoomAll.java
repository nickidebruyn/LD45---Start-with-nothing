/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.bruynhuis.ld45.game.roommaps;

import com.jme3.math.FastMath;

/**
 *
 * @author NideBruyn
 */
public class RoomAll {

    public static String generateRandomRoom() {
        String roomStr = null;
        int index = FastMath.nextRandomInt(0, 1);

        if (index == 0) {
            roomStr = "####W####  ####W####\n"
                    + "#   L          L   #\n"
                    + "#                  #\n"
                    + "#              M   #\n"
                    + "T   #          #   T\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "                    \n"
                    + "                    \n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "T                  T\n"
                    + "#   #          #   #\n"
                    + "#                  #\n"
                    + "#   X          X   #\n"
                    + "#                  #\n"
                    + "####T####  ####T#####";

        } else if (index == 1) {
            roomStr = "#####T###  ###T#####\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#   ##  ####  ##   #\n"
                    + "#   ##  ####  ##   #\n"
                    + "T        M         T\n"
                    + "#                  #\n"
                    + "#  ###T######T###  #\n"
                    + "#                  #\n"
                    + "                    \n"
                    + "                    \n"
                    + "#                  #\n"
                    + "#  ###T######T###  #\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#       ####       #\n"
                    + "#       ####       #\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "######T##  ##T######";

        }

        return roomStr;
    }
}
