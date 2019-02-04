/**
 * Copyright (c) OpenRS
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.unity.application.storage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * An {@link Folder} is a file within the cache that can have multiple member
 * files inside it.
 *
 * @author Graham
 * @author `Discardedx2
 * @author Sino
 */
public final class Folder {

    /**
     * Decodes the specified {@link ByteBuffer} into an {@link Folder}.
     *
     * @param buffer
     *            The buffer.
     * @param size
     *            The size of the folder.
     * @return The decoded {@link Folder}.
     */
    public static Folder decode(ByteBuffer buffer, int size) {
        /* allocate a new archive object */
        Folder folder = new Folder(size);

        /* read the number of chunks at the end of the archive */
        buffer.position(buffer.limit() - 1);
        int chunks = buffer.get() & 0xFF;

        /* read the sizes of the child entries and individual chunks */
        int[][] chunkSizes = new int[chunks][size];
        int[] sizes = new int[size];
        buffer.position(buffer.limit() - 1 - chunks * size * 4);
        for (int chunk = 0; chunk < chunks; chunk++) {
            int chunkSize = 0;
            for (int id = 0; id < size; id++) {
                /* read the delta-encoded chunk length */
                int delta = buffer.getInt();
                chunkSize += delta;

                /* store the size of this chunk */
                chunkSizes[chunk][id] = chunkSize;

                /* and add it to the size of the whole file */
                sizes[id] += chunkSize;
            }
        }

        /* allocate the buffers for the child entries */
        for (int id = 0; id < size; id++) {
            folder.packs[id] = ByteBuffer.allocate(sizes[id]);
        }

        /* read the data into the buffers */
        buffer.position(0);
        for (int chunk = 0; chunk < chunks; chunk++) {
            for (int id = 0; id < size; id++) {
                /* get the length of this chunk */
                int chunkSize = chunkSizes[chunk][id];

                /* copy this chunk into a temporary buffer */
                byte[] temp = new byte[chunkSize];
                buffer.get(temp);

                /* copy the temporary buffer into the file buffer */
                folder.packs[id].put(temp);
            }
        }

        /* flip all of the buffers */
        for (int id = 0; id < size; id++) {
            folder.packs[id].flip();
        }

        /* return the archive */
        return folder;
    }

    /**
     * The array of packs in this archive.
     */
    private final ByteBuffer[] packs;

    /**
     * Creates a new {@link Folder} of the given size.
     */
    public Folder(int size) {
        this.packs = new ByteBuffer[size];
    }

    /**
     * Encodes this {@link Folder} into a {@link ByteBuffer}.
     * <p />
     * Please note that this is a fairly simple implementation that does not
     * attempt to use more than one chunk.
     *
     * @return An encoded {@link ByteBuffer}.
     * @throws IOException
     *             if an I/O error occurs.
     */
    public ByteBuffer encode() throws IOException { // TODO: an implementation
        // that can use more than
        // one chunk
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(bout);
        try {
            /* add the data for each entry */
            for (int id = 0; id < packs.length; id++) {
                /* copy to temp buffer */
                byte[] temp = new byte[packs[id].limit()];
                packs[id].position(0);
                try {
                    packs[id].get(temp);
                } finally {
                    packs[id].position(0);
                }

                /* copy to output stream */
                os.write(temp);
            }

            /* write the chunk lengths */
            int prev = 0;
            for (int id = 0; id < packs.length; id++) {
                /*
                 * since each file is stored in the only chunk, just write the
                 * delta-encoded file size
                 */
                int chunkSize = packs[id].limit();
                os.writeInt(chunkSize - prev);
                prev = chunkSize;
            }

            /*
             * we only used one chunk due to a limitation of the implementation
             */
            bout.write(1);

            /* wrap the bytes from the stream in a buffer */
            byte[] bytes = bout.toByteArray();
            return ByteBuffer.wrap(bytes);
        } finally {
            os.close();
        }
    }

    /**
     * Gets the pack with the specified id.
     *
     * @param id
     *            The id.
     * @return The entry.
     */
    public ByteBuffer getPack(int id) {
        return packs[id];
    }

    /**
     * Inserts/replaces the entry with the specified id.
     *
     * @param id
     *            The id.
     * @param buffer
     *            The entry.
     */
    public void putPack(int id, ByteBuffer buffer) {
        packs[id] = buffer;
    }

    /**
     * Gets the size of this archive.
     *
     * @return The size of this archive.
     */
    public int size() {
        return packs.length;
    }

}
