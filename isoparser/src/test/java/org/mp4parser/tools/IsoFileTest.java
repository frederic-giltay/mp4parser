package org.mp4parser.tools;

import junit.framework.TestCase;
import org.mp4parser.IsoFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.Channels;

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

    }
}
