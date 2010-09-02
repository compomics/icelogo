package com.compomics.icelogo.core.enumeration;

import com.compomics.icelogo.core.data.RegulatedEntity;

/**
 * Created by IntelliJ IDEA. User: Niklaas Colaert Date: 28-aug-2008 Time: 9:07:42 To change this template use File |
 * Settings | File Templates.
 */

/**
 * This class stores a color for every aminoacid
 */
public class ColorScheme {
// ------------------------------ FIELDS ------------------------------

    private ColorEnum iColorA;
    private ColorEnum iColorB;
    private ColorEnum iColorC;
    private ColorEnum iColorD;
    private ColorEnum iColorE;
    private ColorEnum iColorF;
    private ColorEnum iColorG;
    private ColorEnum iColorH;
    private ColorEnum iColorI;
    private ColorEnum iColorJ;
    private ColorEnum iColorK;
    private ColorEnum iColorL;
    private ColorEnum iColorM;
    private ColorEnum iColorN;
    private ColorEnum iColorO;
    private ColorEnum iColorP;
    private ColorEnum iColorQ;
    private ColorEnum iColorR;
    private ColorEnum iColorS;
    private ColorEnum iColorT;
    private ColorEnum iColorU;
    private ColorEnum iColorV;
    private ColorEnum iColorW;
    private ColorEnum iColorX;
    private ColorEnum iColorY;
    private ColorEnum iColorZ;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Constructor
     *
     * @param aColorA
     * @param aColorB
     * @param aColorC
     * @param aColorD
     * @param aColorE
     * @param aColorF
     * @param aColorG
     * @param aColorH
     * @param aColorI
     * @param aColorJ
     * @param aColorK
     * @param aColorL
     * @param aColorM
     * @param aColorN
     * @param aColorO
     * @param aColorP
     * @param aColorQ
     * @param aColorR
     * @param aColorS
     * @param aColorT
     * @param aColorU
     * @param aColorV
     * @param aColorW
     * @param aColorX
     * @param aColorY
     * @param aColorZ
     */
    public ColorScheme(ColorEnum aColorA, ColorEnum aColorB, ColorEnum aColorC, ColorEnum aColorD, ColorEnum aColorE, ColorEnum aColorF, ColorEnum aColorG, ColorEnum aColorH, ColorEnum aColorI, ColorEnum aColorJ, ColorEnum aColorK, ColorEnum aColorL, ColorEnum aColorM, ColorEnum aColorN, ColorEnum aColorO, ColorEnum aColorP, ColorEnum aColorQ, ColorEnum aColorR, ColorEnum aColorS, ColorEnum aColorT, ColorEnum aColorU, ColorEnum aColorV, ColorEnum aColorW, ColorEnum aColorX, ColorEnum aColorY, ColorEnum aColorZ) {
        this.iColorA = aColorA;
        this.iColorB = aColorB;
        this.iColorC = aColorC;
        this.iColorD = aColorD;
        this.iColorE = aColorE;
        this.iColorF = aColorF;
        this.iColorG = aColorG;
        this.iColorH = aColorH;
        this.iColorI = aColorI;
        this.iColorJ = aColorJ;
        this.iColorK = aColorK;
        this.iColorL = aColorL;
        this.iColorM = aColorM;
        this.iColorN = aColorN;
        this.iColorO = aColorO;
        this.iColorP = aColorP;
        this.iColorQ = aColorQ;
        this.iColorR = aColorR;
        this.iColorS = aColorS;
        this.iColorT = aColorT;
        this.iColorU = aColorU;
        this.iColorV = aColorV;
        this.iColorW = aColorW;
        this.iColorX = aColorX;
        this.iColorY = aColorY;
        this.iColorZ = aColorZ;
    }

    /**
     * The default color scheme
     */
    public ColorScheme() {
        iColorA = ColorEnum.BLACK;
        iColorB = ColorEnum.BLACK;
        iColorC = ColorEnum.BLUE;
        iColorD = ColorEnum.RED;
        iColorE = ColorEnum.RED;
        iColorF = ColorEnum.BLACK;
        iColorG = ColorEnum.GREEN;
        iColorH = ColorEnum.BLUE;
        iColorI = ColorEnum.BLACK;
        iColorJ = ColorEnum.BLACK;
        iColorK = ColorEnum.BLUE;
        iColorL = ColorEnum.BLACK;
        iColorM = ColorEnum.BLACK;
        iColorN = ColorEnum.PURPLE;
        iColorO = ColorEnum.BLACK;
        iColorP = ColorEnum.BLACK;
        iColorQ = ColorEnum.BLACK;
        iColorR = ColorEnum.BLUE;
        iColorS = ColorEnum.GREEN;
        iColorT = ColorEnum.GREEN;
        iColorU = ColorEnum.BLACK;
        iColorV = ColorEnum.BLACK;
        iColorW = ColorEnum.BLACK;
        iColorX = ColorEnum.BLACK;
        iColorY = ColorEnum.GREEN;
        iColorZ = ColorEnum.BLACK;
    }


// -------------------------- OTHER METHODS --------------------------

    public void setColor(RegulatedEntity anElement) {
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("A")) {
            anElement.setColor(iColorA);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("B")) {
            anElement.setColor(iColorB);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("C")) {
            anElement.setColor(iColorC);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("D")) {
            anElement.setColor(iColorD);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("E")) {
            anElement.setColor(iColorE);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("F")) {
            anElement.setColor(iColorF);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("G")) {
            anElement.setColor(iColorG);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("H")) {
            anElement.setColor(iColorH);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("I")) {
            anElement.setColor(iColorI);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("J")) {
            anElement.setColor(iColorJ);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("K")) {
            anElement.setColor(iColorK);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("L")) {
            anElement.setColor(iColorL);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("M")) {
            anElement.setColor(iColorM);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("N")) {
            anElement.setColor(iColorN);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("O")) {
            anElement.setColor(iColorO);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("P")) {
            anElement.setColor(iColorP);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("Q")) {
            anElement.setColor(iColorQ);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("R")) {
            anElement.setColor(iColorR);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("S")) {
            anElement.setColor(iColorS);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("T")) {
            anElement.setColor(iColorT);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("U")) {
            anElement.setColor(iColorU);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("V")) {
            anElement.setColor(iColorV);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("W")) {
            anElement.setColor(iColorW);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("X")) {
            anElement.setColor(iColorX);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("Y")) {
            anElement.setColor(iColorY);
        }
        if (String.valueOf(anElement.getAminoAcid()).equalsIgnoreCase("Z")) {
            anElement.setColor(iColorZ);
        }
    }

    public ColorEnum getColorZ() {
        return iColorZ;
    }

    public ColorEnum getColorA() {
        return iColorA;
    }

    public ColorEnum getColorB() {
        return iColorB;
    }

    public ColorEnum getColorC() {
        return iColorC;
    }

    public ColorEnum getColorD() {
        return iColorD;
    }

    public ColorEnum getColorE() {
        return iColorE;
    }

    public ColorEnum getColorF() {
        return iColorF;
    }

    public ColorEnum getColorG() {
        return iColorG;
    }

    public ColorEnum getColorH() {
        return iColorH;
    }

    public ColorEnum getColorI() {
        return iColorI;
    }

    public ColorEnum getColorJ() {
        return iColorJ;
    }

    public ColorEnum getColorK() {
        return iColorK;
    }

    public ColorEnum getColorL() {
        return iColorL;
    }

    public ColorEnum getColorM() {
        return iColorM;
    }

    public ColorEnum getColorN() {
        return iColorN;
    }

    public ColorEnum getColorO() {
        return iColorO;
    }

    public ColorEnum getColorP() {
        return iColorP;
    }

    public ColorEnum getColorQ() {
        return iColorQ;
    }

    public ColorEnum getColorR() {
        return iColorR;
    }

    public ColorEnum getColorS() {
        return iColorS;
    }

    public ColorEnum getColorT() {
        return iColorT;
    }

    public ColorEnum getColorU() {
        return iColorU;
    }

    public ColorEnum getColorV() {
        return iColorV;
    }

    public ColorEnum getColorW() {
        return iColorW;
    }

    public ColorEnum getColorX() {
        return iColorX;
    }

    public ColorEnum getColorY() {
        return iColorY;
    }
}