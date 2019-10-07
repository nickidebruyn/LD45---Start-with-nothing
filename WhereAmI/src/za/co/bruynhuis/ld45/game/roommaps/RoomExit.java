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
public class RoomExit {

    public static String generateRandomRoom() {
        String roomStr = null;
        int index = FastMath.nextRandomInt(0, 1);

        if (index == 0) {
            roomStr = "#########  #########\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "T                  T\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#       X  X       #\n"
                    + "#                  #\n"
                    + "                    \n"
                    + "                    \n"
                    + "#                  #\n"
                    + "#       X  X       #\n"
                    + "#                  #\n"
                    + "T                  #\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#                 Ge\n"
                    + "#                  #\n"
                    + "####T####  ####T#####";

        } else if (index == 1) {
            roomStr = "#########  #########\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "T                  T\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#       X  X       #\n"
                    + "#                  #\n"
                    + "                    \n"
                    + "                    \n"
                    + "#                  #\n"
                    + "#       X  X       #\n"
                    + "#                  #\n"
                    + "T                  #\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#   G              #\n"
                    + "####E####  ####T#####";

        }
        
        return roomStr;
    }
}
