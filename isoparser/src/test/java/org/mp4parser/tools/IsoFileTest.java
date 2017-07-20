package org.mp4parser.tools;

import junit.framework.TestCase;
import org.junit.Assert;
import org.mp4parser.IsoFile;
import org.mp4parser.boxes.apple.AppleGPSCoordinatesBox;
import org.mp4parser.boxes.iso14496.part12.MovieBox;
import org.mp4parser.boxes.iso14496.part12.UserDataBox;
import org.mp4parser.boxes.threegpp.ts26244.LocationInformationBox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.List;

/**
 *
 */
public class IsoFileTest extends TestCase {
    public void testFourCC() {
        assertEquals("AA\0\0", IsoFile.bytesToFourCC(new byte[]{65, 65}));
        assertEquals("AAAA", IsoFile.bytesToFourCC(new byte[]{65, 65, 65, 65, 65, 65}));
        assertEquals("AAAA", new String(IsoFile.fourCCtoBytes("AAAAAAA")));
        assertEquals("AA\0\0", new String(IsoFile.fourCCtoBytes("AA")));
        assertEquals("\0\0\0\0", new String(IsoFile.fourCCtoBytes(null)));
        assertEquals("\0\0\0\0", new String(IsoFile.fourCCtoBytes("")));
        assertEquals("\0\0\0\0", IsoFile.bytesToFourCC(null));
        assertEquals("\0\0\0\0", IsoFile.bytesToFourCC(new byte[0]));

    }

    public void testZeroSizeBox() throws Exception {
        File f = new File(IsoFileTest.class.getResource("/zero-size-mov.mov").toURI());
        IsoFile isoFile = new IsoFile(Channels.newChannel(new FileInputStream(f)), f.length());
        String gps = extractGPSCoordinates(isoFile.getMovieBox());
        assertNotNull(gps);
    }

    private String extractGPSCoordinates(MovieBox movieBox) {
        List<UserDataBox> metaBoxes = movieBox.getBoxes(UserDataBox.class);

        if (!metaBoxes.isEmpty()) {
            UserDataBox userDataBox = metaBoxes.get(0);

            List<AppleGPSCoordinatesBox> appleGPSCoordinatesBoxes = userDataBox.getBoxes(AppleGPSCoordinatesBox.class);

            if (!appleGPSCoordinatesBoxes.isEmpty()) {
                AppleGPSCoordinatesBox appleGPSCoordinatesBox = appleGPSCoordinatesBoxes.get(0);
                return appleGPSCoordinatesBox.getValue();
            }
            List<LocationInformationBox> locationInformationBoxes = userDataBox.getBoxes(LocationInformationBox.class);
            if (!locationInformationBoxes.isEmpty()) {
                LocationInformationBox locationInformationBox = locationInformationBoxes.get(0);

                return ""+locationInformationBox.getLatitude()+locationInformationBox.getLongitude();
            }
        }

        return null;
    }
}
