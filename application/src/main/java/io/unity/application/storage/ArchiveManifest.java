/*
 * Copyright (c) OpenRS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.unity.application.storage;

import io.unity.application.storage.util.ByteBufferUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * An {@link ArchiveManifest} contains checksums, versions of each and every
 * folder within the archive this manifest is for.
 *
 * @author Graham
 * @author `Discardedx2
 * @author Sean
 * @author Sino
 */
public final class ArchiveManifest {
    /**
     * References a folder of files within an {@link ArchiveManifest}.
     * @author Graham Edgecombe
     * @author Sino
     */
    public static class FolderManifest {
        /**
         * The id of this manifest.
         */
        private final int id;

        /**
         * The hash representation of this manifest.
         */
        private int identifier = -1;

        /**
         * The CRC32 checksum of this manifest.
         */
        private int crc;

        /**
         * The version of this manifest.
         */
        private int version;

        /**
         * Contains labels for each and every label.
         */
        private LabelHashSet packLabels;

        /**
         * The children in this entry.
         */
        private final SortedMap<Integer, PackManifest> packs = new TreeMap<>();

        /**
         * Creates a new {@link FolderManifest}.
         * @param id The id of this manifest within the game cache.
         */
        public FolderManifest(int id) {
            this.id = id;
        }

        /**
         * Returns the cache id for this manifest.
         */
        public int getId() {
            return id;
        }

        /**
         * Returns the maximum number of child entries.
         */
        public int capacity() {
            if (packs.isEmpty()) {
                return 0;
            }

            return packs.lastKey() + 1;
        }

        /**
         * Returns the CRC32 checksum of this entry.
         */
        public int getCrc() {
            return crc;
        }

        /**
         * Returns the {@link PackManifest} with the specified id.
         */
        public PackManifest getPackManifest(int id) {
            return packs.get(id);
        }

        /**
         * Returns the identifier of this manifest.
         */
        public int getIdentifier() {
            return identifier;
        }

        /**
         * Returns the version of this manifest.
         */
        public int getVersion() {
            return version;
        }

        /**
         * Replaces or inserts the given {@link PackManifest} with the specified id.
         */
        public void putPack(int id, PackManifest entry) {
            packs.put(id, entry);
        }

        /**
         * Removes the specified {@link PackManifest}.
         */
        public void removePack(int id) {
            packs.remove(id);
        }

        /**
         * Sets the CRC32 checksum of this entry.
         */
        public void setCrc(int crc) {
            this.crc = crc;
        }

        /**
         * Returns the identifier of this entry.
         */
        public void setIdentifier(int identifier) {
            this.identifier = identifier;
        }

        /**
         * Sets the version of this entry.
         */
        public void setVersion(int version) {
            this.version = version;
        }

        /**
         * Returns the number of actual {@link PackManifest}.
         */
        public int size() {
            return packs.size();
        }
    }

    /**
     * References a pack of files within a {@link Folder}.
     * @author Graham Edgecombe
     * @author Sino
     */
    public static class PackManifest {
        /**
         * The id of this pack.
         */
        private final int id;

        /**
         * The hash representation of this pack.
         */
        private int labelHash = -1;

        public PackManifest(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public int getLabelHash() {
            return labelHash;
        }

        public void setLabelHash(int labelHash) {
            this.labelHash = labelHash;
        }
    }

    /**
     * Decodes an {@link ArchiveManifest} contained in the specified
     * {@link ByteBuffer}.
     */
    public static ArchiveManifest decode(ByteBuffer buffer) {
        ArchiveManifest manifest = new ArchiveManifest();

        /* read header */
        manifest.format = buffer.get() & 0xFF;
        if (manifest.format < 5 || manifest.format > 7) {
            throw new RuntimeException();
        }

        if (manifest.format >= 6) {
            manifest.version = buffer.getInt();
        }

        manifest.flags = buffer.get() & 0xFF;

        /* read the ids */
        int[] ids = new int[manifest.format >= 7 ? ByteBufferUtils.getSmartInt(buffer) : buffer.getShort() & 0xFFFF];
        int accumulator = 0, size = -1;
        for (int i = 0; i < ids.length; i++) {
            int delta = manifest.format >= 7 ? ByteBufferUtils.getSmartInt(buffer) : buffer.getShort() & 0xFFFF;
            ids[i] = accumulator += delta;
            if (ids[i] > size) {
                size = ids[i];
            }
        }

        size++;

        /* and allocate the folder manifest for each enlisted folder within the archive */
        int index = 0;
        for (int id : ids) {
            manifest.folderManifests.put(id, new FolderManifest(index++));
        }

        /* read the label hashes of each and every folder if present */
        int[] labelHashes = new int[size];
        if (manifest.flags != 0) {
            for (int id : ids) {
                int labelHash = buffer.getInt();

                labelHashes[id] = labelHash;
                manifest.folderManifests.get(id).identifier = labelHash;
            }
        }

        manifest.labels = new LabelHashSet(labelHashes);

        /* read the CRC32 checksums */
        for (int id : ids) {
            manifest.folderManifests.get(id).crc = buffer.getInt();
        }

        /* read the version numbers */
        for (int id : ids) {
            manifest.folderManifests.get(id).version = buffer.getInt();
        }

        /* read the sizes of each and every pack */
        int[][] packs = new int[size][];
        for (int id : ids) {
            packs[id] = new int[buffer.getShort() & 0xFFFF];
        }

        /* read the child ids */
        for (int id : ids) {
            /* reset the accumulator and size */
            accumulator = 0;
            size = -1;

            /* loop through the array of ids */
            for (int i = 0; i < packs[id].length; i++) {
                int delta = manifest.format >= 7 ? ByteBufferUtils.getSmartInt(buffer) : buffer.getShort() & 0xFFFF;
                packs[id][i] = accumulator += delta;
                if (packs[id][i] > size) {
                    size = packs[id][i];
                }
            }
            size++;

            /* and allocate specific entries within the array */
            index = 0;
            for (int child : packs[id]) {
                manifest.folderManifests.get(id).packs.put(child, new PackManifest(index++));
            }
        }

        /* read the label hash of each and every pack within the folder if present */
        if (manifest.flags != 0) {
            for (int id : ids) {
                labelHashes = new int[packs[id].length];
                for (int packId : packs[id]) {
                    int labelHash = buffer.getInt();

                    labelHashes[packId] = labelHash;
                    manifest.folderManifests.get(id).packs.get(packId).labelHash = labelHash;
                }

                manifest.folderManifests.get(id).packLabels = new LabelHashSet(labelHashes);
            }
        }

        /* return the manifest we constructed */
        return manifest;
    }

    /**
     * The format of this manifest.
     */
    private int format;

    /**
     * The version of this manifest.
     */
    private int version;

    /**
     * The flags of this manifest.
     */
    private int flags;

    /**
     * The entries in this manifest.
     */
    private final SortedMap<Integer, FolderManifest> folderManifests = new TreeMap<>();

    /**
     * The set of folder labels.
     */
    private LabelHashSet labels;

    /**
     * Returns the maximum number of entries in this manifest.
     */
    public int capacity() {
        if (folderManifests.isEmpty())
            return 0;

        return folderManifests.lastKey() + 1;
    }

    /**
     * Encodes this {@link ArchiveManifest} into a {@link ByteBuffer}.
     *
     * @return The {@link ByteBuffer}.
     * @throws IOException if an I/O error occurs.
     */
    public ByteBuffer encode() throws IOException {
        /*
         * we can't (easily) predict the size ahead of time, so we write to a
         * stream and then to the buffer
         */
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(bout);
        try {
            /* write the header */
            os.write(format);
            if (format >= 6) {
                os.writeInt(version);
            }
            os.write(flags);

            /* calculate and write the number of non-null entries */
            putSmartFormat(folderManifests.size(), os);

            /* write the ids */
            int last = 0;
            for (int id = 0; id < capacity(); id++) {
                if (folderManifests.containsKey(id)) {
                    int delta = id - last;
                    putSmartFormat(delta, os);
                    last = id;
                }
            }

            /* write the identifiers if required */
            if (flags != 0) {
                for (FolderManifest entry : folderManifests.values()) {
                    os.writeInt(entry.identifier);
                }
            }

            /* write the CRC checksums */
            for (FolderManifest entry : folderManifests.values()) {
                os.writeInt(entry.crc);
            }

            /* write the versions */
            for (FolderManifest entry : folderManifests.values()) {
                os.writeInt(entry.version);
            }

            /* calculate and write the number of non-null child entries */
            for (FolderManifest entry : folderManifests.values()) {
                putSmartFormat(entry.packs.size(), os);
            }

            /* write the folder ids */
            for (FolderManifest entry : folderManifests.values()) {
                last = 0;
                for (int id = 0; id < entry.capacity(); id++) {
                    if (entry.packs.containsKey(id)) {
                        int delta = id - last;
                        putSmartFormat(delta, os);
                        last = id;
                    }
                }
            }

            /* write the pack labels if required */
            if (flags != 0) {
                for (FolderManifest folder : folderManifests.values()) {
                    for (PackManifest pack : folder.packs.values()) {
                        os.writeInt(pack.labelHash);
                    }
                }
            }

            /* convert the stream to a byte array and then wrap a buffer */
            byte[] bytes = bout.toByteArray();
            return ByteBuffer.wrap(bytes);
        } finally {
            os.close();
        }
    }

    /**
     * Returns the {@link FolderManifest} with the specified id.
     */
    public FolderManifest getFolderManifest(int id) {
        return folderManifests.get(id);
    }

    /**
     * Returns the {@link FolderManifest} with the specified {@link FolderType}.
     */
    public FolderManifest getFolderManifest(FolderType archive) {
        return getFolderManifest(archive.getId());
    }

    /**
     * Returns the {@link PackManifest} with the specified id.
     */
    public PackManifest getPackManifest(int folderId, int packId) {
        FolderManifest folderManifest = folderManifests.get(folderId);
        if (folderManifest == null) {
            return null;
        }

        return folderManifest.getPackManifest(packId);
    }

    /**
     * Returns the flags of this manifest.
     */
    public int getFlags() {
        return flags;
    }

    /**
     * Returns the format of this manifest.
     */
    public int getFormat() {
        return format;
    }

    /**
     * Returns the version of this manifest.
     */
    public int getVersion() {
        return version;
    }

    /**
     * Replaces or inserts the given {@link FolderManifest} with the specified id.
     */
    public void putFolderManifest(int id, FolderManifest manifest) {
        folderManifests.put(id, manifest);
    }

    /**
     * Removes the {@link FolderManifest} with the specified id.
     */
    public void removeFolderManifest(int id) {
        folderManifests.remove(id);
    }

    /**
     * Sets the flags of this manifest.
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * Sets the format of this manifest.
     */
    public void setFormat(int format) {
        this.format = format;
    }

    /**
     * Sets the version of this manifest.
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Returns the folder count.
     */
    public int size() {
        return folderManifests.size();
    }

    /**
     * Returns the set of labels of the folders within this archive.
     */
    public LabelHashSet getLabels() {
        return labels;
    }

    /**
     * Puts the data into a certain type by the format id.
     *
     * @param val The value to put into the buffer.
     * @param os  The stream.
     * @throws IOException The exception thrown if an i/o error occurs.
     */
    public void putSmartFormat(int val, DataOutputStream os) throws IOException {
        if (format >= 7)
            putSmartInt(os, val);
        else
            os.writeShort((short) val);
    }

    /**
     * Puts a smart integer into the stream.
     *
     * @param os    The stream.
     * @param value The value.
     * @throws IOException The exception thrown if an i/o error occurs.
     *                     <p>
     *                     Credits to Graham for this method.
     */
    public static void putSmartInt(DataOutputStream os, int value) throws IOException {
        if ((value & 0xFFFF) < 32768)
            os.writeShort(value);
        else
            os.writeInt(0x80000000 | value);
    }
}
