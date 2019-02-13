/*
  Copyright (c) OpenRS
  <p>
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  <p>
  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.
  <p>
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
 */
package io.versailles.application.storage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A {@link ReleaseManifest} stores checksums and versions of
 * {@link ArchiveManifest}s. When encoded in a {@link Container} and prepended
 * with the file type and id it is more commonly known as the client's
 * "update keys".
 *
 * @author Graham
 * @author `Discardedx2
 * @author Sino
 */
public final class ReleaseManifest {
    /**
     * Decodes the {@link ReleaseManifest} in the specified {@link ByteBuffer}.
     *
     * @param buffer
     *            The {@link ByteBuffer} containing the table.
     * @return The decoded {@link ReleaseManifest}.
     */
    public static ReleaseManifest decode(ByteBuffer buffer) {
        /* find out how many entries there are and allocate a new table */
        int size = buffer.limit() / 8;
        ReleaseManifest manifest = new ReleaseManifest(size);

        /* read the entries */
        for (int i = 0; i < size; i++) {
            int crc = buffer.getInt();
            int version = buffer.getInt();

            manifest.entries[i] = new Entry(crc, version);
        }

        /* if it looks good return the table */
        return manifest;
    }

    /**
     * Represents a single entry in a {@link ReleaseManifest}. Each entry contains
     * a CRC32 checksum and version of the corresponding {@link ArchiveManifest}.
     *
     * @author Graham Edgecombe
     * @author Sino
     */
    public static class Entry {

        /**
         * The CRC32 checksum of the reference table.
         */
        private final int crc;

        /**
         * The version of the reference table.
         */
        private final int version;

        /**
         * Creates a new entry.
         * @param crc
         *            The CRC32 checksum of the slave table.
         * @param version
         *            The version of the slave table.
         */
        public Entry(int crc, int version) {
            this.crc = crc;
            this.version = version;
        }

        /**
         * Returns the CRC32 checksum of the reference table.
         */
        public int getCrc() {
            return crc;
        }

        /**
         * Returns the version of the reference table.
         */
        public int getVersion() {
            return version;
        }

    }

    /**
     * The entries in this manifest.
     */
    private final Entry[] entries;

    /**
     * Creates a new {@link ReleaseManifest} with the specified size.
     */
    public ReleaseManifest(int size) {
        entries = new Entry[size];
    }

    /**
     * Encodes this {@link ReleaseManifest} to a {@link ByteBuffer}.
     */
    public ByteBuffer encode() throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        try (DataOutputStream os = new DataOutputStream(bout)) {
            for (int i = 0; i < entries.length; i++) {
                Entry entry = entries[i];

                os.writeInt(entry.getCrc());
                os.writeInt(entry.getVersion());
            }

            byte[] bytes = bout.toByteArray();
            return ByteBuffer.wrap(bytes);
        }
    }

    /**
     * Returns the size of this manifest.
     */
    public int getSize() {
        return entries.length;
    }

    /**
     * Sets an entry in this manifest.
     *
     * @param id
     *            The id.
     * @param entry
     *            The entry.
     * @throws IndexOutOfBoundsException
     *             if the id is less than zero or greater than or equal to the
     *             size of the table.
     */
    public void setEntry(int id, Entry entry) {
        if (id < 0 || id >= entries.length)
            throw new IndexOutOfBoundsException();
        entries[id] = entry;
    }

    /**
     * Gets an entry from this manifest.
     *
     * @param id
     *            The id.
     * @return The entry.
     * @throws IndexOutOfBoundsException
     *             if the id is less than zero or greater than or equal to the
     *             size of the table.
     */
    public Entry getEntry(int id) {
        if (id < 0 || id >= entries.length)
            throw new IndexOutOfBoundsException();
        return entries[id];
    }
}
