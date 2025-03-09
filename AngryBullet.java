/**
 * Angry Bullets
 * This project is a simple target shooting game.
 * @author Rojhat Delibaş
 */
import java.awt.event.KeyEvent;

public class AngryBullet {
    public static void main(String[] args) {
        // set canvas
        int canvas_width = 1600; // canvas width
        int canvas_height = 800; // canvas height
        StdDraw.setCanvasSize(canvas_width, canvas_height);
        StdDraw.setXscale(0, canvas_width); // scale canvas X
        StdDraw.setYscale(0, canvas_height); // scale canvas Y
        StdDraw.enableDoubleBuffering();


        // Game Parameters
        double gravity = 9.80665; // gravity
        double x0 = 120; // x and y coordinates of the bullet’s starting position on the platform
        double y0 = 120;
        double pst_x; // pst_x and y coordinates of the bullet's changing position wrt time
        double pst_y;
        double x0_same = 120; // x0_same and y the first coordinates of line between balls
        double y0_same =120;
        double bulletVelocity = 180; // initial velocity
        double bulletAngle = Math.toRadians(45.0); // initial angle
        int keyboardPauseDuration = 50;
        boolean is_not_Hitted = true; // for checking the ball hit the target or obstacle or ground

        // Box coordinates for obstacles and targets
        double[][] obstacleArray = {
                {1200, 0, 60, 220},
                {1000, 0, 60, 160},
                {600, 0, 60, 80},
                {600, 180, 60, 160},
                {220, 0, 120, 180}
        };
        double[][] targetArray = {
                {1160, 0, 30, 30},
                {730, 0, 30, 30},
                {150, 0, 20, 20},
                {1480, 0, 60, 60},
                {340, 80, 60, 30},
                {1500, 600, 60, 60}
        };


        // drawing the obstacles and targets
        StdDraw.setPenColor(StdDraw.DARK_GRAY); // color of obstacle
        int i;
        for (i = 0; i <= obstacleArray.length - 1; i++) {
            StdDraw.filledRectangle(obstacleArray[i][0] + obstacleArray[i][2] / 2,
                    obstacleArray[i][1] + obstacleArray[i][3] / 2,
                    obstacleArray[i][2] / 2, obstacleArray[i][3] / 2);
        }
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE); // color of targets
        int j;
        for (j = 0; j <= targetArray.length - 1; j++) {
            StdDraw.filledRectangle(targetArray[j][0] + targetArray[j][2] / 2,
                    targetArray[j][1] + targetArray[j][3] / 2,
                    targetArray[j][2] / 2, targetArray[j][3] / 2);
        }
        // draws the shooting platform and shooting line
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(60, 60, 60, 60); // shooting platform
        StdDraw.setPenRadius(0.01); // thickness of line
        StdDraw.line(x0,y0,x0+(bulletVelocity*4/9)*Math.cos(bulletAngle),y0+(bulletVelocity*4/9)*Math.sin(bulletAngle)); // length of line
        StdDraw.setPenRadius(); // reset the thickness
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(25,70,"a:"+ (Math.toDegrees(bulletAngle))); // shows the angle
        StdDraw.textLeft(25,50,"v:"+ (bulletVelocity)); // shows the velocity
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.show();

        // START THE GAME
        while (true) {
            double timeStep = 0.2; // Time step for simulation
            double t = 0; // Initial time
            double bV = bulletVelocity/1.725; // scaled velocity


            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {

                while (is_not_Hitted) {

                    StdDraw.pause(keyboardPauseDuration);

                    pst_x = x0 + bV * Math.cos(bulletAngle) * t; // formula for position of ball for x axis
                    pst_y = y0 + bV * Math.sin(bulletAngle) * t - 0.5 * gravity * t * t; // formula for position of ball for y axis

                    StdDraw.line(x0_same, y0_same, pst_x, pst_y); // draws the lines between ball's position
                    x0_same = pst_x; // former position of ball (x axis)
                    y0_same = pst_y; // former position of ball (y axis)

                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledCircle(pst_x,pst_y, 4.0); // draws the ball

                    // checking the ball's collision with obstacles
                    int a;
                    for (a = 0; a <= obstacleArray.length - 1; a++) {
                        if (((obstacleArray[a][0] <= pst_x) &&
                                (pst_x <= (obstacleArray[a][0] + obstacleArray[a][2])))
                                && ((obstacleArray[a][1] <= pst_y) &&
                                (pst_y <= (obstacleArray[a][1] + obstacleArray[a][3])))) {
                            is_not_Hitted = false;
                            StdDraw.textLeft(3, 790, "Hit an obstacle. Press 'r' to shoot again.");
                        }
                    }
                    // checking the ball's collision with targets
                    for (a = 0; a <= targetArray.length - 1; a++) {
                        if (((targetArray[a][0] <= pst_x) &&
                                (pst_x <= (targetArray[a][0] + targetArray[a][2])))
                                && ((targetArray[a][1] <= pst_y) &&
                                (pst_y <= (targetArray[a][1] + targetArray[a][3])))) {
                            is_not_Hitted = false;
                            StdDraw.textLeft(3, 790, "Congratulations: You hit the target!");

                        }
                    }

                    // checks exceeding of X scale
                    if (pst_x > canvas_width) {
                        is_not_Hitted = false;
                        StdDraw.textLeft(3, 790, "Max X reached. Press 'r' to shoot again.");

                    // checks hitting the ground
                    }
                    if (pst_y <= 0){
                        is_not_Hitted = false;
                        StdDraw.textLeft(3, 790, "Hit the ground. Press 'r' to shoot again.");
                    }
                    StdDraw.show();
                    StdDraw.pause(10);

                    t += timeStep; // updates the time

                }

            }

            // starts the game again
            if (StdDraw.isKeyPressed(KeyEvent.VK_R)) {
                StdDraw.clear(); // clear the canvas
                is_not_Hitted = true; // to shoot again
                //RESET THE GAME PARAMETERS
                gravity = 9.80665;
                x0_same = 120;
                y0_same = 120;
                bulletVelocity = 180;
                bulletAngle = Math.toRadians(45.0);

                // draws  obstacles again
                StdDraw.setPenColor(StdDraw.DARK_GRAY);
                for (i = 0; i <= obstacleArray.length - 1; i++) {
                    StdDraw.filledRectangle(obstacleArray[i][0] + obstacleArray[i][2] / 2,
                            obstacleArray[i][1] + obstacleArray[i][3] / 2,
                            obstacleArray[i][2] / 2, obstacleArray[i][3] / 2);
                }
                // draws targets again
                StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                for (j = 0; j <= targetArray.length - 1; j++) {
                    StdDraw.filledRectangle(targetArray[j][0] + targetArray[j][2] / 2,
                            targetArray[j][1] + targetArray[j][3] / 2,
                            targetArray[j][2] / 2, targetArray[j][3] / 2);
                }

                // draws shooting platform and line
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.filledRectangle(60, 60, 60, 60);
                StdDraw.setPenRadius(0.01);
                StdDraw.line(x0,y0,x0+(bulletVelocity*4/9)*Math.cos(bulletAngle),y0+(bulletVelocity*4/9)*Math.sin(bulletAngle));
                StdDraw.setPenRadius();
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.textLeft(25,70,"a:"+ (Math.toDegrees(bulletAngle)));
                StdDraw.textLeft(25,50,"v:"+ (bulletVelocity));
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.show();
            }
            // to decrease the angel
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)){
                StdDraw.pause(keyboardPauseDuration);
                StdDraw.clear();
                bulletAngle = bulletAngle - Math.toRadians(1); // increase the angel
                // draws obstacle and target
                StdDraw.setPenColor(StdDraw.DARK_GRAY);
                for (i = 0; i <= obstacleArray.length - 1; i++) {
                    StdDraw.filledRectangle(obstacleArray[i][0] + obstacleArray[i][2] / 2,
                            obstacleArray[i][1] + obstacleArray[i][3] / 2,
                            obstacleArray[i][2] / 2, obstacleArray[i][3] / 2);
                }
                StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                for (j = 0; j <= targetArray.length - 1; j++) {
                    StdDraw.filledRectangle(targetArray[j][0] + targetArray[j][2] / 2,
                            targetArray[j][1] + targetArray[j][3] / 2,
                            targetArray[j][2] / 2, targetArray[j][3] / 2);
                }
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.filledRectangle(60, 60, 60, 60);
                StdDraw.setPenRadius(0.01);
                StdDraw.line(x0,y0,x0+(bulletVelocity*4/9)*Math.cos(bulletAngle),y0+(bulletVelocity*4/9)*Math.sin(bulletAngle));
                StdDraw.setPenRadius();
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.textLeft(25,70,"a:"+ (Math.toDegrees(bulletAngle)));
                StdDraw.textLeft(25,50,"v:"+ (bulletVelocity));
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.show();

            }
            // to increase the angle
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)){
                StdDraw.pause(keyboardPauseDuration);
                StdDraw.clear(); // clear the former angle
                bulletAngle = bulletAngle + Math.toRadians(1); // decreases the angel
                StdDraw.setPenColor(StdDraw.DARK_GRAY);
                // draws obstacle and target
                for (i = 0; i <= obstacleArray.length - 1; i++) {
                    StdDraw.filledRectangle(obstacleArray[i][0] + obstacleArray[i][2] / 2,
                            obstacleArray[i][1] + obstacleArray[i][3] / 2,
                            obstacleArray[i][2] / 2, obstacleArray[i][3] / 2);
                }
                StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                for (j = 0; j <= targetArray.length - 1; j++) {
                    StdDraw.filledRectangle(targetArray[j][0] + targetArray[j][2] / 2,
                            targetArray[j][1] + targetArray[j][3] / 2,
                            targetArray[j][2] / 2, targetArray[j][3] / 2);
                }
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.filledRectangle(60, 60, 60, 60);
                StdDraw.setPenRadius(0.01);
                StdDraw.line(x0,y0,x0+(bulletVelocity*4/9)*Math.cos(bulletAngle),y0+(bulletVelocity*4/9)*Math.sin(bulletAngle));
                StdDraw.setPenRadius();
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.textLeft(25,70,"a:"+ (Math.toDegrees(bulletAngle)));
                StdDraw.textLeft(25,50,"v:"+ (bulletVelocity));
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.show();

            }
            // increase the velocity
            if (StdDraw.isKeyPressed(KeyEvent.VK_UP)){
                StdDraw.pause(keyboardPauseDuration);
                StdDraw.clear();
                bulletVelocity = bulletVelocity+ 1;// increase the velocity
                // draws target and obstacle
                StdDraw.setPenColor(StdDraw.DARK_GRAY);
                for (i = 0; i <= obstacleArray.length - 1; i++) {
                    StdDraw.filledRectangle(obstacleArray[i][0] + obstacleArray[i][2] / 2,
                            obstacleArray[i][1] + obstacleArray[i][3] / 2,
                            obstacleArray[i][2] / 2, obstacleArray[i][3] / 2);
                }
                StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                for (j = 0; j <= targetArray.length - 1; j++) {
                    StdDraw.filledRectangle(targetArray[j][0] + targetArray[j][2] / 2,
                            targetArray[j][1] + targetArray[j][3] / 2,
                            targetArray[j][2] / 2, targetArray[j][3] / 2);
                }
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.filledRectangle(60, 60, 60, 60);
                StdDraw.setPenRadius(0.01);
                StdDraw.line(x0,y0,x0+(bulletVelocity*4/9)*Math.cos(bulletAngle),y0+(bulletVelocity*4/9)*Math.sin(bulletAngle));
                StdDraw.setPenRadius();
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.textLeft(25,70,"a:"+ (Math.toDegrees(bulletAngle)));
                StdDraw.textLeft(25,50,"v:"+ (bulletVelocity));
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.show();
            }
            // decrease velocity
            if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)){
                StdDraw.pause(keyboardPauseDuration);
                StdDraw.clear();
                bulletVelocity = bulletVelocity -1;// decrease velocity
                StdDraw.setPenColor(StdDraw.DARK_GRAY);
                // draws target and obstacle
                for (i = 0; i <= obstacleArray.length - 1; i++) {
                    StdDraw.filledRectangle(obstacleArray[i][0] + obstacleArray[i][2] / 2,
                            obstacleArray[i][1] + obstacleArray[i][3] / 2,
                            obstacleArray[i][2] / 2, obstacleArray[i][3] / 2);
                }
                StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                for (j = 0; j <= targetArray.length - 1; j++) {
                    StdDraw.filledRectangle(targetArray[j][0] + targetArray[j][2] / 2,
                            targetArray[j][1] + targetArray[j][3] / 2,
                            targetArray[j][2] / 2, targetArray[j][3] / 2);
                }
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.filledRectangle(60, 60, 60, 60);
                StdDraw.setPenRadius(0.01);
                StdDraw.line(x0,y0,x0+(bulletVelocity*4/9)*Math.cos(bulletAngle),y0+(bulletVelocity*4/9)*Math.sin(bulletAngle));
                StdDraw.setPenRadius();
                StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.textLeft(25,70,"a:"+ (Math.toDegrees(bulletAngle)));
                StdDraw.textLeft(25,50,"v:"+ (bulletVelocity));
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.show();
            }
        }

    }
}