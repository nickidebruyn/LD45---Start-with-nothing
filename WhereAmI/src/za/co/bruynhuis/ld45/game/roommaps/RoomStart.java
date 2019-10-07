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
public class RoomStart {

    public static String generateRandomRoom() {
        String roomStr = null;
        int index = FastMath.nextRandomInt(0, 1);

        if (index == 0) {
            roomStr = "####W####  ####W####\n"
                    + "#          #   L   #\n"
                    + "#          #       #\n"
                    + "#          #       #\n"
                    + "T          ####D####\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#      X     X     #\n"
                    + "#                  #\n"
                    + "                    \n"
                    + "                    \n"
                    + "#                  #\n"
                    + "#      X     X     #\n"
                    + "#                  #\n"
                    + "W                  W\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#                ###\n"
                    + "#k               # #\n"
                    + "#########  ###T#####";

        } else if (index == 1) {
            roomStr = "####W####  ####W####\n"
                    + "#   L          L   #\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "T                  #\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#      X     X     #\n"
                    + "#                  #\n"
                    + "                    \n"
                    + "                    \n"
                    + "#                  #\n"
                    + "#      X     X     #\n"
                    + "#                  #\n"
                    + "WL                 W\n"
                    + "#                  #\n"
                    + "#                  #\n"
                    + "#                ###\n"
                    + "#                # #\n"
                    + "#########  ###T#####";

        }

        return roomStr;
    }
}
