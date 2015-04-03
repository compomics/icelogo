package com.compomics.icelogo.core.enumeration;

import com.compomics.icelogo.core.data.RegulatedEntity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Niklaas Colaert, except totally changed by Davy Maddelein Date: 28-aug-2008 Time: 9:07:42 To change this template use File |
 * Settings | File Templates.
 */

/**
 * This class stores a color for every aminoacid
 */
public class ColorScheme {

    private List<Color> colors = new ArrayList<Color>();
    private List<Integer> mapper = new ArrayList<Integer>(21);

    //todo: presets could be extracted to file

    public ColorScheme() {
        colors.addAll(new ArrayList<Color>() {{
            this.add(Color.decode("0x0000E5"));
            this.add(Color.decode("0x0E00E4"));
            this.add(Color.decode("0x1D00E3"));
            this.add(Color.decode("0x2B00E3"));
            this.add(Color.decode("0x3900E2"));
            this.add(Color.decode("0x4800E1"));
            this.add(Color.decode("0x5600E1"));
            this.add(Color.decode("0x6400E0"));
            this.add(Color.decode("0x7200E0"));
            this.add(Color.decode("0x8000DF"));
            this.add(Color.decode("0x8E00DE"));
            this.add(Color.decode("0x9B00DE"));
            this.add(Color.decode("0xA900DD"));
            this.add(Color.decode("0xB700DD"));
            this.add(Color.decode("0xC400DC"));
            this.add(Color.decode("0xD200DB"));
            this.add(Color.decode("0xDB00D6"));
            this.add(Color.decode("0xDA00C8"));
            this.add(Color.decode("0xDA00BA"));
            this.add(Color.decode("0xD900AB"));
            this.add(Color.decode("0xD8009D"));
            this.add(Color.decode("0xD8008F"));
            this.add(Color.decode("0xD70080"));
            this.add(Color.decode("0xD70072"));
            this.add(Color.decode("0xD60064"));
            this.add(Color.decode("0xD50056"));
            this.add(Color.decode("0xD50049"));
            this.add(Color.decode("0xD4003B"));
            this.add(Color.decode("0xD4002D"));
            this.add(Color.decode("0xD30020"));
            this.add(Color.decode("0xD20012"));
            this.add(Color.decode("0xD20005"));
            this.add(Color.decode("0xD10800"));
            this.add(Color.decode("0xD11500"));
            this.add(Color.decode("0xD02200"));
            this.add(Color.decode("0xCF3000"));
            this.add(Color.decode("0xCF3D00"));
            this.add(Color.decode("0xCE4A00"));
            this.add(Color.decode("0xCE5700"));
            this.add(Color.decode("0xCD6300"));
            this.add(Color.decode("0xCC7000"));
            this.add(Color.decode("0xCC7D00"));
            this.add(Color.decode("0xCB8900"));
            this.add(Color.decode("0xCB9600"));
            this.add(Color.decode("0xCAA200"));
            this.add(Color.decode("0xC9AF00"));
            this.add(Color.decode("0xC9BB00"));
            this.add(Color.decode("0xC8C700"));
            this.add(Color.decode("0xBCC800"));
            this.add(Color.decode("0xAEC700"));
            this.add(Color.decode("0xA1C600"));
            this.add(Color.decode("0x94C600"));
            this.add(Color.decode("0x87C500"));
            this.add(Color.decode("0x7AC500"));
            this.add(Color.decode("0x6DC400"));
            this.add(Color.decode("0x60C300"));
            this.add(Color.decode("0x54C300"));
            this.add(Color.decode("0x47C200"));
            this.add(Color.decode("0x3AC200"));
            this.add(Color.decode("0x2EC100"));
            this.add(Color.decode("0x21C000"));
            this.add(Color.decode("0x15C000"));
            this.add(Color.decode("0x09BF00"));
            this.add(Color.decode("0x00BF03"));
        }});
    }


    public ColorScheme(String[] colorString) {
        for (String colorHex : colorString) {
            colors.add(Color.decode(colorHex));
        }
    }



    public void setColors(String[] colorString) {
        colors.clear();
        for (String aColor : colorString) {
            colors.add(Color.decode(aColor));
        }
    }

    public List<Color> getColors(){
        return Collections.unmodifiableList(colors);
    }

    public Color getAminoAcidColor(RegulatedEntity thing) {

        return colors.get(thing.getAminoAcidEntity().getAANumber());
    }

    public Color getAminoAcidColor(AminoAcidEnum anAAEnum) {
        return colors.get(anAAEnum.getAANumber());
    }
}