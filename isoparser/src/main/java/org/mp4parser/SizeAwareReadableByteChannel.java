package org.mp4parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class SizeAwareReadableByteChannel implements ReadableByteChannel {

  private ReadableByteChannel channel;
  private long size;
  private long offset = 0;

  public SizeAwareReadableByteChannel(ReadableByteChannel channel, long size) {
    this.channel = channel;
    this.size = size;
  }

  public int read(ByteBuffer dst) throws IOException {
    int read = channel.read(dst);
    if (read!=-1) {
      offset += read;
    }
    return read;
  }

  public boolean isOpen() {
    return channel.isOpen();
  }

  public void close() throws IOException {
    channel.close();
  }

  public long getSize() {
    return size;
  }

  public long getOffset() {
    return offset;
  }

  public long getRemaining() {
    return getSize()-getOffset();
  }

}
