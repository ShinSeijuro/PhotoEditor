/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlugIn;

import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import static javafx.scene.input.Clipboard.getSystemClipboard;
import javafx.scene.input.ClipboardContent;

/**
 *
 * @author CMQ
 */
public class ClipboardWrapper {

    public static Image get() {
        Clipboard clipboard = getSystemClipboard();
        if (clipboard.hasImage()) {
            return clipboard.getImage();
        } else if (clipboard.hasUrl()) {
            return new Image(clipboard.getUrl());
        } else if (clipboard.hasString()) {
            return new Image(clipboard.getString());
        }
        return null;
    }

    public static void set(Image image) {
        Clipboard clipboard = getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putImage(image);
        clipboard.setContent(content);
    }
}
