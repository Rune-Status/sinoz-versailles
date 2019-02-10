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
 * A page is a block of 520 bytes that carries a piece of the whole data.
 *
 * @author Graham
 * @author `Discardedx2
 * @author Sino
 */
public final class Page {
    /**
     * The size of the header within a page in bytes.
     */
    private static final int HEADER_SIZE = 8;

    /**
     * The size of the data within a page in bytes.
     */
    static final int PAYLOAD_SIZE = 512;

    /**
     * The total size of a page in bytes.
     */
    static final int SIZE = HEADER_SIZE + PAYLOAD_SIZE;

    /**
     * Decodes the specified {@link ByteBuffer} into a {@link Page} object.
     */
    static Page decode(ByteBuffer buf) {
        if (buf.remaining() != SIZE)
            throw new IllegalArgumentException();

        int id = buf.getShort() & 0xFFFF;
        int chunk = buf.getShort() & 0xFFFF;
        int tail = ByteBufferUtils.getMedium(buf);
        int type = buf.get() & 0xFF;
        byte[] data = new byte[PAYLOAD_SIZE];
        buf.get(data);

        return new Page(type, id, chunk, tail, data);
    }

    /**
     * The type of file this page contains.
     */
    private final int type;

    /**
     * The id of the file this page contains.
     */
    private final int id;

    /**
     * The chunk within the file that this page contains.
     */
    private final int chunk;

    /**
     * The next page.
     */
    private final int tail;

    /**
     * The data in this page.
     */
    private final byte[] data;

    /**
     * Creates a new {@link Page}.
     *
     * @param type  The type of the file.
     * @param id    The file's id.
     * @param chunk The chunk of the file this page contains.
     * @param tail  The page containing the next chunk.
     * @param data  The data in this page.
     */
    Page(int type, int id, int chunk, int tail, byte[] data) {
        this.type = type;
        this.id = id;
        this.chunk = chunk;
        this.tail = tail;
        this.data = data;
    }

    /**
     * Encodes this page into a {@link ByteBuffer}.
     *
     * @return The encoded buffer.
     */
    public ByteBuffer encode() {
        ByteBuffer buf = ByteBuffer.allocate(SIZE);
        if (id > 0xFFFF) {
            buf.putInt(id);
        } else {
            buf.putShort((short) id);
        }

        buf.putShort((short) chunk);
        ByteBufferUtils.putMedium(buf, tail);
        buf.put((byte) type);
        buf.put(data);

        return (ByteBuffer) buf.flip();
    }

    /**
     * Returns the chunk of the file this page contains.
     */
    public int getChunk() {
        return chunk;
    }

    /**
     * Returns this page's data.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Returns the id of the file within this page.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the next page.
     */
    public int getTail() {
        return tail;
    }

    /**
     * Returns the type of file in this page.
     */
    public int getType() {
        return type;
    }

}
