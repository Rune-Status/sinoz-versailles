/*
  Copyright (c) OpenRS

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
 */
package io.versailles.application.storage;

import io.versailles.application.storage.util.ByteBufferUtils;

import java.nio.ByteBuffer;

/**
 * An {@link Index} points to a file inside a {@link FileBundle}.
 *
 * @author Graham
 * @author `Discardedx2
 * @author Sino
 */
public final class Index {

    /**
     * The size of an index, in bytes.
     */
    public static final int SIZE = 6;

    /**
     * Decodes the specified {@link ByteBuffer} into an {@link Index} object.
     *
     * @param buf The buffer.
     * @return The index.
     */
    public static Index decode(ByteBuffer buf) {
        if (buf.remaining() != SIZE)
            throw new IllegalArgumentException();

        int size = ByteBufferUtils.getMedium(buf);
        int page = ByteBufferUtils.getMedium(buf);
        return new Index(size, page);
    }

    /**
     * The size of the file in bytes.
     */
    private int size;

    /**
     * The number of the first page that contains the file.
     */
    private int page;

    /**
     * Creates a new index.
     *
     * @param size The size of the file in bytes.
     * @param page The number of the first page that contains the file.
     */
    public Index(int size, int page) {
        this.size = size;
        this.page = page;
    }

    /**
     * Encodes this index into a byte buffer.
     *
     * @return The buffer.
     */
    public ByteBuffer encode() {
        ByteBuffer buf = ByteBuffer.allocate(Index.SIZE);
        ByteBufferUtils.putMedium(buf, size);
        ByteBufferUtils.putMedium(buf, page);
        return (ByteBuffer) buf.flip();
    }

    /**
     * Gets the number of the first page that contains the file.
     *
     * @return The number of the first page that contains the file.
     */
    public int getPage() {
        return page;
    }

    /**
     * Gets the size of the file.
     *
     * @return The size of the file in bytes.
     */
    public int getSize() {
        return size;
    }

}