// Conversion.java

import java.awt.Color;
import java.io.*;
import java.util.Date;
import loci.visbio.util.ColorUtil;

public class Conversion {

  private static final Color[] COLORS = {
    Color.black,
    Color.blue,
    Color.cyan,
    Color.darkGray,
    Color.gray,
    Color.green,
    Color.lightGray,
    Color.magenta,
    Color.orange,
    Color.pink,
    Color.red,
    Color.white,
    Color.yellow
  };

  public static void main(String[] args) throws Exception {
    String path = "/home/curtis/data/tiff/sharon/";
    BufferedReader fin = new BufferedReader(
      new FileReader(path + "sharon.txt"));
    PrintWriter fout = new PrintWriter(
      new FileWriter(path + "CellLineage2.txt"));
    fout.println("# Generated from 4DViewer/Java data on " + new Date());
    fout.println();
    fout.println("Overlay\tSlice (12)\tTime (542)\tx1\ty1\tx2\ty2\ttext\tcolor\tfilled\tgroup\tnotes");
    int lastPlane = -1, plane = -1, frame = -1;
    String type = null;
    float x1 = Float.NaN, y1 = Float.NaN, x2 = Float.NaN, y2 = Float.NaN;
    String text = null;
    Color color = null;
    while (true) {
      String line = fin.readLine();
      if (line == null) line = "[[";
      if (line.startsWith("Plane")) {
        int pound = line.indexOf("#");
        int comma = line.indexOf(",", pound);
        plane = Integer.parseInt(line.substring(pound + 1, comma));
        pound = line.lastIndexOf("#");
        int colon = line.lastIndexOf(":");
        frame = Integer.parseInt(line.substring(pound + 1, colon));
      }
      else if (line.startsWith("[")) {
        if (type != null) {
          if (plane != lastPlane) {
            System.out.println("Converting plane #" + plane + "/12");
            lastPlane = plane;
          }
          if (!type.equals("Outline")) {
            // skip Outlines for now
            boolean filled = type.equals("Arrow");
            if (type.equals("Text")) y1 -= 4; // little adjustment
            String sx2 = type.equals("Text") ? "N/A" : "" + x2;
            String sy2 = type.equals("Text") ? "N/A" : "" + y2;
            text = type.equals("Text") ? text : "N/A";
            if (!type.equals("Text") || !text.trim().equals("")) {
              // write out previous entry
              fout.println(type + "\t" + plane + "\t" + frame + "\t" + x1 +
                "\t" + y1 + "\t" + sx2 + "\t" + sy2 + "\t" + text + "\t" +
                ColorUtil.colorToHex(color) + "\t" + filled + "\tNone\t");
            }
          }

          // clear out values
          x1 = Float.NaN;
          y1 = Float.NaN;
          x2 = Float.NaN;
          y2 = Float.NaN;
          text = null;
          color = null;
        }
        if (line.startsWith("[[")) break; // EOF
        type = line.substring(1, line.length() - 1);
      }
      else if (!line.trim().equals("")) {
        // parse key/value pair
        int equals = line.indexOf("=");
        String key = line.substring(0, equals);
        String value = line.substring(equals + 1);

        if (key.equals("start_pt_x")) x1 = Float.parseFloat(value);
        else if (key.equals("start_pt_y")) y1 = 434 - Float.parseFloat(value);
        else if (key.equals("end_pt_x")) x2 = Float.parseFloat(value);
        else if (key.equals("end_pt_y")) y2 = 434 - Float.parseFloat(value);
        else if (key.equals("text_string")) text = value;
        else if (key.equals("object_color")) {
          color = COLORS[Integer.parseInt(value)];
        }
      }
    }
    fin.close();
    fout.close();
  }

}
