package com.bcp.reto.exchangerate;

public class NumberTextConverter {

    private static final String[] UNIDADES = {"", "uno ", "dos ", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve "};
    private static final String[] DECENAS = {"diez ", "once ", "doce ", "trece ", "catorce ", "quince ", "dieciseis ", "diecisiete ",
            "dieciocho ", "diecinueve", "veinte ", "treinta ", "cuarenta ", "cincuenta ", "sesenta ",
            "setenta ", "ochenta ", "noventa "};
    private static final String[] CENTENAS = {"", "ciento ", "doscientos ", "trecientos ", "cuatrocientos ", "quinientos ", "seiscientos ",
            "setecientos ", "ochocientos ", "novecientos "};

    public static void main(String[] args) {
        System.out.println(NumberTextConverter.convert("21"));
        System.out.println(NumberTextConverter.convert("32"));
    }

    public static String convert(String numero) {
        String letras;
        if (numero == null) {
            return null;
        }
        if (Integer.parseInt(numero) == 0) {
            letras = "cero ";
        } else if (Integer.parseInt(numero) > 999) {
            letras = getMil(numero);
        } else if (Integer.parseInt(numero) > 99) {
            letras = getCentena(numero);
        } else if (Integer.parseInt(numero) > 9) {
            letras = getDecena(numero);
        } else {
            letras = getUnidad(numero);
        }
        return letras;
    }


    private static String getUnidad(String numero) {
        String num = numero.substring(numero.length() - 1);
        return UNIDADES[Integer.parseInt(num)];
    }

    private static String getDecena(String num) {
        int n = Integer.parseInt(num);
        if (n < 10) {
            return getUnidad(num);
        } else if (n > 19) {
            String u = getUnidad(num);
            int i = Integer.parseInt(num.substring(0, 1)) + 8;
            if (u.isEmpty()) {
                return DECENAS[i];
            } else {
                return DECENAS[i] + "y " + u;
            }
        } else {
            return DECENAS[n - 10];
        }
    }

    private static String getCentena(String num) {
        if (Integer.parseInt(num) > 99) {
            if (Integer.parseInt(num) == 100) {
                return " cien ";
            } else {
                return CENTENAS[Integer.parseInt(num.substring(0, 1))] + getDecena(num.substring(1));
            }
        } else {
            return getDecena(Integer.parseInt(num) + "");
        }
    }

    private static String getMil(String numero) {

        String c = numero.substring(numero.length() - 3);
        String m = numero.substring(0, numero.length() - 3);
        String n;
        if (Integer.parseInt(m) > 0) {
            n = getCentena(m);
            return n + "mil " + getCentena(c);
        } else {
            return "" + getCentena(c);
        }
    }
}
