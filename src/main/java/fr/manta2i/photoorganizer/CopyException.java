package fr.manta2i.photoorganizer;

import java.io.IOException;

public class CopyException extends RuntimeException{
    public CopyException(IOException e) {
        super(e);
    }
}
