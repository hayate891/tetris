/*
 * Copyright © 2016 spypunk <spypunk@gmail.com>
 *
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package spypunk.tetris.sound;

import javax.sound.sampled.spi.AudioFileReader;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

public enum Sound {

    SHAPE_LOCKED(Format.WAV, false),
    GAME_OVER(Format.WAV, true),
    BACKGROUND(Format.MP3, true);

    public enum Format {
        WAV {
            @Override
            public AudioFileReader getFileReader() {
                return new com.sun.media.sound.WaveFileReader();
            }
        },
        MP3 {
            @Override
            public AudioFileReader getFileReader() {
                return new MpegAudioFileReader();
            }
        };

        public abstract AudioFileReader getFileReader();
    }

    private final String fileName;

    private final Format format;

    private final boolean loop;

    private Sound(Format format, boolean loop) {
        fileName = name().toLowerCase() + "." + format.name().toLowerCase();
        this.format = format;
        this.loop = loop;
    }

    public String getFileName() {
        return fileName;
    }

    public Format getFormat() {
        return format;
    }

    public boolean isLoop() {
        return loop;
    }
}