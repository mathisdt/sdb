package org.zephyrsoft.util;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.imageio.*;

/**
 * Copyright (C) 2007  Scott Carpenter (scottc at movingtofreedom dot org)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Created on November 9, 2007, 4:07 PM
 *
 */
public class ImagePanel extends JPanel {

    private Image image;
    private Image scaledImage;

    //constructor
    public ImagePanel(Color background) {
        super();
        setBackground(background);
    }

    public void loadImage(String file) throws IOException {
        Image myImage = ImageIO.read(new File(file));
        setImage(myImage);
    }
    
    public void setImage(Image myImage) {
    	image = myImage;
    	setScaledImage();
    }

    //e.g., containing frame might call this from formComponentResized
    public void scaleImage() {
        setScaledImage();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if ( scaledImage != null ) {
            g.drawImage(scaledImage, (super.getWidth()-scaledImage.getWidth(null))/2, 0, this);
        }
    }

    private void setScaledImage() {
        if ( image != null ) {

            //use floats so division below won't round
            float iw = image.getWidth(null);
            float ih = image.getHeight(null);
            float pw = this.getWidth();   //panel width
            float ph = this.getHeight();  //panel height

            if ( pw < iw || ph < ih ) {

                /* compare some ratios and then decide which side of image to anchor to panel
                   and scale the other side
                   (this is all based on empirical observations and not at all grounded in theory)*/

                if ( (pw / ph) > (iw / ih) ) {
                    iw = -1;
                    ih = ph;
                } else {
                    iw = pw;
                    ih = -1;
                }

                //prevent errors if panel is 0 wide or high
                if (iw == 0) {
                    iw = -1;
                }
                if (ih == 0) {
                    ih = -1;
                }

                scaledImage = image.getScaledInstance(
                            new Float(iw).intValue(), new Float(ih).intValue(), Image.SCALE_DEFAULT);

            } else {
                scaledImage = image;
            }
        }
    }

}
