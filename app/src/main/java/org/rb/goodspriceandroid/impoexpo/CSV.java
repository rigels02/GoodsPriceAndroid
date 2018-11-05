package org.rb.goodspriceandroid.impoexpo;


import org.rb.goodspriceandroid.model.Good;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Developer
 */
public class CSV {

    private final static String FMT = "Line: (%d) %s : %s";

    public static String makeCSVString(List<Good> goods) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        for (Good good : goods) {
            sb.append(df.format(good.getCdate())).append(",")
                    .append(good.getName()).append(",")
                    .append(good.getShop()).append(",")
                    .append(good.getPrice()).append(",")
                    .append(good.getDiscount()).append("\n");
        }
        return sb.toString();
    }

    public static List<Good> makeListFromCSVString(String csvString) throws Exception {

        ArrayList<Good> goods = new ArrayList<>();

        String[] lines = csvString.split("\n");
        int i = 1;
        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }
            String[] tokens = line.split(",");

            if (tokens.length != Good.FCOUNT) {
                throw new Exception("Bad CSV format!\n Line: (" + i + ") : " + line);
            }
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Good good = new Good();
            for (int c = 0; c < tokens.length; c++) {
                switch (c) {
                    case 0:
                        Date ds = null;
                        try {
                            ds = df.parse(tokens[0]);
                        } catch (ParseException e) {
                            throw new Exception(String.format(FMT, i, line, e.getMessage()));
                        }

                        good.setCdate(ds);
                        break;
                    case 1:
                        good.setName(tokens[1].trim());
                        break;
                    case 2:
                        good.setShop(tokens[2].trim());
                        break;
                    case 3:
                        try {
                            good.setPrice(Double.parseDouble(tokens[3]));
                        } catch (NumberFormatException ex) {
                            throw new NumberFormatException(
                                    String.format(FMT, i, line, ex.getMessage())
                            );
                        }
                        break;
                    case 4:
                        try {
                            good.setDiscount(Double.parseDouble(tokens[4]));
                        } catch (NumberFormatException ex) {
                            throw new NumberFormatException(
                                    String.format(FMT, i, line, ex.getMessage())
                            );
                        }
                        break;
                    default:
                        throw new Exception("Bad CSV format!\n Line: " + i);
                }
            }
            goods.add(good);
            i++;
        }
        return goods;
    }
}
