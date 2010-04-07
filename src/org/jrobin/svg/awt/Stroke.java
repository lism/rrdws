package org.jrobin.svg.awt;
//http://www.selfsvg.info/?section=4.7
public interface Stroke {
	/**
	 * 4.7.1 Die Liniendicke
	 * Die St�rke einer Linie (also auch einer Umrandung) 
	 * wird mit Hilfe des Attributs stroke-width festgelegt, 
	 * das none oder positive L�ngenangaben besitzen kann. 
 	 */
	public float getWidth();

	/**
	 * linecap
	 * 
	 * TODO
	 */
	public static String[] linecaps = {
		"butt",	"round", "square" };
	
	/**
	 * linejoin
	 * miter - zeichnet eckige Spitzen (Standard)
	 * round - zeichnet abgerundete Spitzen
	 * bevel - zeichnet abgeschnittene Spitzen
	 * 
	 * TODO
	 */
	public static String[] linejoin = {
		"miter",	"round", "bevel" };
	
	
	/**
	 * dasharray
	 * 
	 * 4.7.4 Segmentierte Linien
	 * Die letzte hier vorgestellte Stifteigenschaft ist f�r die 
	 * Aufteilung einer Linie in einzelne Segmente zust�ndig. 
	 * Diese Eigenschaft nennt sich stroke-dasharray und erwartet 
	 * als Wert entweder none, was eine durchgehenden Linie 
	 * generieren w�rde, oder eine komma-separierte Liste von 
	 * Prozentwerten. Dabei verwendet der Interpreter jeden 
	 * zweiten Wert als L�cke. Folgendes Beispiel soll dies 
	 * etwas anschaulicher gestalten:
	 * 
	 * TODO
	 * 
	 */
	
	Color getColor();
}
