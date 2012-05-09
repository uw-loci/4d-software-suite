// Go.java

public class Go {
  public static final void main(String[] args) throws Exception {
    String dir = OverlayInput.input_directory;
    ObjectArray objectArray = new ObjectArray();
    HeaderArray headerArray = new HeaderArray();
    int numPlanes = 12, numFrames = 409;
    for (int plane=1; plane<=numPlanes; plane++) {
      headerArray.resetArray();
      OverlayInput.readOverlayHeaders(dir, headerArray, plane);
      for (int frame=1; frame<=numFrames; frame++) {
        System.err.println("Processing plane #" + plane +
          ", frame #" + frame + "...");
        System.out.println("Plane #" + plane + ", Frame #" + frame + ":");
        OverlayInput.readOverlayFromDisk(dir,
          objectArray, headerArray, plane, frame);
        System.out.println();
      }
    }
    System.exit(0);
  }
}
